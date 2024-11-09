import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class Tests extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://sebastianjadczak.usermd.net/map/api/")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "10"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));

    @Override
    public void before() {
        System.out.printf("Running test with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
    }

    private static ChainBuilder getAllPoints = exec(http("Get all points").get("/points/"));


    private ScenarioBuilder scn = scenario("Stiner Stress Test")
            .exec(getAllPoints)
            .pause(2);

    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                )
        ).protocols(httpProtocol);
    }

}
