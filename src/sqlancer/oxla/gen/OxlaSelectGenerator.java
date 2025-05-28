package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.ast.OxlaExpression;
import sqlancer.oxla.schema.OxlaColumn;
import sqlancer.oxla.schema.OxlaDataType;
import sqlancer.oxla.schema.OxlaTables;

import java.util.List;

class OxlaSelectGenerator implements QueryGenerator {
    enum SelectRule {SIMPLE, UNION, INTERSECT, EXCEPT}

    private static final ExpectedErrors errors = new ExpectedErrors();
    private final OxlaExpressionGenerator generator;
    private final OxlaGlobalState globalState;

    public OxlaSelectGenerator(OxlaGlobalState globalState) {
        this.globalState = globalState;
        generator = new OxlaExpressionGenerator(globalState);
    }

    @Override
    public SQLQueryAdapter getRandomQuery() {
        final SelectRule rule = Randomly.fromOptions(SelectRule.values());
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

    private SQLQueryAdapter Rule() {
        return new SQLQueryAdapter(null, errors);
    }

    private SQLQueryAdapter simpleRule() {
        String query = "SELECT ";

        // TOP
        if (Randomly.getBoolean()) {
            final OxlaExpression intValue = generator.generateConstant(Randomly.fromOptions(OxlaDataType.INTEGER));
            query += String.format("TOP %s ", intValue.toString());
        }

        // TYPE
        if (Randomly.getBoolean()) {
            query += "DISTINCT ";
        }

        // WHAT
        final OxlaTables randomSelectTables = globalState.getSchema().getRandomTableNonEmptyTables();
        final List<OxlaColumn> fetchColumns = randomSelectTables.getColumns();

        // INTO
        if (Randomly.getBoolean()) {

        }

        // FROM
        if (Randomly.getBoolean()) {

        }

        // WHERE
        if (Randomly.getBoolean()) {
            final OxlaExpressionGenerator whereGenerator = new OxlaExpressionGenerator(globalState).setColumns(fetchColumns);
            final OxlaExpression predicate = whereGenerator.generatePredicate();
            query += OxlaToStringVisitor.asString(predicate);
        }

        // GROUP BY
        if (Randomly.getBoolean()) {

        }

        // WINDOWS

        return new SQLQueryAdapter(query, errors);
    }

    private SQLQueryAdapter unionRule() {
        SQLQueryAdapter firstSelectQuery = getRandomQuery();
        SQLQueryAdapter secondSelectQuery = getRandomQuery();
        boolean isUnionAll = Randomly.getBoolean();
        return new SQLQueryAdapter(
                String.format("%s UNION%s %s", firstSelectQuery, isUnionAll ? " ALL" : "", secondSelectQuery),
                errors);
    }

    private SQLQueryAdapter intersectRule() {
        SQLQueryAdapter firstSelectQuery = getRandomQuery();
        SQLQueryAdapter secondSelectQuery = getRandomQuery();
        return new SQLQueryAdapter(
                String.format("%s INTERSECT %s", firstSelectQuery, secondSelectQuery),
                errors);
    }

    private SQLQueryAdapter exceptRule() {
        SQLQueryAdapter firstSelectQuery = getRandomQuery();
        SQLQueryAdapter secondSelectQuery = getRandomQuery();
        return new SQLQueryAdapter(
                String.format("%s EXPECT %s", firstSelectQuery, secondSelectQuery),
                errors);
    }
}
