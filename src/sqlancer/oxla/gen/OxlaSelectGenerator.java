package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.ast.OxlaExpression;
import sqlancer.oxla.schema.OxlaColumn;
import sqlancer.oxla.schema.OxlaDataType;
import sqlancer.oxla.schema.OxlaTable;
import sqlancer.oxla.schema.OxlaTables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO OXLA-8192 Implement ORDER BY / LIMIT rules.
public class OxlaSelectGenerator extends OxlaQueryGenerator {
    private enum Rule {SIMPLE, UNION, INTERSECT, EXCEPT}

    private static final ExpectedErrors errors = new ExpectedErrors();

    public OxlaSelectGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    public static SQLQueryAdapter generate(OxlaGlobalState globalState) {
        return new OxlaSelectGenerator(globalState).getQuery();
    }

    @Override
    public SQLQueryAdapter getQuery() {
        final Rule rule = Randomly.fromOptions(Rule.values());
        switch (rule) {
            case SIMPLE:
                return simpleRule();
            case UNION:
                return unionRule();
            case INTERSECT:
                return intersectRule();
            case EXCEPT:
                return exceptRule();
            default:
                throw new AssertionError(rule);
        }
    }

    private SQLQueryAdapter simpleRule() {
        final StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT ");

        // TOP
        if (Randomly.getBoolean()) {
            final OxlaExpression intValue = generator.generateConstant(Randomly.fromOptions(OxlaDataType.INTEGER));
            queryBuilder.append("TOP ")
                    .append(intValue)
                    .append(' ');
        }

        // TYPE
        if (Randomly.getBoolean()) {
            queryBuilder.append("DISTINCT ");
        }

        // WHAT
        final OxlaTables randomSelectTables = globalState.getSchema().getRandomTableNonEmptyTables();
        generator.setTablesAndColumns(randomSelectTables); // TODO: Separate generator for this?
        List<OxlaExpression> what = new ArrayList<>();
        for (int index = 0; index < Randomly.smallNumber() + 1; ++index) {
            what.add(generator.generateExpression(OxlaDataType.getRandomType()));
        }
        queryBuilder.append(OxlaToStringVisitor.asString(what));

        // INTO
//        if (Randomly.getBoolean()) {
//            queryBuilder.append(" INTO ");
//            // TODO
//        }

        // FROM
        if (Randomly.getBoolean()) {
            queryBuilder
                    .append(" FROM ")
                    .append(randomSelectTables
                            .getColumns()
                            .stream()
                            .map(OxlaColumn::getTable)
                            .map(OxlaTable::toString)
                            .collect(Collectors.joining(",")));
        }

        // WHERE
        if (Randomly.getBoolean()) {
            queryBuilder.append(" WHERE ")
                    .append(OxlaToStringVisitor.asString(generator.generatePredicate()));
        }

        // GROUP BY
        if (Randomly.getBoolean()) {
            final List<OxlaExpression> groupByExpressions = generator.generateExpressions(Randomly.smallNumber() + 1);
            queryBuilder.append(" GROUP BY ").append(OxlaToStringVisitor.asString(groupByExpressions));

            // HAVING
            if (Randomly.getBoolean()) {
                queryBuilder.append(" HAVING ").append(OxlaToStringVisitor.asString(generator.generatePredicate()));
            }
        }

        // WINDOWS
//        if (Randomly.getBoolean()) {
//            // TODO
//        }

        return new SQLQueryAdapter(queryBuilder.toString(), errors);
    }

    private SQLQueryAdapter unionRule() {
        SQLQueryAdapter firstSelectQuery = getQuery();
        SQLQueryAdapter secondSelectQuery = getQuery();
        boolean isUnionAll = Randomly.getBoolean();
        return new SQLQueryAdapter(
                String.format("%s UNION%s %s", firstSelectQuery, isUnionAll ? " ALL" : "", secondSelectQuery),
                errors);
    }

    private SQLQueryAdapter intersectRule() {
        SQLQueryAdapter firstSelectQuery = getQuery();
        SQLQueryAdapter secondSelectQuery = getQuery();
        return new SQLQueryAdapter(
                String.format("%s INTERSECT %s", firstSelectQuery, secondSelectQuery),
                errors);
    }

    private SQLQueryAdapter exceptRule() {
        SQLQueryAdapter firstSelectQuery = getQuery();
        SQLQueryAdapter secondSelectQuery = getQuery();
        return new SQLQueryAdapter(
                String.format("%s EXPECT %s", firstSelectQuery, secondSelectQuery),
                errors);
    }
}
