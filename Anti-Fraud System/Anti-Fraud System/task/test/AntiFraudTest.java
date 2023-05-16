import antifraud.AntiFraudApplication;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;

class TestHint {
    private final String apiPath;
    private final String requestBody;
    private final String message;

    public TestHint(String apiPath, String requestBody, String message) {
        this.apiPath = apiPath;
        this.requestBody = requestBody;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Test case\n" +
                "Testing api: '" + apiPath + '\'' + "\n" +
                (requestBody.length() > 0 ? "request: '" + requestBody + '\'' + "\n" : "") +
                "Expectations: '" + message + "'" + "\n" +
                "-----";
    }
}

public class AntiFraudTest extends SpringTest {

    private String transactionApi = "/api/antifraud/transaction";

    public AntiFraudTest() {
        super(AntiFraudApplication.class, 28852);
    }

    private HttpResponse checkResponseStatus(String body, int status, String api, String method) {
        HttpRequest request = get(api);
        switch (method) {
            case "GET":
                request = get(api);
                break;
            case "POST":
                request = post(api, body);
                break;
            case "PUT":
                request = put(api, body);
                break;
            case "DELETE":
                request = delete(api);
                break;
        }

        HttpResponse response = request.send();

        if (response.getStatusCode() != status) {
            throw new WrongAnswer(method + " " + api  + " should respond with "
                    + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
                    + "Response body:\n" + response.getContent() + "\n");
        }
        return response;
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            long l = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private CheckResult testApi(String api, String method,
                                int status, String amount, String answer, TestHint hint) {

        System.out.println(hint.toString());

        JsonObject jsonBody = new JsonObject();
        if (isNumeric(amount)) {
            jsonBody.addProperty("amount", Long.parseLong(amount));
        } else if (amount == null) {
            jsonBody.addProperty("amount", (Boolean) null);
        } else if (!amount.equals("empty")) {
            jsonBody.addProperty("amount", amount);
        }

        HttpResponse response = checkResponseStatus(jsonBody.toString(), status, api, method);

        // Check JSON in response
        if (response.getStatusCode() == 200) {
            expect(response.getContent()).asJson().check(
                    isObject()
                            .value("result", answer));
        }
        return CheckResult.correct();
    }

    @DynamicTest
    DynamicTesting[] dt = new DynamicTesting[]{
            // Test POST request for signup api
            () -> testApi(transactionApi, "POST", 200,"1", "ALLOWED",
                    new TestHint(transactionApi, "amount = 1", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 1

            () -> testApi(transactionApi, "POST", 200,"199", "ALLOWED",
                    new TestHint(transactionApi, "amount = 199", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 2

            () -> testApi(transactionApi, "POST", 200,"200", "ALLOWED",
                    new TestHint(transactionApi, "amount = 200", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 3

            () -> testApi(transactionApi, "POST", 200,"201", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 201", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 4

            () -> testApi(transactionApi, "POST", 200,"1499", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 1499", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 5

            () -> testApi(transactionApi, "POST", 200,"1500", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 1500", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 6

            () -> testApi(transactionApi, "POST", 200,"1501", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 1501", "Result validating of Transaction" +
                            " must be 'PROHIBITED'")), // 7

            () -> testApi(transactionApi, "POST", 200,"2000", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 2000", "Result validating of Transaction" +
                            " must be 'PROHIBITED'")), // 8

            () -> testApi(transactionApi, "POST", 400,"-1", "Wrong request!",
                    new TestHint(transactionApi, "amount = -1", "Response status" +
                            " must be 'Bad request'")), // 9

            () -> testApi(transactionApi, "POST", 400,"0", "Wrong request!",
                    new TestHint(transactionApi, "amount = 0", "Response status" +
                            " must be 'Bad request'")), // 10

            () -> testApi(transactionApi, "POST", 400,null, "Wrong request!",
                    new TestHint(transactionApi, "amount = null", "Response status" +
                            " must be 'Bad request'")), // 11

            () -> testApi(transactionApi, "POST", 400,"", "Wrong request!",
                    new TestHint(transactionApi, "amount = \"\"", "Response status" +
                            " must be 'Bad request'")), // 12

            () -> testApi(transactionApi, "POST", 400," ", "Wrong request!",
                    new TestHint(transactionApi, "amount = \" \"", "Response status" +
                            " must be 'Bad request'")), // 13

            () -> testApi(transactionApi, "POST", 400,"empty", "Wrong request!",
                    new TestHint(transactionApi, "empty body", "Response status" +
                            " must be 'Bad request'")), // 14

    };
}