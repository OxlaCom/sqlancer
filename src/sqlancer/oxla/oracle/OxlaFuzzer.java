package sqlancer.oxla.oracle;

import sqlancer.IgnoreMeException;
import sqlancer.common.oracle.TestOracle;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.common.query.SQLancerResultSet;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaOptions;
import sqlancer.oxla.gen.OxlaCreateTableGenerator;
import sqlancer.oxla.gen.OxlaDeleteTableGenerator;
import sqlancer.oxla.gen.OxlaInsertIntoGenerator;
import sqlancer.oxla.gen.OxlaSelectGenerator;
import sqlancer.oxla.schema.OxlaTable;

import java.util.List;

public class OxlaFuzzer implements TestOracle<OxlaGlobalState> {
    private final OxlaGlobalState globalState;
    private final ExpectedErrors errors;

    private final OxlaCreateTableGenerator createTableGenerator;
    private final OxlaDeleteTableGenerator deleteTableGenerator;
    private final OxlaInsertIntoGenerator insertIntoGenerator;
    private final OxlaSelectGenerator selectGenerator;

    public OxlaFuzzer(OxlaGlobalState globalState, ExpectedErrors errors) {
        this.globalState = globalState;
        this.errors = errors;

        createTableGenerator = new OxlaCreateTableGenerator(this.globalState);
        deleteTableGenerator = new OxlaDeleteTableGenerator(this.globalState);
        insertIntoGenerator = new OxlaInsertIntoGenerator(this.globalState);
        selectGenerator = new OxlaSelectGenerator(this.globalState);
    }

    @Override
    public void check() throws Exception {
        try {
            SQLQueryAdapter query = selectGenerator.getQuery();
            globalState.executeStatement(query);
            globalState.getManager().incrementSelectQueryCount();
        } catch (Error e) {
            if (errors.errorIsExpected(e.getMessage())) {
                throw new IgnoreMeException();
            }
            throw new AssertionError(e);
        }
    }

    public void ensureValidDatabaseState() throws Exception {
        // 1. Delete random tables until we're not over the specified limit...
        final OxlaOptions options = globalState.getDbmsSpecificOptions();
        final List<OxlaTable> presentTables = globalState.getSchema().getDatabaseTables();
        if (presentTables.size() > options.maxTableCount) {
            globalState.executeStatement(deleteTableGenerator.getQuery());
        }

        // 2. ...but if we're under, then generate them until the upper limit is reached.
        if (presentTables.size() < options.minTableCount) {
            while (presentTables.size() < options.maxTableCount) {
                globalState.executeStatement(createTableGenerator.getQuery());
            }
        }

        // 3. Insert values if a table's row count fell below some specified threshold.
        for (OxlaTable table : presentTables) {
            final SQLQueryAdapter rowCountQuery = new SQLQueryAdapter(String.format("SELECT COUNT(*) FROM %s", table.getName()));
            try (SQLancerResultSet rowCountResult = globalState.executeStatementAndGet(rowCountQuery)) {
                int rowCount = rowCountResult.getInt(1);
                assert !rowCountResult.next();
                if (rowCount < options.minRowCount) {
                    globalState.executeStatement(insertIntoGenerator.getQuery());
                }
            } catch (Exception e) {
                throw new AssertionError("[OxlaFuzzer] failed to insert rows to a table '" + table.getName() + "', because: " + e);
            }
        }

        // FIXME: What about cases where we've run an UPDATE? Should we just drop all rows and repopulate?
    }
}
