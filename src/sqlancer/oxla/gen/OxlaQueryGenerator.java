package sqlancer.oxla.gen;

import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;

public abstract class OxlaQueryGenerator {
    /**
     * Generates a new query.
     *
     * @param globalState Current database state.
     * @param depth Starting depth of an expression generator (will nest expressions as long as depth < max depth).
     * @return Query corresponding to a given Generator.
     */
    public abstract SQLQueryAdapter getQuery(OxlaGlobalState globalState, int depth);

    /**
     *
     */
    public boolean modifiesDatabaseState() {
        return false;
    }
}
