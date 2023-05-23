import antifraud.AntiFraudApplication;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.ArrayList;
import java.util.List;

import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

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

    private final String transactionApi = "/api/antifraud/transaction";
    private final String userApi = "/api/auth/user";
    private final String userListApi = "/api/auth/list";
    List<Integer> userIdList = new ArrayList<>();

    private final String johndoe1 = "{\n" +
            "   \"name\": \"John Doe 1\",\n" +
            "   \"username\": \"johndoe1\",\n" +
            "   \"password\": \"oMoa3VvqnLxW\"\n" +
            "}";

    private final String johndoe1Upper = "{\n" +
            "   \"name\": \"John Doe 1\",\n" +
            "   \"username\": \"JohnDoe1\",\n" +
            "   \"password\": \"oMoa3VvqnLxW\"\n" +
            "}";

    private final String johndoe2 = "{\n" +
            "   \"name\": \"John Doe 2\",\n" +
            "   \"username\": \"johndoe2\",\n" +
            "   \"password\": \"oMoa3VvqnLxW\"\n" +
            "}";

    private final String johndoe2Upper = "{\n" +
            "   \"name\": \"John Doe 2\",\n" +
            "   \"username\": \"Johnddoe2\",\n" +
            "   \"password\": \"oMoa3VvqnLxW\"\n" +
            "}";

    private final String wronguser1 = "{\n" +
            "   \"name\": \"John Doe 1\",\n" +
            "   \"password\": \"oa3VvqnLxW\"\n" +
            "}";

    private final String wronguser2 = "{\n" +
            "   \"name\": \"John Doe 2\",\n" +
            "   \"username\": \"johndoe1\"\n" +
            "}";

    private final String wronguserCred1 = "{\n" +
            "   \"name\": \"John Doe 1\",\n" +
            "   \"username\": \"johndoe1\",\n" +
            "   \"password\": \"oa3VvqnLxW\"\n" +
            "}";

    private final String wronguserCred2 = "{\n" +
            "   \"name\": \"John Do\",\n" +
            "   \"username\": \"johndo\",\n" +
            "   \"password\": \"oMoa3VvqnLxW\"\n" +
            "}";

    private final String listAnswer1 = "[ {\n" +
            "  \"id\" : 1,\n" +
            "  \"name\" : \"John Doe 1\",\n" +
            "  \"username\" : \"johndoe1\"\n" +
            "}, {\n" +
            "  \"id\" : 3,\n" +
            "  \"name\" : \"John Doe 2\",\n" +
            "  \"username\" : \"johndoe2\"\n" +
            "} ]";

    private final String listAnswer2 = "[ {\n" +
            "  \"id\" : 3,\n" +
            "  \"name\" : \"John Doe 2\",\n" +
            "  \"username\" : \"johndoe2\"\n" +
            "} ]";

    public AntiFraudTest() {
        super(AntiFraudApplication.class, "../service_db.mv.db");
    }

    /**
     * Method for restarting application
     *
     */
    private CheckResult restartApplication() {
        try {
            reloadSpring();
        } catch (Exception ex) {
            throw new UnexpectedError(ex.getMessage());
        }
        return CheckResult.correct();
    }

    private HttpResponse checkResponseStatus(String user, String body,
                                             int status, String api, String method) {
        HttpRequest request = null;
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

        if (user != null) {
            JsonObject userJson = getJson(user).getAsJsonObject();
            String password = userJson.get("password").getAsString();
            String login = userJson.get("username").getAsString().toLowerCase();
            request = request.basicAuth(login, password);
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

    CheckResult testAddUser(String body, int status, TestHint hint) {

        System.out.println(hint.toString());

        HttpResponse response = checkResponseStatus(null, body, status, userApi, "POST");

        if (response.getStatusCode() == 201) {

            JsonObject rightResponse = getJson(body).getAsJsonObject();
            rightResponse.remove("password");

            // Check is it JSON in response or something else
            if (!response.getJson().isJsonObject()) {
                return CheckResult.wrong("Wrong object in response, expected JSON but was \n" +
                        response.getContent().getClass());

            }

            JsonObject jsonResponse = response.getJson().getAsJsonObject();

            // Check if password is presence in response
            if (jsonResponse.get("password") != null) {
                return CheckResult.wrong("You must remove password from response\n" +
                        jsonResponse);
            }

            if (jsonResponse.get("id") == null) {
                return CheckResult.wrong("Response must contain user ID\n" +
                        "Received response:\n" +
                        jsonResponse);
            }

            if (jsonResponse.get("id").isJsonNull()) {
                return CheckResult.wrong("Response must contain user ID\n" +
                        "Received response:\n" +
                        jsonResponse);
            }

            if (userIdList.contains(jsonResponse.get("id").getAsInt())) {
                return CheckResult.wrong("User ID must be unique!\n" +
                        "Received response:\n" +
                        jsonResponse);
            }

            rightResponse.addProperty("id", jsonResponse.get("id").toString());
            // Check JSON in response
            expect(response.getContent()).asJson().check(
                    isObject()
                            .value("id", isInteger())
                            .value("name", rightResponse.get("name").getAsString())
                            .value("username", isString(s -> s.equalsIgnoreCase(rightResponse.get("username").getAsString())))
            );
            userIdList.add(jsonResponse.get("id").getAsInt());
        }
        return CheckResult.correct();
    }

    CheckResult testDeleteUser(String body, int status, String user, TestHint hint) {

        System.out.println(hint.toString());

        HttpResponse response = checkResponseStatus(body, "", status, userApi + "/" + user, "DELETE");

        if (response.getStatusCode() == 200) {

            // Check is it JSON in response or something else
            if (!response.getJson().isJsonObject()) {
                return CheckResult.wrong("Wrong object in response, expected JSON but was \n" +
                        response.getContent().getClass());

            }

            JsonObject jsonResponse = response.getJson().getAsJsonObject();

            // Check JSON in response
            expect(response.getContent()).asJson().check(
                    isObject()
                            .value("status", "Deleted successfully!")
                            .value("username", isString(s -> s.equalsIgnoreCase(user)))
            );
        }
        return CheckResult.correct();
    }

    private CheckResult testListUser(String user, int status, String answer,
                                     int position, TestHint hint) {

        System.out.println(hint.toString());

        HttpResponse response = checkResponseStatus(user, "", status, userListApi, "GET");

        if (response.getStatusCode() == 200) {
            // Check is it array of JSON in response or something else
            if (!response.getJson().isJsonArray()) {
                return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
                        response.getContent().getClass());

            }

            JsonArray correctJson = getJson(answer).getAsJsonArray();
            JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();
            if (responseJson.size() == 0) {
                throw new WrongAnswer("Empty array in response!");
            }

            if (responseJson.size() != position + 1) {
                throw new WrongAnswer("Incorrect number - " +  responseJson.size() +
                        " users in response, must be - " + (position + 1));
            }

            // Check JSON in response
            expect(responseJson.get(position).toString()).asJson().check(
                    isObject()
                            .value("id", isInteger())
                            .value("name", correctJson.get(position).getAsJsonObject().get("name").getAsString())
                            .value("username",
                                    isString(s -> s.equalsIgnoreCase(correctJson.get(position).getAsJsonObject()
                                            .get("username").getAsString()))
                            ));

        }
        return CheckResult.correct();
    }

    private CheckResult testTransaction(String user, String api, String method,
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

        HttpResponse response = checkResponseStatus(user, jsonBody.toString(), status, api, method);

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
            () -> testAddUser(wronguser1, 400,
                    new TestHint(userApi, wronguser1, "In case of wrong data in request, endpoint" +
                            " must respond with BAD REQUEST  status (400).")), // 1

            () -> testAddUser(wronguser2, 400,
                    new TestHint(userApi, wronguser2, "In case of wrong data in request, endpoint" +
                            " must respond with BAD REQUEST  status (400).")), // 2

            () -> testAddUser(johndoe1, 201,
                    new TestHint(userApi, johndoe1, "If user successfully added, endpoint" +
                            " must respond with HTTP CREATED status (201) ")), // 3

            () -> testListUser(johndoe1, 200,  listAnswer1, 0,
                    new TestHint(userListApi, "", "Endpoint must respond with HTTP OK status (200)" +
                            " and body with array of objects representing the users sorted by ID in ascending order.")), // 4

            () -> testListUser(wronguserCred1, 401,  listAnswer1, 0,
                    new TestHint(userListApi, "", "A user with incorrect credentials is not allowed")), // 5

            () -> testListUser(wronguserCred2, 401,  listAnswer1, 0,
                    new TestHint(userListApi, "", "A user with incorrect credentials is not allowed")), // 6

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1", "ALLOWED",
                    new TestHint(transactionApi, "amount = 1", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 7

            // Testing persistence
            () -> restartApplication(), // 8

            () -> testListUser(johndoe1, 200,  listAnswer1, 0,
                    new TestHint(userListApi, "", "After restart user must exist")), // 9

            () -> testAddUser(johndoe1, 409,
                    new TestHint(userApi, johndoe1, "In case of an attempt to register an existing user," +
                            " endpoint must respond with HTTP CONFLICT status (409).")), // 10

            () -> testAddUser(johndoe2, 201,
                    new TestHint(userApi, johndoe2, "If user successfully added, endpoint" +
                            " must respond with HTTP CREATED status (201) ")), // 11

            () -> testListUser(johndoe1, 200,  listAnswer1, 1,
                    new TestHint(userListApi, "", "Endpoint must respond with HTTP OK status (200)" +
                            " and body with array of objects representing the users sorted by ID in ascending order.")), // 12

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"199", "ALLOWED",
                    new TestHint(transactionApi, "amount = 199", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 13

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"200", "ALLOWED",
                    new TestHint(transactionApi, "amount = 200", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 14

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"201", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 201", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 15

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1499", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 1499", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 16

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1500", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 1500", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 17

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1501", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 1501", "Result validating of Transaction" +
                            " must be 'PROHIBITED'")), // 18

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"2000", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 2000", "Result validating of Transaction" +
                            " must be 'PROHIBITED'")), // 19

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"-1", "Wrong request!",
                    new TestHint(transactionApi, "amount = -1", "Response status" +
                            " must be 'Bad request'")), // 20

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"0", "Wrong request!",
                    new TestHint(transactionApi, "amount = 0", "Response status" +
                            " must be 'Bad request'")), // 21

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,null, "Wrong request!",
                    new TestHint(transactionApi, "amount = null", "Response status" +
                            " must be 'Bad request'")), // 22

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"", "Wrong request!",
                    new TestHint(transactionApi, "amount = \"\"", "Response status" +
                            " must be 'Bad request'")), // 23

            () -> testTransaction(johndoe1, transactionApi, "POST", 400," ", "Wrong request!",
                    new TestHint(transactionApi, "amount = \" \"", "Response status" +
                            " must be 'Bad request'")), // 24

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"empty", "Wrong request!",
                    new TestHint(transactionApi, "empty body", "Response status" +
                            " must be 'Bad request'")), // 25

            () -> testDeleteUser(johndoe1,404,"somebody",
                    new TestHint(userApi, userApi + "/" + "somebody", "If a user is not found," +
                            " respond with HTTP Not Found status (404).")), // 26

            () -> testTransaction(johndoe1Upper, transactionApi, "POST", 200,"2000", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 2000", "Usernames must be case insensitive.")), // 27

            () -> testDeleteUser(johndoe2,200,"johndoe1",
                    new TestHint(userApi, userApi + "/" + "johndoe1", "The endpoint must delete" +
                            " the user and respond with HTTP OK status (200)")), // 28

            () -> testListUser(johndoe2, 200,  listAnswer2, 0,
                    new TestHint(userListApi, "", "User 'johndoe1' must be delete")), // 29

            () -> testListUser(johndoe2, 200,  listAnswer2, 0,
                    new TestHint(userListApi, "", "Usernames must be case insensitive.")), // 30

    };
}