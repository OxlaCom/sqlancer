package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.schema.OxlaTable;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class OxlaDropTableGenerator extends OxlaQueryGenerator {
    private static final Collection<String> errors = List.of(
            "only DROP SCHEMA and TABLE are currently supported"
    );
    private static final Collection<Pattern> regexErrors = List.of();
    public static final ExpectedErrors expectedErrors = new ExpectedErrors(errors, regexErrors);

    public OxlaDropTableGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    @Override
    public SQLQueryAdapter getQuery(int ignored) {
        final OxlaTable randomTable = globalState.getSchema().getRandomTable();
        final StringBuilder queryBuilder = new StringBuilder()
                .append("DROP ")
                .append(Randomly.getBoolean() ? "TABLE " : "VIEW ")
                .append(Randomly.getBoolean() ? "IF EXISTS " : "")
                .append(randomTable.getName());
        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }
}
