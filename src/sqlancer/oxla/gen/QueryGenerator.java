package sqlancer.oxla.gen;

import sqlancer.common.query.SQLQueryAdapter;

public interface QueryGenerator {
    SQLQueryAdapter getRandomQuery();
}
