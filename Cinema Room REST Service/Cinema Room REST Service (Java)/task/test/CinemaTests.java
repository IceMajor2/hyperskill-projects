import cinema.Main;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

public class CinemaTests extends SpringTest {

    public CinemaTests() {
        super(Main.class, 28852);
    }

    CheckResult testEndpoint() {

        HttpResponse response = get("/seats").send();

        if (response.getStatusCode() != 200) {
            return CheckResult.wrong("GET /seats should respond with " +
                "status code 200, responded: " + response.getStatusCode() + "\n\n" +
                "Response body:\n" + response.getContent());
        }

        return CheckResult.correct();
    }

    CheckResult testEndpointAvailableSeats() {
        HttpResponse response = get("/seats").send();

        expect(response.getContent()).asJson().check(
            isObject()
                .value("available_seats",
                    isArray(
                        81,
                        isObject()
                        .value("row", isInteger(i -> i >= 1 && i <= 9))
                        .value("column", isInteger(i -> i >= 1 && i <= 9))
                    )
                )
                .value("total_columns", 9)
                .value("total_rows", 9)
        );

        return CheckResult.correct();
    }

    @DynamicTest
    DynamicTesting[] dynamicTests = new DynamicTesting[]{
        this::testEndpoint,
        this::testEndpointAvailableSeats
    };
}