import cinema.Main;
import com.google.gson.Gson;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.Map;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

public class CinemaTests extends SpringTest {

    private static final String ALREADY_PURCHASED_ERROR_MESSAGE = "The ticket has been already purchased!";
    private static final String OUT_OF_BOUNDS_ERROR_MESSAGE = "The number of a row or a column is out of bounds!";

    private static final Gson gson = new Gson();

    public CinemaTests() {
        super(Main.class);
    }

    private static void checkStatusCode(HttpResponse resp, int status) {
        if (resp.getStatusCode() != status) {
            throw new WrongAnswer(
                resp.getRequest().getMethod() + " " +
                    resp.getRequest().getLocalUri() +
                    " should respond with status code " + status + ", " +
                    "responded: " + resp.getStatusCode() + "\n\n" +
                    "Response body:\n\n" + resp.getContent()
            );
        }
    }

    CheckResult testEndpoint() {
        HttpResponse response = get("/seats").send();
        checkStatusCode(response, 200);
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
                            .value("price", isInteger(price -> price == 10 || price == 8))
                    )
                )
                .value("total_columns", 9)
                .value("total_rows", 9)
        );
        return CheckResult.correct();
    }

    CheckResult testPurchaseTicket() {
        HttpResponse response = post(
            "/purchase",
            gson.toJson(Map.of(
                "row", "1",
                "column", "1"
            ))
        ).send();

        checkStatusCode(response, 200);

        expect(response.getContent()).asJson()
            .check(
                isObject()
                    .value("row", 1)
                    .value("column", 1)
                    .value("price", 10)
            );
        return CheckResult.correct();
    }

    CheckResult testErrorMessageThatTicketHasBeenPurchased() {
        HttpResponse response = post(
            "/purchase",
            gson.toJson(Map.of(
                "row", "1",
                "column", "1"
            ))
        ).send();

        checkStatusCode(response, 400);

        expect(response.getContent()).asJson()
            .check(
                isObject()
                    .value("error", ALREADY_PURCHASED_ERROR_MESSAGE)
                    .anyOtherValues()
            );
        return CheckResult.correct();
    }

    CheckResult testErrorMessageThatNumbersOutOfBounds() {
        HttpResponse response = post(
            "/purchase",
            gson.toJson(Map.of(
                "row", "10",
                "column", "1"
            ))
        ).send();

        checkStatusCode(response, 400);

        expect(response.getContent()).asJson()
            .check(
                isObject()
                    .value("error", OUT_OF_BOUNDS_ERROR_MESSAGE)
                    .anyOtherValues()
            );

        response = post(
            "/purchase",
            gson.toJson(Map.of(
                "row", "1",
                "column", "10"
            ))
        ).send();

        checkStatusCode(response, 400);

        expect(response.getContent()).asJson()
            .check(
                isObject()
                    .value("error", OUT_OF_BOUNDS_ERROR_MESSAGE)
                    .anyOtherValues()
            );

        response = post(
            "/purchase",
            gson.toJson(Map.of(
                "row", "-1",
                "column", "-1"
            ))
        ).send();

        checkStatusCode(response, 400);

        expect(response.getContent()).asJson()
            .check(
                isObject()
                    .value("error", OUT_OF_BOUNDS_ERROR_MESSAGE)
                    .anyOtherValues()
            );


        return CheckResult.correct();
    }

    @DynamicTest
    DynamicTesting[] dynamicTests = new DynamicTesting[]{
        this::testEndpoint,
        this::testEndpointAvailableSeats,
        this::testPurchaseTicket,
        this::testErrorMessageThatTicketHasBeenPurchased,
        this::testErrorMessageThatNumbersOutOfBounds
    };
}