package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.DBMSCommon;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.schema.OxlaDataType;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class OxlaCreateTableGenerator extends OxlaQueryGenerator {
    enum Rule {FILE, SIMPLE, SELECT, VIEW}

    private static final Collection<String> errors = List.of();
    private static final Collection<Pattern> regexErrors = List.of(
            Pattern.compile("relation \"[^\"]*\" already exists"),
            Pattern.compile("column \"[^\"]*\" has unsupported type")
    );
    public static final ExpectedErrors expectedErrors = new ExpectedErrors(errors, regexErrors);

    public OxlaCreateTableGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    public static SQLQueryAdapter generate(OxlaGlobalState globalState) {
        return new OxlaCreateTableGenerator(globalState).simpleRule();
    }

    @Override
    public SQLQueryAdapter getQuery() {
        final Rule rule = Randomly.fromOptions(Rule.values());
        switch (rule) {
            case FILE:
                return fileRule();
            case SIMPLE:
                return simpleRule();
            case SELECT:
                return selectRule();
            case VIEW:
                return viewRule();
            default:
                throw new AssertionError(rule);
        }
    }

    private SQLQueryAdapter fileRule() {
        final StringBuilder queryBuilder = new StringBuilder()
                .append("CREATE TABLE ")
                .append(Randomly.getBoolean() ? "IF NOT EXISTS " : "")
                .append(DBMSCommon.createTableName(globalState.getSchema().getDatabaseTables().size()))
                .append(" FROM ")
                .append(generator.generateExpression(OxlaDataType.TEXT).toString().replaceAll("'", ""))
                .append(" FILE ")
                .append(generator.generateExpression(OxlaDataType.TEXT));

        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }

    private SQLQueryAdapter simpleRule() {
        final StringBuilder queryBuilder = new StringBuilder()
                .append("CREATE TABLE ")
                .append(Randomly.getBoolean() ? "IF NOT EXISTS " : "")
                .append(DBMSCommon.createTableName(globalState.getSchema().getDatabaseTables().size()))
                .append('(');

        final int columnCount = Randomly.smallNumber() + 1;
        for (int index = 0; index < columnCount; ++index) {
            queryBuilder.append(DBMSCommon.createColumnName(index))
                    .append(' ')
                    .append(OxlaDataType.getRandomType())
                    .append(' ')
                    .append(Randomly.fromOptions("NOT NULL", "NULL"));
            if (index + 1 < columnCount) {
                queryBuilder.append(',');
            }
        }
        queryBuilder.append(')');

        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }

    private SQLQueryAdapter selectRule() {
        final StringBuilder queryBuilder = new StringBuilder()
                .append("CREATE TABLE ")
                .append(Randomly.getBoolean() ? "IF NOT EXISTS " : "")
                .append(DBMSCommon.createTableName(globalState.getSchema().getDatabaseTables().size()))
                .append(" AS ")
                .append(OxlaSelectGenerator.generate(globalState));
        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }

    private SQLQueryAdapter viewRule() {
        // FIXME: This rule also takes optional list of columns (literals) matching the count of SELECT statements' WHAT.
        final StringBuilder queryBuilder = new StringBuilder()
                .append("CREATE VIEW ")
                .append(Randomly.getBoolean() ? "IF NOT EXISTS " : "")
                .append(DBMSCommon.createTableName(globalState.getSchema().getDatabaseTables().size()))
                .append("_view")
                .append(" AS ")
                .append(OxlaSelectGenerator.generate(globalState));
        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }
}
