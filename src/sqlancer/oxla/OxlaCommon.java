package sqlancer.oxla;

import java.util.List;

public class OxlaCommon {
    private OxlaCommon() {
    }

    public static List<String> SYNTAX_ERRORS = List.of(
            "invalid input syntax for type"
    );
    public static final List<String> JOIN_ERRORS = List.of(
            "invalid JOIN ON clause condition. Only equi join is supported"
    );
    public static final List<String> GROUP_BY_ERRORS = List.of(
            "non-integer constant in GROUP BY",
            "is not in select list"
    );
    public static final List<String> ORDER_BY_ERRORS = List.of(
            "non-integer constant in ORDER BY"
    );
}
