package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.schema.OxlaTable;

public class OxlaDeleteFromGenerator extends OxlaQueryGenerator {
    enum Rule {SIMPLE, WITH_CLAUSE}

    private static final ExpectedErrors errors = new ExpectedErrors();

    public OxlaDeleteFromGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    @Override
    public SQLQueryAdapter getQuery() {
        final Rule rule = Randomly.fromOptions(Rule.values());
        switch (rule) {
            case SIMPLE:
                return simpleRule();
            case WITH_CLAUSE:
                return withClauseRule();
            default:
                throw new AssertionError(rule);
        }
    }

    private SQLQueryAdapter simpleRule() {
        final OxlaTable table = Randomly.fromList(globalState.getSchema().getDatabaseTables());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder = appendCommonPart(queryBuilder, table);
        return new SQLQueryAdapter(queryBuilder.toString(), errors);
    }

    private SQLQueryAdapter withClauseRule() {
        final String query = new StringBuilder()
                .toString();
        // TODO OXLA-8192 WITH clause rule.
        return new SQLQueryAdapter(query, errors);
    }

    private StringBuilder appendCommonPart(StringBuilder queryBuilder, OxlaTable table) {
        queryBuilder.append("DELETE FROM ")
                .append(Randomly.getBoolean() ? "ONLY " : "")
                .append(table.getName());

        if (Randomly.getBoolean()) {
            queryBuilder.append(" AS ")
                    .append(table.getName())
                    .append('_')
                    .append("_aliased")
                    .append(' ');
        }

        if (Randomly.getBoolean()) {
            queryBuilder.append("WHERE ").append(OxlaToStringVisitor.asString(generator.generatePredicate()));
        }

        return queryBuilder;
    }
}
