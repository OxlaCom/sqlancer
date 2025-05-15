package sqlancer.oxla;

public final class OxlaBugs {
    private OxlaBugs() {
    }

    /// See: https://oxla.atlassian.net/browse/OXLA-3376
    /// Valid INT_MIN value results in an "Integer literal error. Value of literal exceeds range." parsing error.
    public static boolean bugOxla3376 = true;

    /// See: https://oxla.atlassian.net/browse/OXLA-8323
    /// Errors caused in JOIN's WHERE condition return internal error non-deterministically.
    public static boolean bugOxla8323 = true;

    /// See: https://oxla.atlassian.net/browse/OXLA-8328
    /// Adding/Subtracting large integers to/from a date will crash Oxla in Debug builds, and fail silently in Release.
    public static boolean bugOxla8328 = true;

    /// See: https://oxla.atlassian.net/browse/OXLA-8329
    /// Oxla instantly crashes for REGEX patterns containing invalid symbols.
    public static boolean bugOxla8329 = true;

    /// See: https://oxla.atlassian.net/browse/OXLA-8330
    /// Oxla parses ~~, !~~, ~~*, !~~* operators incorrectly.
    public static boolean bugOxla8330 = true;
}
