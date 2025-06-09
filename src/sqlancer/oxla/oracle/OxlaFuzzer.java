package sqlancer.oxla.oracle;

import sqlancer.IgnoreMeException;
import sqlancer.common.oracle.TestOracle;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.gen.*;
import sqlancer.oxla.util.RandomCollection;

public class OxlaFuzzer implements TestOracle<OxlaGlobalState> {
    private final OxlaGlobalState globalState;
    private final RandomCollection<OxlaQueryGenerator> generators;

    public OxlaFuzzer(OxlaGlobalState globalState) {
        this.globalState = globalState;

        generators = new RandomCollection<OxlaQueryGenerator>()
                .add(5, new OxlaCreateTableGenerator())
                .add(2, new OxlaDeleteFromGenerator())
                .add(2, new OxlaUpdateGenerator())
                .add(3, new OxlaDropTableGenerator())
                .add(10, new OxlaInsertIntoGenerator())
                .add(200, new OxlaSelectGenerator());
    }

    @Override
    public void check() throws Exception {
        final OxlaQueryGenerator generator = generators.getRandom();
        final SQLQueryAdapter query = generator.getQuery(globalState, 0);
        try {
            globalState.executeStatement(query);
            globalState.getManager().incrementSelectQueryCount();
        } catch (Error e) {
            if (query.getExpectedErrors().errorIsExpected(e.getMessage())) {
                throw new IgnoreMeException();
            }
            throw new AssertionError(e);
        }
    }
}
