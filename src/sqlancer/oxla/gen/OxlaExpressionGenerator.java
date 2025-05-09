package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.gen.NoRECGenerator;
import sqlancer.common.gen.TypedExpressionGenerator;
import sqlancer.common.schema.AbstractTables;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.ast.*;
import sqlancer.oxla.schema.OxlaColumn;
import sqlancer.oxla.schema.OxlaDataType;
import sqlancer.oxla.schema.OxlaTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OxlaExpressionGenerator extends TypedExpressionGenerator<OxlaExpression, OxlaColumn, OxlaDataType>
        implements NoRECGenerator<OxlaSelect, OxlaJoin, OxlaExpression, OxlaTable, OxlaColumn> {
    private final OxlaGlobalState globalState;
    private List<OxlaTable> tables;

    public OxlaExpressionGenerator(OxlaGlobalState globalState) {
        this.globalState = globalState;
    }

    private enum ExpressionType {
        UNARY_PREFIX, UNARY_POSTFIX;

        public static ExpressionType getRandom() {
            return Randomly.fromOptions(values());
        }
    }

    @Override
    public OxlaExpression generateConstant(OxlaDataType type) {
        if (Randomly.getBooleanWithSmallProbability()) {
            return OxlaConstant.createNullConstant();
        }
        // FIXME Imho, we should generate a random constant that is implicitly or explicitly cast-able to the wanted type.
        OxlaExpression expression = OxlaConstant.getRandomForType(globalState, type);
        if (Randomly.getBooleanWithRatherLowProbability()) {
            return new OxlaCast(expression, type); // Explicit cast to self type.
        }
        return expression;
    }

    @Override
    protected OxlaExpression generateExpression(OxlaDataType wantReturnType, int depth) {
        if (depth >= globalState.getOptions().getMaxExpressionDepth() || Randomly.getBoolean()) {
            return generateLeafNode(wantReturnType);
        }

        ExpressionType expressionType = ExpressionType.getRandom();
        switch (expressionType) {
            case UNARY_PREFIX:
                return generateOperator(OxlaUnaryPrefixOperation::new, OxlaUnaryPrefixOperation.ALL, wantReturnType, depth);
            case UNARY_POSTFIX:
                return generateOperator(OxlaUnaryPostfixOperation::new, OxlaUnaryPostfixOperation.ALL, wantReturnType, depth);
            default:
                throw new AssertionError(expressionType);
        }
    }

    @Override
    protected OxlaExpression generateColumn(OxlaDataType type) {
        // FIXME Iterate over all columns and:
        //       1. check their 'cast-ability':
        //          - exclude impossible casts from the list,
        //          - do nothing if the cast would be implicit,
        //          - generate cast expression if the cast is explicit.
        //       2. (?) Throw an error if the resulting list is empty.
        //       Potentially add a boolean switch for the behavior above.
        return new OxlaColumnReference(Randomly.fromList(columns
                .stream()
                .filter(column -> (column.getType() == type))
                .collect(Collectors.toList())));
    }

    @Override
    protected OxlaDataType getRandomType() {
        return OxlaDataType.getRandomType();
    }

    @Override
    protected boolean canGenerateColumnOfType(OxlaDataType type) {
        return columns.stream().anyMatch(column -> column.getType() == type);
    }

    @Override
    public OxlaExpression generatePredicate() {
        return generateExpression(OxlaDataType.BOOLEAN);
    }

    @Override
    public OxlaExpression negatePredicate(OxlaExpression predicate) {
        return new OxlaUnaryPrefixOperation(predicate, OxlaUnaryPrefixOperation.NOT);
    }

    @Override
    public OxlaExpression isNull(OxlaExpression expr) {
        return new OxlaUnaryPostfixOperation(expr, OxlaUnaryPostfixOperation.IS_NULL);
    }

    @Override
    public OxlaExpressionGenerator setTablesAndColumns(AbstractTables<OxlaTable, OxlaColumn> tables) {
        this.columns = tables.getColumns();
        this.tables = tables.getTables();
        return this;
    }

    @Override
    public OxlaExpression generateBooleanExpression() {
        return generateExpression(OxlaDataType.BOOLEAN);
    }

    @Override
    public OxlaSelect generateSelect() {
        return new OxlaSelect();
    }

    @Override
    public List<OxlaJoin> getRandomJoinClauses() {
        List<OxlaJoin> joinStatements = new ArrayList<>();
        if (Randomly.getBooleanWithRatherLowProbability()) {
            return joinStatements;
        }
        List<OxlaTableReference> tableReferences = tables.stream().map(OxlaTableReference::new).collect(Collectors.toList());
        while (tableReferences.size() >= 2 && Randomly.getBoolean()) {
            OxlaTableReference leftTable = tableReferences.removeLast();
            OxlaTableReference rightTable = tableReferences.removeLast();
            List<OxlaColumn> columns = Stream.concat(leftTable.getTable().getColumns().stream(), rightTable.getTable().getColumns().stream()).collect(Collectors.toList());
            OxlaExpressionGenerator joinGenerator = new OxlaExpressionGenerator(globalState).setColumns(columns);
            OxlaJoin.JoinType joinType = OxlaJoin.JoinType.getRandom();
            joinStatements.add(new OxlaJoin(leftTable, rightTable, joinType, joinType != OxlaJoin.JoinType.CROSS
                    ? joinGenerator.generateExpression(OxlaDataType.BOOLEAN)
                    : null));
        }
        tables = tableReferences.stream().map(OxlaTableReference::getTable).collect(Collectors.toList());
        return joinStatements;
    }

    @Override
    public List<OxlaExpression> getTableRefs() {
        return tables.stream().map(OxlaTableReference::new).collect(Collectors.toList());
    }

    @Override
    public String generateOptimizedQueryString(OxlaSelect select, OxlaExpression whereCondition, boolean shouldUseAggregate) {
        select.type = OxlaSelect.SelectType.ALL;
        select.setWhereClause(whereCondition);
        if (shouldUseAggregate) {
            // TODO OXLA-8194 use `COUNT` Aggregate function instead of hardcoding it here.
            final OxlaExpression aggregate = new OxlaColumnReference(new OxlaColumn("COUNT(*)", OxlaDataType.INT64));
            select.setFetchColumns(List.of(aggregate));
        } else {
            select.setFetchColumns(columns.stream().map(OxlaColumnReference::new).collect(Collectors.toList()));
            if (Randomly.getBooleanWithSmallProbability()) {
                select.setOrderByClauses(List.of(OxlaConstant.getRandom(globalState)));
            }
        }
        return select.asString();
    }

    @Override
    public String generateUnoptimizedQueryString(OxlaSelect select, OxlaExpression whereCondition) {
        final OxlaPostfixText asText = new OxlaPostfixText(new OxlaCast(whereCondition, OxlaDataType.INT32), " as count");
        select.setFetchColumns(List.of(asText));
        select.setWhereClause(null);
        return "SELECT SUM(COUNT) FROM (" + select.asString() + ") as res";
    }

    @FunctionalInterface
    interface OxlaUnaryOperatorFactory {
        OxlaExpression create(OxlaExpression expr, OxlaOperator op);
    }

    private OxlaExpression generateOperator(OxlaUnaryOperatorFactory factory, List<OxlaOperator> operators, OxlaDataType wantReturnType, int depth) {
        List<OxlaOperator> validOperators = new ArrayList<>(operators);
        validOperators.removeIf(operator -> operator.overload.returnType != wantReturnType);

        if (validOperators.isEmpty()) {
            // In case no operator matches the criteria - we can safely generate a leaf expression instead.
            return generateLeafNode(wantReturnType);
        }

        OxlaOperator randomOperator = Randomly.fromList(validOperators);
        OxlaExpression inputExpression = generateExpression(randomOperator.overload.inputTypes[0], depth + 1);
        return factory.create(inputExpression, randomOperator);
    }
}
