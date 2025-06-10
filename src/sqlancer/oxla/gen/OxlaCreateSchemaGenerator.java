package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.DBMSCommon;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;

import java.util.List;
import java.util.regex.Pattern;

public class OxlaCreateSchemaGenerator extends OxlaQueryGenerator {
    private static final List<String> errors = List.of();
    private static final List<Pattern> regexErrors = List.of();
    private static final ExpectedErrors expectedErrors = new ExpectedErrors(errors, regexErrors);

    @Override
    public SQLQueryAdapter getQuery(OxlaGlobalState globalState, int depth) {
        // create_schema := CREATE SCHEMA [IF NOT EXISTS] database_object_name
        final var queryBuilder = new StringBuilder()
                .append("CREATE SCHEMA ")
                .append(Randomly.getBoolean() ? "IF NOT EXISTS " : "")
                .append(DBMSCommon.createSchemaName(Randomly.smallNumber()));
        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }

    @Override
    public boolean modifiesDatabaseState() {
        return true;
    }
}
