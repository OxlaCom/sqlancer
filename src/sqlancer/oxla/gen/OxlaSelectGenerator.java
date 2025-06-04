package sqlancer.oxla.gen;

import sqlancer.Randomly;
import sqlancer.common.query.ExpectedErrors;
import sqlancer.common.query.SQLQueryAdapter;
import sqlancer.oxla.OxlaCommon;
import sqlancer.oxla.OxlaGlobalState;
import sqlancer.oxla.OxlaToStringVisitor;
import sqlancer.oxla.ast.OxlaColumnReference;
import sqlancer.oxla.ast.OxlaExpression;
import sqlancer.oxla.ast.OxlaFunctionOperation;
import sqlancer.oxla.schema.OxlaColumn;
import sqlancer.oxla.schema.OxlaDataType;
import sqlancer.oxla.schema.OxlaTable;
import sqlancer.oxla.schema.OxlaTables;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OxlaSelectGenerator extends OxlaQueryGenerator {
    private static final List<String> errors = List.of();
    private static final List<Pattern> regexErrors = List.of();
    private static final ExpectedErrors expectedErrors = new ExpectedErrors(errors, regexErrors)
            .addAll(OxlaCommon.ALL_ERRORS);

    public OxlaSelectGenerator(OxlaGlobalState globalState) {
        super(globalState);
    }

    public static SQLQueryAdapter generate(OxlaGlobalState globalState, int depth) {
        return new OxlaSelectGenerator(globalState).getQuery(depth);
    }

    @Override
    public SQLQueryAdapter getQuery(int depth) {
        if (depth > globalState.getOptions().getMaxExpressionDepth() || Randomly.getBoolean()) {
            return new SQLQueryAdapter(simpleRule(), expectedErrors);
        }

        enum Rule {SIMPLE, UNION, INTERSECT, EXCEPT}
        final String query = switch (Randomly.fromOptions(Rule.values())) {
            case SIMPLE -> simpleRule();
            case UNION -> unionRule(depth + 1);
            case INTERSECT -> intersectRule(depth + 1);
            case EXCEPT -> exceptRule(depth + 1);
        };
        final StringBuilder queryBuilder = new StringBuilder()
                .append(query);

        // ORDER BY
        if (Randomly.getBoolean()) {
            queryBuilder.append(" ORDER BY ").append(OxlaToStringVisitor.asString(generator.generateOrderBys()));
        }

        // LIMIT
        if (Randomly.getBoolean()) {
            queryBuilder.append(" LIMIT ").append(Randomly.getNonCachedInteger());
        }

        return new SQLQueryAdapter(queryBuilder.toString(), expectedErrors);
    }

    private String simpleRule() {
        final StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT ")
                .append(/* TYPE */ Randomly.getBoolean() ? "DISTINCT " : "");

        // WHAT
        final OxlaTables randomSelectTables = globalState.getSchema().getRandomTableNonEmptyTables();
        generator.setTablesAndColumns(randomSelectTables); // TODO: Separate generator for this?
        List<OxlaExpression> what = new ArrayList<>();
        for (int index = 0; index < Randomly.smallNumber() + 1; ++index) {
            what.add(generator.generateExpression(OxlaDataType.getRandomType()));
        }
        queryBuilder.append(OxlaToStringVisitor.asString(what));

        // INTO
        if (Randomly.getBooleanWithRatherLowProbability()) {
            queryBuilder.append(" INTO ");
            // TODO
        }

        // FROM (SOURCE)
        {
            final boolean containsColumns = what.stream().anyMatch(OxlaColumnReference.class::isInstance);
            if (containsColumns || Randomly.getBoolean()) {
                queryBuilder
                        .append(" FROM ")
                        .append(randomSelectTables
                                .getColumns()
                                .stream()
                                .map(OxlaColumn::getTable)
                                .map(OxlaTable::getName)
                                .collect(Collectors.joining(", ")));
            }

            // JOIN
            if (Randomly.getBooleanWithRatherLowProbability()) {
                queryBuilder.append(OxlaToStringVisitor
                        .asString(generator
                                .getRandomJoinClauses()
                                .stream()
                                .map(OxlaExpression.class::cast)
                                .collect(Collectors.toList())));
            }
        }

        // WHERE
        if (Randomly.getBoolean()) {
            queryBuilder.append(" WHERE ")
                    .append(OxlaToStringVisitor.asString(generator.generatePredicate()));
        }

        // GROUP BY
        if (Randomly.getBoolean()) {
            final List<OxlaExpression> groupByExpressions = generator.generateExpressions(Randomly.smallNumber() + 1);
            queryBuilder.append(" GROUP BY ").append(OxlaToStringVisitor.asString(groupByExpressions));

            // HAVING
            if (Randomly.getBoolean()) {
                queryBuilder.append(" HAVING ").append(OxlaToStringVisitor.asString(generator.generatePredicate()));
            }
        }

        // WINDOWS
        final var possibleWindowFunctions = what
                .stream()
                .filter(OxlaFunctionOperation.class::isInstance)
                .map(OxlaFunctionOperation.class::cast)
                .toList();
        if (!possibleWindowFunctions.isEmpty()) {
            queryBuilder
                    .append(" WINDOW ")
                    .append(possibleWindowFunctions
                            .stream()
                            .map(ignored -> getWindowClause())
                            .collect(Collectors.joining(", ")));
        }

        return queryBuilder.toString();
    }

    private String unionRule(int depth) {
        final String firstSelectQuery = getQuery(depth).getUnterminatedQueryString();
        final String secondSelectQuery = getQuery(depth).getUnterminatedQueryString();
        boolean isUnionAll = Randomly.getBoolean();
        return String.format("(%s) UNION%s (%s)", firstSelectQuery, isUnionAll ? " ALL" : "", secondSelectQuery);
    }

    private String intersectRule(int depth) {
        final String firstSelectQuery = getQuery(depth).getUnterminatedQueryString();
        final String secondSelectQuery = getQuery(depth).getUnterminatedQueryString();
        return String.format("(%s) INTERSECT (%s)", firstSelectQuery, secondSelectQuery);
    }

    private String exceptRule(int depth) {
        final String firstSelectQuery = getQuery(depth).getUnterminatedQueryString();
        final String secondSelectQuery = getQuery(depth).getUnterminatedQueryString();
        return String.format("(%s) EXCEPT (%s)", firstSelectQuery, secondSelectQuery);
    }

    private String getWindowClause() {
        // frame_mode     := RANGE | ROWS | GROUPS
        // frame_boundary := UNBOUNDED PRECEDING | expr PRECEDING | CURRENT ROW | expr FOLLOWING | UNBOUNDED FOLLOWING
        // frame          := frame_mode frame_boundary | frame_mode BETWEEN frame_boundary AND frame_boundary
        // window_clause  := window_name AS '(' [ existing_window_name ] [ PARTITION BY expr [, ...] ] [ ORDER BY ] [ frame ] ')'

        final StringBuilder clauseBuilder = new StringBuilder()
                .append('w')
                .append(Randomly.smallNumber())
                .append(" AS ")
                .append('(');

        // EXISTING WINDOW NAME
        if (Randomly.getBooleanWithRatherLowProbability()) {
            clauseBuilder
                    .append("w")
                    .append(Randomly.smallNumber())
                    .append(' ');
        }

        // PARTITION BY
        if (Randomly.getBoolean()) {
            clauseBuilder
                    .append("PARTITION BY ")
                    .append(' ');
        }

        // ORDER BY
        if (Randomly.getBoolean()) {

        }

        // FRAME
        if (Randomly.getBoolean()) {
            enum FrameType {SIMPLE, BETWEEN}
            switch (Randomly.fromOptions(FrameType.values())) {
                case SIMPLE -> clauseBuilder
                        .append(FrameMode.getRandom().asString())
                        .append(' ')
                        .append(FrameBoundary.getRandom().asString(generator, OxlaDataType.getRandomType()));
                case BETWEEN -> clauseBuilder
                        .append(FrameMode.getRandom().asString())
                        .append(" BETWEEN ")
                        .append(FrameBoundary.getRandom().asString(generator, OxlaDataType.getRandomType()))
                        .append(" AND ")
                        .append(FrameBoundary.getRandom().asString(generator, OxlaDataType.getRandomType()));
            }
        }


        clauseBuilder.append(')');

        return clauseBuilder.toString();
    }

    enum FrameMode {
        RANGE, ROWS, GROUPS;

        public static FrameMode getRandom() {
            return Randomly.fromOptions(values());
        }

        public String asString() {
            return switch (this) {
                case RANGE -> "range";
                case ROWS -> "rows";
                case GROUPS -> "groups";
            };
        }
    }

    enum FrameBoundary {
        UNBOUNDED_PRECEDING, PRECEDING, CURRENT_ROW, FOLLOWING, UNBOUNDED_FOLLOWING;

        public static FrameBoundary getRandom() {
            return Randomly.fromOptions(values());
        }

        public String asString(OxlaExpressionGenerator generator, OxlaDataType wantReturnType) {
            return switch (this) {
                case UNBOUNDED_PRECEDING -> "UNBOUNDED PRECEDING";
                case PRECEDING -> String.format("%s PRECEDING", generator.generateExpression(wantReturnType));
                case CURRENT_ROW -> "CURRENT ROW";
                case FOLLOWING -> String.format("%s FOLLOWING", generator.generateExpression(wantReturnType));
                case UNBOUNDED_FOLLOWING -> "UNBOUNDED FOLLOWING";
            };
        }
    }
}
