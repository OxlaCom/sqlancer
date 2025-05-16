package sqlancer.oxla;

import sqlancer.common.query.ExpectedErrors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OxlaCommon {
    private OxlaCommon() {
    }

    public static final List<String> SYNTAX_ERRORS = List.of(
            "invalid input syntax for type"
    );
    public static final List<Pattern> SYNTAX_REGEX_ERRORS = List.of(
            Pattern.compile("operator \"[^\"]+\" is not unique"),
            Pattern.compile("operator is not unique: (.*)"),
            Pattern.compile("Failed to compile '[^']+' as a regular expression pattern"),
            Pattern.compile("Could not locate this time zone:.*")
    );
    public static final List<String> JOIN_ERRORS = List.of(
            "invalid JOIN ON clause condition. Only equi join is supported",
            "both sides of \"=\" operator in JOIN ON condition must come from different sources",
            "expression on one side of \"=\" operator in JOIN ON condition must come from exactly one source"
    );
    public static final List<String> GROUP_BY_ERRORS = List.of(
            "non-integer constant in GROUP BY"
    );
    public static final List<Pattern> GROUP_BY_REGEX_ERRORS = List.of(
            Pattern.compile("GROUP BY position (\\d+) is not in select list")
    );
    public static final List<String> ORDER_BY_ERRORS = List.of(
            "non-integer constant in ORDER BY"
    );
    public static final List<Pattern> ORDER_BY_REGEX_ERRORS = List.of(
            Pattern.compile("ORDER BY position (\\d+) is not in select list")
    );
    public static final List<String> EXPRESSION_ERRORS = List.of(
            "out of range",
            "division by zero",
            "zero raised to a negative power is undefined",
            "LIMIT must not be negative",
            "OFFSET must not be negative"
    );
    public static final List<Pattern> EXPRESSION_REGEX_ERRORS = List.of(
            Pattern.compile("operator is not unique:\\s+(.+)"),
            Pattern.compile("operator does not exist:\\s+(.+)")
    );

    public static List<String> bugErrors() {
        List<String> list = new ArrayList<>();
        if (OxlaBugs.bugOxla3376) {
            list.add("Integer literal error. Value of literal exceeds range.");
        }
        if (OxlaBugs.bugOxla8323) {
            list.add("Invalidated shared object in join processors");
        }
        if (OxlaBugs.bugOxla8330) {
            list.add("syntax error, unexpected RE_CI_MATCH");
        }
        if (OxlaBugs.bugOxla8332) {
            list.add("std::get: wrong index for variant");
        }
        return list;
    }

    public static final ExpectedErrors ALL_ERRORS = ExpectedErrors.newErrors()
            .with(OxlaCommon.SYNTAX_ERRORS)
            .withRegex(OxlaCommon.SYNTAX_REGEX_ERRORS)
            .with(OxlaCommon.JOIN_ERRORS)
            .with(OxlaCommon.GROUP_BY_ERRORS)
            .withRegex(OxlaCommon.GROUP_BY_REGEX_ERRORS)
            .with(OxlaCommon.ORDER_BY_ERRORS)
            .withRegex(OxlaCommon.ORDER_BY_REGEX_ERRORS)
            .with(OxlaCommon.EXPRESSION_ERRORS)
            .withRegex(OxlaCommon.EXPRESSION_REGEX_ERRORS)
            .with(OxlaCommon.bugErrors())
            .build();
}
