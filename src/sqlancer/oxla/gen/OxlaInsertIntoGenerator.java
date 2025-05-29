package sqlancer.oxla.gen;

import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaGlobalState;

public class OxlaInsertIntoGenerator extends OxlaQueryGenerator {
    public OxlaInsertIntoGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    @Override
    public SQLQueryAdapter getQuery() {
        return null;
    }
}
