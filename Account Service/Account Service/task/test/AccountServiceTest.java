import account.AccountServiceApplication;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.common.JsonUtils.getPrettyJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;


class User {
    private String name;
    private String lastname;
    private String email;
    private String password;

    public User(User another) {
        this(another.name, another.lastname, another.email, another.password);
    }

    public User(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}

public class AccountServiceTest extends SpringTest {

    private  final String signUpApi = "/api/auth/signup/";
    List<String> deniedSignUpMethods = new ArrayList<String>() {{
        add("get");
        add("put");
        add("delete");
    }};

    private final User johnDoe = new User("John", "Doe", "johndoe@acme.com", "secret");
    private final String johnDoeCorrectUser = johnDoe.toJson();
    private final String johnDoeEmptyName = new User(johnDoe).setName("").toJson();
    private final String johnDoeNoName = new User(johnDoe).setName(null).toJson();
    private final String johnDoeEmptyLastName = new User(johnDoe).setLastname("").toJson();
    private final String johnDoeNoLastName = new User(johnDoe).setLastname(null).toJson();
    private final String johnDoeEmptyEmail = new User(johnDoe).setEmail("").toJson();
    private final String johnDoeNoEmail = new User(johnDoe).setLastname(null).toJson();
    private final String johnDoeEmptyPassword = new User(johnDoe).setPassword("").toJson();
    private final String johnDoeNoPassword = new User(johnDoe).setPassword(null).toJson();
    private final String johnDoeWrongEmail1 = new User(johnDoe).setEmail("johndoeacme.com").toJson();
    private final String johnDoeWrongEmail2 = new User(johnDoe).setEmail("johndoe@google.com").toJson();

    public AccountServiceTest() {
        super(AccountServiceApplication.class, 28852);
    }

    /**
     * Method for checking status code of response Post request for API
     *
     * @param api testing api (String)
     * @param body string representation of body content in JSON format (String)
     * @param status required http status for response (int)
     * @return instance of CheckResult class containing result of checks (CheckResult)
     */
    CheckResult testPostApi(String api, String body, int status) {
        HttpResponse response = post(api, body).send();

        if (response.getStatusCode() != status) {
            return CheckResult.wrong("POST " + api + " should respond with " +
                    "status code " + status + ", responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent() + "\n" +
                    "Request body:\n" + body);
        }
        return CheckResult.correct();
    }

    /**
     * Method for checking response on Post request for signup API
     *
     * @param body string representation of body content in JSON format (String)
     * @param status required http status for response (int)
     * @return instance of CheckResult class containing result of checks (CheckResult)
     */
    CheckResult testPostSignUpResponse(String body, int status) {
        HttpResponse response = post(signUpApi, body).send();
        testPostApi(signUpApi, body, status);

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
                    getPrettyJson(jsonResponse));
        }

        // Check JSON in response
        expect(response.getContent()).asJson().check(
                isObject()
                        .value("name", rightResponse.get("name").getAsString())
                        .value("lastname", rightResponse.get("lastname").getAsString())
                        .value("email", rightResponse.get("email").getAsString().toLowerCase()));

        return CheckResult.correct();
    }

    /**
     * Method for check the prohibition of requests specified types
     *
     * @param api testing api (String)
     * @param body string representation of body content in JSON format (String)
     * @param deniedMethods list of prohibited type requests
     * @return instance of CheckResult class containing result of checks (CheckResult)
     */
    CheckResult testDeniedMethods(String api, List<String> deniedMethods, String body) {

        HttpRequest getReq = get(api);
        HttpRequest postReq = post(api, body);
        HttpRequest putReq = put(api, body);
        HttpRequest deleteReq = delete(api);

        Map<String, HttpRequest> methodsMap = new LinkedHashMap<String,  HttpRequest>() {{
            put("get", getReq);
            put("post", postReq);
            put("put", putReq);
            put("delete", deleteReq);
        }};

        for (Map.Entry<String, HttpRequest> entry : methodsMap.entrySet()) {
            if (deniedMethods.contains(entry.getKey())) {
                HttpResponse response = entry.getValue().send();
                if (response.getStatusCode() != 405) {
                    return CheckResult.wrong("Method " + entry.getKey().toUpperCase() + " is not allowed for " + api + " status code should be " +
                            "405, responded: " + response.getStatusCode());
                }
            }
        }
        return CheckResult.correct();
    }

    @DynamicTest
    DynamicTesting[] dt = new DynamicTesting[]{
            // Test POST request for signup api
            () -> testPostApi(signUpApi, johnDoeCorrectUser, 200),
            () -> testPostApi(signUpApi, johnDoeEmptyName, 400),
            () -> testPostApi(signUpApi, johnDoeNoName, 400),
            () -> testPostApi(signUpApi, johnDoeEmptyLastName, 400),
            () -> testPostApi(signUpApi, johnDoeNoLastName, 400),
            () -> testPostApi(signUpApi, johnDoeEmptyEmail, 400),
            () -> testPostApi(signUpApi, johnDoeNoEmail, 400),
            () -> testPostApi(signUpApi, johnDoeEmptyPassword, 400),
            () -> testPostApi(signUpApi, johnDoeNoPassword, 400),
            () -> testPostApi(signUpApi, johnDoeWrongEmail1, 400),
            () -> testPostApi(signUpApi, johnDoeWrongEmail2, 400),

            // Test allowed methods
            () -> testDeniedMethods(signUpApi, deniedSignUpMethods, johnDoeCorrectUser),

            // Test response for signup api
            () -> testPostSignUpResponse(johnDoeCorrectUser, 200)
    };
}