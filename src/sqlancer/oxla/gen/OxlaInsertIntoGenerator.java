package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.ast.OxlaColumnReference;
import sqlancer.oxla.schema.OxlaColumn;
import sqlancer.oxla.schema.OxlaTable;

import java.util.List;
import java.util.stream.Collectors;

// TODO OXLA-8192 WITH clause rule.
public class OxlaInsertIntoGenerator extends OxlaQueryGenerator {
    enum Rule {SIMPLE, SELECT}

    private static final ExpectedErrors errors = new ExpectedErrors();
    private final int minRowCount;
    private final int maxRowCount;


    public OxlaInsertIntoGenerator(OxlaGlobalState globalState) {
        super(globalState);
        minRowCount = globalState.getDbmsSpecificOptions().minRowCount;
        maxRowCount = globalState.getDbmsSpecificOptions().maxRowCount;
    }

    public SQLQueryAdapter getQueryForTable(OxlaTable table) {
        return simpleRule(table);
    }

    @Override
    public SQLQueryAdapter getQuery() {
        final Rule rule = Randomly.fromOptions(Rule.values());
        final OxlaTable table = globalState.getSchema().getRandomTable();
        switch (rule) {
            case SIMPLE:
                return simpleRule(table);
            case SELECT:
                return selectRule(table);
            default:
                throw new AssertionError(rule);
        }
    }

    private SQLQueryAdapter simpleRule(OxlaTable table) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ")
                .append(table.getName())
                .append(' ');

        final List<OxlaColumn> randomColumns = Randomly.nonEmptySubset(table.getColumns());
        if (Randomly.getBoolean()) {
            queryBuilder
                    .append('(')
                    .append(
                            OxlaToStringVisitor.asString(
                                    randomColumns
                                            .stream()
                                            .map(OxlaColumnReference::new)
                                            .collect(Collectors.toList())))
                    .append(") ");
        }

        queryBuilder.append("VALUES ");
        final int rowCount = globalState.getRandomly().getInteger(minRowCount, maxRowCount + 1); // [)
        final int columnCount = randomColumns.size();
        for (int rowIndex = 0; rowIndex < rowCount; ++rowIndex) {
            queryBuilder.append('(');
            for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex) {
                queryBuilder.append(generator.generateExpression(randomColumns.get(columnIndex).getType()));
                if (columnIndex + 1 != columnCount) {
                    queryBuilder.append(',');
                }
            }
            queryBuilder.append(')');
            if (rowIndex + 1 != rowCount) {
                queryBuilder.append(',');
            }
        }

        return new SQLQueryAdapter(queryBuilder.toString(), errors);
    }

    private SQLQueryAdapter selectRule(OxlaTable table) {
        final StringBuilder queryBuilder = new StringBuilder();

        return new SQLQueryAdapter(queryBuilder.toString(), errors);
    }
}
