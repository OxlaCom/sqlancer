package sqlancer.oxla.oracle;

import sqlancer.IgnoreMeException;
import sqlancer.common.oracle.TestOracle;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.gen.OxlaSelectGenerator;

public class OxlaFuzzer implements TestOracle<OxlaGlobalState> {
    private final OxlaGlobalState globalState;
    private final ExpectedErrors errors;

    private final OxlaSelectGenerator selectGenerator;

    public OxlaFuzzer(OxlaGlobalState globalState, ExpectedErrors errors) {
        this.globalState = globalState;
        this.errors = errors;

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
}
