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

    // Get all points: endpoint: /points/
    private static ChainBuilder getAllPoints = exec(http("Get all points").get("/points/"));

    //Get all city: endpoint: /mapCenter/
    private static ChainBuilder getCites = exec(http("Get cities").get("/mapCenter/"));

    //Get all country: endpoint: /country/
    private static ChainBuilder getCountries = exec(http("Get countries").get("/country/"));

    //Get all commercial places: endpoint: /commercial/
    private static ChainBuilder getCommercial = exec(http("Get commercial").get("/commercial/"));

    //Get all news: endpoint: /news/
    private static ChainBuilder getNewses = exec(http("Get newses").get("/news/"));


    //todo: have been to test the service to: /createUser/, /listUser/, /newsletter_email/. Important: API token hidden!

    private ScenarioBuilder scn = scenario("Stiner Stress Test")
            .exec(getAllPoints)
            .pause(2)
            .exec(getCites)
            .pause(2)
            .exec(getCountries)
            .pause(2)
            .exec(getCommercial)
            .pause(2)
            .exec(getNewses);

    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                )
        ).protocols(httpProtocol);
    }

}
