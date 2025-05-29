package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.ast.OxlaExpression;
import sqlancer.oxla.schema.OxlaDataType;
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
        generator.setTablesAndColumns(randomSelectTables);
        List<OxlaExpression> what = new ArrayList<>();
        for (int index = 0; index < Randomly.smallNumber() + 1; ++index) {
            what.add(generator.generateExpression(OxlaDataType.getRandomType()));
        }
        queryBuilder.append(what
                .stream()
                .map(OxlaToStringVisitor::asString)
                .collect(Collectors.joining(", ")));

        // INTO
        if (Randomly.getBoolean()) {
            queryBuilder.append(" INTO ");
            // TODO
        }

        // FROM
        if (Randomly.getBoolean()) {
            // TODO
            query += String.format("FROM %s ", "TODO");
        }

        // WHERE
        if (Randomly.getBoolean()) {
            final OxlaExpressionGenerator whereGenerator = new OxlaExpressionGenerator(globalState).setColumns(fetchColumns);
            final OxlaExpression predicate = whereGenerator.generatePredicate();
            query += String.format("WHERE %s ", OxlaToStringVisitor.asString(predicate));
        }

        // GROUP BY
        if (Randomly.getBoolean()) {
            // TODO
            query += String.format("GROUP BY %s ", "TODO");
        }

        // WINDOWS
        if (Randomly.getBoolean()) {
            // TODO
        }

        return new SQLQueryAdapter(query, errors);
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
