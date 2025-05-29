package sqlancer.oxla.gen;

import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;

public class OxlaDeleteTableGenerator extends OxlaQueryGenerator {
    public OxlaDeleteTableGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    @Override
    public SQLQueryAdapter getQuery() {
        return null;
    }
}
