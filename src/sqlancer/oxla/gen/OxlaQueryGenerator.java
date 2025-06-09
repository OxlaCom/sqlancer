package sqlancer.oxla.gen;

import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;

public abstract class OxlaQueryGenerator {
    public abstract SQLQueryAdapter getQuery(OxlaGlobalState globalState, int depth);
}
