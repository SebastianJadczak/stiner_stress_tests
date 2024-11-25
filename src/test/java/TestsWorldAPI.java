import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.http.HttpDsl.http;

public class TestsWorldAPI extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://sebastianjadczak.usermd.net/world/api/")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "10"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));
    //todo: do wydzielenia dwie zmienne globalne dla wszystkich testów, po dodaniu wszystkich

    @Override
    public void before() {
        System.out.printf("Running test with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
    }


    // Get all province: endpoint: /province/
    private static ChainBuilder getAllProvince = exec(http("Get all province").get("/province/"));


    private ScenarioBuilder scn = scenario("Stiner Stress Test World")
            .exec(getAllProvince)
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
