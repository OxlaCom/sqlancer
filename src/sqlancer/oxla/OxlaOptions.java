package sqlancer.oxla;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import sqlancer.DBMSSpecificOptions;

import java.util.List;

@Parameters(separators = "=", commandDescription = "Oxla (default port: " + OxlaOptions.DEFAULT_PORT
        + ", default host: " + OxlaOptions.DEFAULT_HOST + ")")
public class OxlaOptions implements DBMSSpecificOptions<OxlaOracleFactory> {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 5432;

    @Parameter(names = { "--max-tables-count" }, description = "The maximum number of tables that can be created.")
    public int maxTableCount = 10;

    @Parameter(names = "--oracle", description = "Specifies which test oracle should be used with Oxla")
    public List<OxlaOracleFactory> oracle = List.of(OxlaOracleFactory.FUZZER, OxlaOracleFactory.PQS, OxlaOracleFactory.NOREC);

    @Parameter(names = "--connection-url", description = "Specifies the URL for connecting to the Oxla server", arity = 1)
    public String connectionURL = String.format("postgresql://%s:%d/oxla", OxlaOptions.DEFAULT_HOST, OxlaOptions.DEFAULT_PORT);

    @Override
    public List<OxlaOracleFactory> getTestOracleFactory() {
        return oracle;
    }
}
