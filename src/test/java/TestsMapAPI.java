import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class TestsMapAPI extends Simulation {

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

    // Get all points
    private static ChainBuilder getAllPoints = exec(http("Get all points").get("/points/"));

    //todo: Get all city: endpoint: /mapCenter/

    //todo: Get all country: endpoint: /country/

    //todo: Get all commercial places: endpoint: /commercial/

    //todo:: Get all news: endpoint: /news/


    //todo: have been to test the service to: /createUser/, /listUser/, /newsletter_email/. Important: API token hidden!

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
