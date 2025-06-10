package sqlancer.oxla.gen;

import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;

import java.util.List;
import java.util.regex.Pattern;

public class OxlaDropSchemaGenerator extends OxlaQueryGenerator {
    private static final List<String> errors = List.of();
    private static final List<Pattern> regexErrors = List.of();
    private static final ExpectedErrors expectedErrors = new ExpectedErrors(errors, regexErrors);

    @Override
    public SQLQueryAdapter getQuery(OxlaGlobalState globalState, int depth) {
        final var queryBuilder = new StringBuilder()
                .append("DROP SCHEMA ");
        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }

    @Override
    public boolean modifiesDatabaseState() {
        return true;
    }
}
