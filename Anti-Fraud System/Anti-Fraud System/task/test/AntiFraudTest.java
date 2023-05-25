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
    private final String lockApi = "/api/auth/access";
    private final String roleApi = "/api/auth/role";
    List<Integer> userIdList = new ArrayList<>();

    private final String administrator = "{\n" +
            "   \"name\": \"administrator\",\n" +
            "   \"username\": \"administrator\",\n" +
            "   \"password\": \"oMoa4VvqnLxW\"\n" +
            "}";

    private final String johndoe1 = "{\n" +
            "   \"name\": \"John Doe 1\",\n" +
            "   \"username\": \"johndoe1\",\n" +
            "   \"password\": \"oMoa3VvqnLxW\"\n" +
            "}";

    private final String johndoe2 = "{\n" +
            "   \"name\": \"John Doe 2\",\n" +
            "   \"username\": \"johndoe2\",\n" +
            "   \"password\": \"oMoa5VvqnLxW\"\n" +
            "}";

    private final String johndoe3 = "{\n" +
            "   \"name\": \"John Doe 3\",\n" +
            "   \"username\": \"johndoe3\",\n" +
            "   \"password\": \"oMoa6VvqnLxW\"\n" +
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
            "  \"name\" : \"administrator\",\n" +
            "  \"username\" : \"administrator\",\n" +
            "  \"role\" : \"ADMINISTRATOR\"\n" +
            "}, {\n" +
            "  \"id\" : 2,\n" +
            "  \"name\" : \"John Doe 1\",\n" +
            "  \"username\" : \"johndoe1\",\n" +
            "  \"role\" : \"MERCHANT\"\n" +
            "}, {\n" +
            "  \"id\" : 4,\n" +
            "  \"name\" : \"John Doe 2\",\n" +
            "  \"username\" : \"johndoe2\",\n" +
            "  \"role\" : \"MERCHANT\"\n" +
            "} ]";

    private final String listAnswer2 = "[ {\n" +
            "  \"id\" : 1,\n" +
            "  \"name\" : \"administrator\",\n" +
            "  \"username\" : \"administrator\",\n" +
            "  \"role\" : \"ADMINISTRATOR\"\n" +
            "}, {\n" +
            "  \"id\" : 4,\n" +
            "  \"name\" : \"John Doe 2\",\n" +
            "  \"username\" : \"johndoe2\",\n" +
            "  \"role\" : \"MERCHANT\"\n" +
            "} ]";

    private final String listAnswer3 = "[ {\n" +
            "  \"id\" : 1,\n" +
            "  \"name\" : \"administrator\",\n" +
            "  \"username\" : \"administrator\",\n" +
            "  \"role\" : \"ADMINISTRATOR\"\n" +
            "}, {\n" +
            "  \"id\" : 4,\n" +
            "  \"name\" : \"John Doe 2\",\n" +
            "  \"username\" : \"johndoe2\",\n" +
            "  \"role\" : \"SUPPORT\"\n" +
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

    CheckResult testAddUser(String body, int status, String role, TestHint hint) {

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
                            .value("role", role)
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
                            .value("role", correctJson.get(position).getAsJsonObject().get("role").getAsString())
                            .value("username",
                                    isString(s -> s.equalsIgnoreCase(correctJson.get(position).getAsJsonObject()
                                            .get("username").getAsString()))
                            ));

        }
        return CheckResult.correct();
    }

    private CheckResult testLock(String api, String user, int status, String operation, String username, TestHint hint) {
        System.out.println(hint.toString());

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("operation", operation);

        HttpResponse response = checkResponseStatus(user, jsonBody.toString(), status, api, "PUT");
        if (response.getStatusCode() == 200) {
            expect(response.getContent()).asJson().check(
                    isObject()
                            .value("status", isString(s ->
                                    s.equalsIgnoreCase("User " + username + " " + operation + "ed!"))));
        }
        return CheckResult.correct();
    }

    private CheckResult testRole(String api, String user, int status, String role, String username, TestHint hint) {
        System.out.println(hint.toString());

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("username", username);
        jsonBody.addProperty("role", role);

        HttpResponse response = checkResponseStatus(user, jsonBody.toString(), status, api, "PUT");

        if (response.getStatusCode() == 200) {
            expect(response.getContent()).asJson().check(
                    isObject()
                            .value("id", isInteger())
                            .value("name", isString())
                            .value("role", role)
                            .value("username", isString(s -> s.equalsIgnoreCase(username))));
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
            () -> testAddUser(wronguser1, 400, "MERCHANT",
                    new TestHint(userApi, wronguser1, "In case of wrong data in request, endpoint" +
                            " must respond with BAD REQUEST  status (400).")), // 1

            () -> testAddUser(wronguser2, 400, "MERCHANT",
                    new TestHint(userApi, wronguser2, "In case of wrong data in request, endpoint" +
                            " must respond with BAD REQUEST  status (400).")), // 2

            () -> testAddUser(administrator, 201, "ADMINISTRATOR",
                    new TestHint(userApi, johndoe1, "If user successfully added, endpoint" +
                            " must respond with HTTP CREATED status (201) ")), // 3

            () -> testListUser(administrator, 200,  listAnswer1, 0,
                    new TestHint(userListApi, "", "Endpoint must respond with HTTP OK status (200)" +
                            " and body with array of objects representing the users sorted by ID in ascending order.")), // 4


            () -> testAddUser(johndoe1, 201, "MERCHANT",
                    new TestHint(userApi, johndoe1, "If user successfully added, endpoint" +
                            " must respond with HTTP CREATED status (201) ")), // 5

            () -> testListUser(administrator, 200,  listAnswer1, 1,
                    new TestHint(userListApi, "", "Endpoint must respond with HTTP OK status (200)" +
                            " and body with array of objects representing the users sorted by ID in ascending order.")), // 6

            () -> testListUser(wronguserCred1, 401,  listAnswer1, 0,
                    new TestHint(userListApi, "", "A user with incorrect credentials is not allowed")), // 7

            () -> testListUser(wronguserCred2, 401,  listAnswer1, 0,
                    new TestHint(userListApi, "", "A user with incorrect credentials is not allowed")), // 8

            () -> testTransaction(johndoe1, transactionApi, "POST", 401,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "Merchant after registration" +
                            " must be LOCKED")), // 9

            () -> testLock(lockApi, administrator, 200, "UNLOCK", "johndoe1",
                    new TestHint(lockApi, "", "A user johndoe1 must be UNLOCKED")), // 10

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "A user johndoe1 must be UNLOCKED")), // 11

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1", "ALLOWED",
                    new TestHint(transactionApi, "amount = 1", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 12

            // Testing persistence
            () -> restartApplication(), // 13

            () -> testListUser(administrator, 200,  listAnswer1, 1,
                    new TestHint(userListApi, "", "After restart user must exist")), // 14

            () -> testAddUser(johndoe1, 409, "MERCHANT",
                    new TestHint(userApi, johndoe1, "In case of an attempt to register an existing user," +
                            " endpoint must respond with HTTP CONFLICT status (409).")), // 15

            () -> testAddUser(johndoe2, 201, "MERCHANT",
                    new TestHint(userApi, johndoe2, "If user successfully added, endpoint" +
                            " must respond with HTTP CREATED status (201) ")), // 16

            () -> testListUser(administrator, 200,  listAnswer1, 2,
                    new TestHint(userListApi, "", "Endpoint must respond with HTTP OK status (200)" +
                            " and body with array of objects representing the users sorted by ID in ascending order.")), // 17

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"199", "ALLOWED",
                    new TestHint(transactionApi, "amount = 199", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 18

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"200", "ALLOWED",
                    new TestHint(transactionApi, "amount = 200", "Result validating of Transaction" +
                            " must be 'ALLOWED'")), // 19

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"201", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 201", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 20

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1499", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 1499", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 21

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1500", "MANUAL_PROCESSING",
                    new TestHint(transactionApi, "amount = 1500", "Result validating of Transaction" +
                            " must be 'MANUAL_PROCESSING'")), // 22

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"1501", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 1501", "Result validating of Transaction" +
                            " must be 'PROHIBITED'")), // 23

            () -> testTransaction(johndoe1, transactionApi, "POST", 200,"2000", "PROHIBITED",
                    new TestHint(transactionApi, "amount = 2000", "Result validating of Transaction" +
                            " must be 'PROHIBITED'")), // 24

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"-1", "Wrong request!",
                    new TestHint(transactionApi, "amount = -1", "Response status" +
                            " must be 'Bad request'")), // 25

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"0", "Wrong request!",
                    new TestHint(transactionApi, "amount = 0", "Response status" +
                            " must be 'Bad request'")), // 26

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,null, "Wrong request!",
                    new TestHint(transactionApi, "amount = null", "Response status" +
                            " must be 'Bad request'")), // 27

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"", "Wrong request!",
                    new TestHint(transactionApi, "amount = \"\"", "Response status" +
                            " must be 'Bad request'")), // 28

            () -> testTransaction(johndoe1, transactionApi, "POST", 400," ", "Wrong request!",
                    new TestHint(transactionApi, "amount = \" \"", "Response status" +
                            " must be 'Bad request'")), // 29

            () -> testTransaction(johndoe1, transactionApi, "POST", 400,"empty", "Wrong request!",
                    new TestHint(transactionApi, "empty body", "Response status" +
                            " must be 'Bad request'")), // 30

            () -> testDeleteUser(administrator,404,"somebody",
                    new TestHint(userApi, userApi + "/" + "somebody", "If a user is not found," +
                            " respond with HTTP Not Found status (404).")), // 31

            () -> testDeleteUser(administrator,200,"johndoe1",
                    new TestHint(userApi, userApi + "/" + "johndoe1", "The endpoint must delete" +
                            " the user and respond with HTTP OK status (200)")), // 32

            () -> testListUser(administrator, 200,  listAnswer2, 1,
                    new TestHint(userListApi, "", "User 'johndoe1' must be delete")), // 33

            () -> testRole(roleApi, administrator,404,"MERCHANT", "johndoe22",
                    new TestHint(roleApi, "", "If a user is not found, endpoint" +
                            " must respond with the HTTP Not Found status (404).")), // 34

            () -> testRole(roleApi, administrator,400,"USER", "johndoe2",
                    new TestHint(roleApi, "", "If a role is not found, endpoint" +
                            " must respond with the HTTP Bad Request status (400).")), // 35

            () -> testRole(roleApi, administrator,400,"ADMINISTRATOR", "johndoe2",
                    new TestHint(roleApi, "", "If a role is ADMINISTRATOR, endpoint" +
                            " must respond with the HTTP Bad Request status (400).")), // 36

            () -> testRole(roleApi, administrator,200,"SUPPORT", "johndoe2",
                    new TestHint(roleApi, "", "If a role successfully changed," +
                            " respond with the HTTP OK status (200)")), // 37

            () -> testRole(roleApi, administrator,409,"SUPPORT", "johndoe2",
                    new TestHint(roleApi, "", "If a role already assigned to a user," +
                            " endpoint must respond with the HTTP Conflict status (409).")), // 38

            () -> testLock(lockApi, administrator, 200, "UNLOCK", "johndoe2",
                    new TestHint(lockApi, "", "A user johndoe2 must be UNLOCKED")), // 39

            () -> testListUser(johndoe2, 200,  listAnswer3, 1,
                    new TestHint(userListApi, "", "Role for user 'johndoe2" +
                            "' must be changed to SUPPORT")), // 40

            () -> testTransaction(johndoe2, transactionApi, "POST", 403,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "Role MERCHANT must be removed from" +
                            " user johndoe2!")), // 41

            // test Locking
            () -> testAddUser(johndoe3, 201, "MERCHANT",
                    new TestHint(userApi, johndoe3, "If user successfully added, endpoint" +
                            " must respond with HTTP CREATED status (201) ")), // 42

            () -> testTransaction(johndoe3, transactionApi, "POST", 401,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "MERCHANT user after registration" +
                            " must be LOCKED")), // 43

            () -> testLock(lockApi, administrator, 200, "UNLOCK", "johndoe3",
                    new TestHint(lockApi, "", "A user johndoe3 must be UNLOCKED")), // 44

            () -> testTransaction(johndoe3, transactionApi, "POST", 200,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "A user johndoe3 must be UNLOCKED")), // 45

            () -> testLock(lockApi, administrator, 200, "LOCK", "johndoe3",
                    new TestHint(lockApi, "", "A user johndoe3 must be LOCKED")), // 46

            () -> testTransaction(johndoe3, transactionApi, "POST", 401,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "A user johndoe3 must be LOCKED")), // 47

            () -> testLock(lockApi, administrator, 200, "UNLOCK", "johndoe3",
                    new TestHint(lockApi, "", "A user johndoe3 must be UNLOCKED")), // 48

            // test role model
            () -> testTransaction(administrator, transactionApi, "POST", 403,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "A user with role ADMINISTRATOR" +
                            " must not have access to " + transactionApi)), // 49

            () -> testTransaction(administrator, transactionApi + "/", "POST", 403,"1", "ALLOWED",
                    new TestHint(transactionApi + "/", "", "A user with role ADMINISTRATOR" +
                            " must not have access to " + transactionApi + "/")), // 50

            () -> testTransaction(johndoe2, transactionApi, "POST", 403,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "A user with role SUPPORT" +
                            " must not have access to " + transactionApi)), // 51

            () -> testTransaction(johndoe2, transactionApi + "/", "POST", 403,"1", "ALLOWED",
                    new TestHint(transactionApi + "/", "", "A user with role SUPPORT" +
                            " must not have access to " + transactionApi + "/")), // 52

            () -> testTransaction(null, transactionApi, "POST", 401,"1", "ALLOWED",
                    new TestHint(transactionApi, "", "A user with role ANONYMOUS" +
                            " must not have access to " + transactionApi)), // 53

            () -> testDeleteUser(null,401,"johndoe1",
                    new TestHint(userApi, "", "A user with role ANONYMOUS" +
                            " must not have access to DELETE " + userApi)), // 54

            () -> testDeleteUser(johndoe2,403,"johndoe1",
                    new TestHint(userApi, "", "A user with role SUPPORT" +
                            " must not have access to DELETE " + userApi)), // 55

            () -> testDeleteUser(johndoe3,403,"johndoe1",
                    new TestHint(userApi, "", "A user with role MERCHANT" +
                            " must not have access to DELETE " + userApi)), // 56

            () -> testDeleteUser(johndoe3,403,"",
                    new TestHint(userApi, "", "A user with role MERCHANT" +
                            " must not have access to DELETE " + userApi)), // 57

            () -> testListUser(null, 401,  listAnswer3, 1,
                    new TestHint(userListApi, "", "A user with role ANONYMOUS" +
                            " must not have access to " + userListApi)), // 58

            () -> testListUser(johndoe3, 403,  listAnswer3, 1,
                    new TestHint(userListApi, "", "A user with role MERCHANT" +
                            " must not have access to " + userListApi)), // 59

            () -> testLock(lockApi, johndoe3, 403, "UNLOCK", "johndoe2",
                    new TestHint(lockApi, "", "A user with role MERCHANT" +
                            " must not have access to "  + lockApi)), // 60

            () -> testLock(lockApi, johndoe2, 403, "UNLOCK", "johndoe2",
                    new TestHint(lockApi, "", "A user with role SUPPORT" +
                            " must not have access to "  + lockApi)), // 61

            () -> testLock(lockApi + "/", johndoe3, 403, "UNLOCK", "johndoe2",
                    new TestHint(lockApi + "/", "", "A user with role MERCHANT" +
                            " must not have access to " + lockApi + "/")), // 62

            () -> testLock(lockApi + "/", johndoe2, 403, "UNLOCK", "johndoe2",
                    new TestHint(lockApi + "/", "", "A user with role SUPPORT" +
                            " must not have access to " + lockApi + "/")), // 63

            () -> testLock(lockApi, null, 401, "UNLOCK", "johndoe2",
                    new TestHint(lockApi, "", "A user with role ANONYMOUS" +
                            " must not have access to " + lockApi)), // 64

            () -> testRole(roleApi, johndoe2,403,"SUPPORT", "johndoe2",
                    new TestHint(roleApi, "", "A user with role SUPPORT" +
                            " must not have access to " + roleApi)), // 65

            () -> testRole(roleApi, johndoe3,403,"SUPPORT", "johndoe2",
                    new TestHint(roleApi, "", "A user with role MERCHANT" +
                            " must not have access to " + roleApi)), // 66

            () -> testRole(roleApi, null,401,"SUPPORT", "johndoe2",
                    new TestHint(roleApi, "", "A user with role ANONYMOUS" +
                            " must not have access to " + roleApi)), // 67

            () -> testRole(roleApi + "/", johndoe2,403,"SUPPORT", "johndoe2",
                    new TestHint(roleApi + "/", "", "A user with role SUPPORT" +
                            " must not have access to " + roleApi + "/")), // 68

            () -> testRole(roleApi + "/", johndoe3,403,"SUPPORT", "johndoe2",
                    new TestHint(roleApi + "/", "", "A user with role MERCHANT" +
                            " must not have access to " + roleApi + "/")), // 69

    };
}