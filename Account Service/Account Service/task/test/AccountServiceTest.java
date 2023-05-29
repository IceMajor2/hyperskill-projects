import account.AccountServiceApplication;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import java.util.*;
import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.common.JsonUtils.getPrettyJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

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

  private  final String signUpApi = "/api/auth/signup";
  private  final String paymentApi = "/api/empl/payment";
  private final User johnDoe = new User("John", "Doe", "JohnDoe@acme.com", "secret");
  private final User maxMustermann = new User("Max", "Mustermann", "MaxMustermann@acme.com", "secret");
  private final User captainNemo = new User("Captain", "Nemo", "nautilus@pompilius.com", "wings");
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
  private final String maxMustermannCorrectUser = maxMustermann.toJson();
  private final String johnDoeCorrectUserLower = new User(johnDoe).setEmail(johnDoe.getEmail().toLowerCase()).toJson();
  private final String maxMustermannCorrectUserLower = new User(maxMustermann).setEmail(maxMustermann.getEmail().toLowerCase()).toJson();
  private final String johnDoeWrongPassword = new User(johnDoe).setPassword("none").toJson();
  private final String johnDoeWrongPasswordCaseSensitive = new User(johnDoe).setPassword(johnDoe.getPassword().toUpperCase()).toJson();
  private final String maxMustermannWrongPassword = new User(maxMustermann).setPassword("none").toJson();
  private final String captainNemoWrongUser = captainNemo.toJson();

  List<Integer> userIdList = new ArrayList<>();

  public AccountServiceTest() {
    super(AccountServiceApplication.class, "../service_db.mv.db");
  }

  /**
   * Method for checking status code of response Post request for API
   *
   * @param api testing api (String)
   * @param body string representation of body content in JSON format (String)
   * @param status required http status for response (int)
   * @return instance of CheckResult class containing result of checks (CheckResult)
   */
  CheckResult testPostApi(String api, String body, int status, String message) {
    HttpResponse response = post(api, body).send();

    if (response.getStatusCode() != status) {
      return CheckResult.wrong("POST " + api + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + message + "\n"
              + "Response body:\n" + response.getContent() + "\n"
              + "Request body:\n" + body);
    }
    return CheckResult.correct();
  }

  /**
   * Method for checking status code of response Get request for API
   *
   * @param api testing api (String)
   * @param status required http status for response (int)
   * @param message hint about reason of error (String)
   * @return instance of CheckResult class containing result of checks (CheckResult)
   */
  CheckResult testGetApi(String api, int status, String message) {
    HttpResponse response = get(api).send();

    if (response.getStatusCode() != status) {
      return CheckResult.wrong("GET " + api + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + message);
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
    testPostApi(signUpApi, body, status, "API must be available");

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


    // Check JSON in response
    expect(response.getContent()).asJson().check(
            isObject()
                    .value("id", isInteger())
                    .value("name", rightResponse.get("name").getAsString())
                    .value("lastname", rightResponse.get("lastname").getAsString())
                    .value("email", isString(s -> s.equalsIgnoreCase(rightResponse.get("email").getAsString()))));

    if (userIdList.contains(jsonResponse.get("id").getAsInt())) {
      return CheckResult.wrong("User ID must be unique!\n" +
              "Received response:\n" +
              jsonResponse);
    }

    userIdList.add(jsonResponse.get("id").getAsInt());
    return CheckResult.correct();
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

  /**
   * Method for checking authentication
   *
   * @param user string representation of user information in JSON format (String)
   * @param status required http status for response (int)
   * @param message hint about reason of error (String)
   * @return instance of CheckResult class containing result of checks (CheckResult)
   */
  private CheckResult testUserRegistration(String user, int status, String message) {
    JsonObject userJson = getJson(user).getAsJsonObject();
    String password = userJson.get("password").getAsString();
    String login = userJson.get("email").getAsString();
    HttpResponse response = get(paymentApi).basicAuth(login, password).send();
    if (response.getStatusCode() != status) {
      return CheckResult.wrong("Get " + paymentApi + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + message + "\n"
              + "Authentication with " + login + " / " + password);
    }
    // Check JSON in response
    if (status == 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("id", isInteger())
                      .value("name", userJson.get("name").getAsString())
                      .value("lastname", userJson.get("lastname").getAsString())
                      .value("email", isString(s -> s.equalsIgnoreCase(userJson.get("email").getAsString()))));
    }
    return CheckResult.correct();
  }

  /**
   * Method for testing duplicate users
   *
   * @param user string representation of user information in JSON format (String)
   * @return instance of CheckResult class containing result of checks (CheckResult)
   */
  private CheckResult testUserDuplicates(String user) {
    HttpResponse response = post(signUpApi, user).send();
    // Check error message field in JSON response
    expect(response.getContent()).asJson().check(
            isObject()
                    .value("status", 400)
                    .value("error", "Bad Request")
                    .value("message", "User exist!")
                    .anyOtherValues());
    return CheckResult.correct();
  }

  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[] {

          // Test user registration on signup api
          () -> testPostSignUpResponse(johnDoeCorrectUser, 200),
          () -> testPostApi(signUpApi, johnDoeCorrectUser, 400, "User must be unique!"),
          () -> testUserDuplicates(johnDoeCorrectUser),
          () -> testPostApi(signUpApi, johnDoeCorrectUserLower, 400, "User must be unique (ignorecase)!"),
          () -> testPostSignUpResponse(maxMustermannCorrectUserLower, 200),
          () -> testPostApi(signUpApi, maxMustermannCorrectUserLower, 400, "User must be unique!"),
          () -> testPostApi(signUpApi, maxMustermannCorrectUser, 400, "User must be unique (ignorecase)!"),

          // Test wrong POST request for signup api
          () -> testPostApi(signUpApi, johnDoeEmptyName, 400, "Empty name field!"),
          () -> testPostApi(signUpApi, johnDoeNoName, 400, "Name field is absent!"),
          () -> testPostApi(signUpApi, johnDoeEmptyLastName, 400, "Empty lastname field!"),
          () -> testPostApi(signUpApi, johnDoeNoLastName, 400, "Lastname field is absent!"),
          () -> testPostApi(signUpApi, johnDoeEmptyEmail, 400, "Empty email field!"),
          () -> testPostApi(signUpApi, johnDoeNoEmail, 400, "Email field is absent!"),
          () -> testPostApi(signUpApi, johnDoeEmptyPassword, 400, "Empty password field!"),
          () -> testPostApi(signUpApi, johnDoeNoPassword, 400, "Password field is absent!"),
          () -> testPostApi(signUpApi, johnDoeWrongEmail1, 400, "Wrong email!"),
          () -> testPostApi(signUpApi, johnDoeWrongEmail2, 400, "Wrong email!"),

          // Test authentication, positive tests
          () -> testUserRegistration(johnDoeCorrectUserLower, 200, "User must login!"),
          () -> testUserRegistration(johnDoeCorrectUser, 200, "Login case insensitive!"),
          () -> testUserRegistration(maxMustermannCorrectUserLower, 200, "User must login!"),
          () -> testUserRegistration(maxMustermannCorrectUser, 200, "Login case insensitive!"),

          // Test authentication, negative tests
          () -> testUserRegistration(johnDoeWrongPassword, 401, "Wrong password!"),
          () -> testUserRegistration(johnDoeWrongPasswordCaseSensitive, 401,
                  "Password must be case sensitive!"),
          () -> testUserRegistration(johnDoeWrongEmail1, 401, "Wrong user!"),
          () -> testUserRegistration(maxMustermannWrongPassword, 401, "Wrong password!"),
          () -> testUserRegistration(captainNemoWrongUser, 401, "Wrong user"),
          () -> testGetApi(paymentApi, 401, "This api only for authenticated user"),

          // Test persistence
          () -> restartApplication(),
          () -> testUserRegistration(johnDoeCorrectUser, 200, "User must login, after restarting!" +
                  " Check persistence."),
  };
}