import account.AccountServiceApplication;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.common.JsonUtils.getPrettyJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

class TestReq {

  private Map<String, String> properties = new LinkedHashMap<>();

  // Deep copy
  public TestReq(TestReq another) {
    this.properties = another.properties.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public TestReq() {
  }

  public String toJson() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    try {
      return mapper.writeValueAsString(this.properties);
    } catch (JsonProcessingException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public TestReq setProps(String key, String value) {
    properties.put(key, value);
    return this;
  }

}

public class AccountServiceTest extends SpringTest {

  private  final String signUpApi = "/api/auth/signup";
  private  final String changePassApi = "/api/auth/changepass";
  private  final String paymentApi = "/api/empl/payment";
  private final TestReq johnDoe = new TestReq().setProps("name", "John")
          .setProps("lastname", "Doe")
          .setProps("email", "JohnDoe@acme.com")
          .setProps("password", "oMoa3VvqnLxW");
  private final TestReq maxMus = new TestReq().setProps("name", "Max")
          .setProps("lastname", "Mustermann")
          .setProps("email", "MaxMustermann@acme.com")
          .setProps("password", "ai0y9bMvyF6G");
  private final TestReq captainNemo = new TestReq().setProps("name", "Captain")
          .setProps("lastname", "Nemo")
          .setProps("email", "nautilus@pompilius.com")
          .setProps("password", "wings");

  private final String jDCorrectUser = johnDoe.toJson();
  private final String jDEmptyName = new TestReq(johnDoe).setProps("name", "").toJson();
  private final String jDNoName = new TestReq(johnDoe).setProps("name", null).toJson();
  private final String jDEmptyLastName = new TestReq(johnDoe).setProps("lastname", "").toJson();
  private final String jDNoLastName = new TestReq(johnDoe).setProps("lastname", null).toJson();
  private final String jDEmptyEmail = new TestReq(johnDoe).setProps("email", "").toJson();
  private final String jDNoEmail = new TestReq(johnDoe).setProps("email", null).toJson();
  private final String jDEmptyPassword = new TestReq(johnDoe).setProps("password", "").toJson();
  private final String jDNoPassword = new TestReq(johnDoe).setProps("password", null).toJson();
  private final String jDWrongEmail1 = new TestReq(johnDoe).setProps("email", "johndoeacme.com").toJson();
  private final String jDWrongEmail2 = new TestReq(johnDoe).setProps("email", "johndoe@google.com").toJson();
  private final String maxMusCorrectUser = maxMus.toJson();
  private final String jDLower = new TestReq(johnDoe).setProps("email", "johndoe@acme.com").toJson();
  private final String maxMusLower = new TestReq(maxMus).setProps("email", "maxmustermann@acme.com").toJson();
  private final String jDWrongPassword = new TestReq(johnDoe).setProps("password", "none").toJson();
  private final String maxMusWrongPassword = new TestReq(maxMus).setProps("password", "none").toJson();
  private final String captainNemoWrongUser = captainNemo.toJson();

  private final String jDDuplicatePass = new TestReq().setProps("new_password", "oMoa3VvqnLxW").toJson();

  private final String jDShortPass1 = new TestReq().setProps("new_password", "o").toJson();
  private final String jDShortPass2 = new TestReq().setProps("new_password", "oM").toJson();
  private final String jDShortPass3 = new TestReq().setProps("new_password", "oMo").toJson();
  private final String jDShortPass4 = new TestReq().setProps("new_password", "oMoa").toJson();
  private final String jDShortPass5 = new TestReq().setProps("new_password", "oMoa3").toJson();
  private final String jDShortPass6 = new TestReq().setProps("new_password", "oMoa3V").toJson();
  private final String jDShortPass7 = new TestReq().setProps("new_password", "oMoa3Vv").toJson();
  private final String jDShortPass8 = new TestReq().setProps("new_password", "oMoa3Vvq").toJson();
  private final String jDShortPass9 = new TestReq().setProps("new_password", "oMoa3Vvqn").toJson();
  private final String jDShortPass10 = new TestReq().setProps("new_password", "oMoa3Vvqno").toJson();
  private final String jDShortPass11 = new TestReq().setProps("new_password", "oMoa3VvqnoM").toJson();


  private final String jDPass = new TestReq().setProps("new_password", "aNob5VvqzRtb").toJson();


  static String[] breachedPass= new String[]{"PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
          "PasswordForApril", "PasswordForMay", "PasswordForJune",
          "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember",
          "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"};

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

  CheckResult testPostApiWithAuth(String api, String body, int status, String login, String pass, String message) {
    HttpResponse response = post(api, body).basicAuth(login, pass).send();

    if (response.getStatusCode() == 404) {
      return CheckResult.wrong("POST " + api + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + "Endpoint not found!" + "\n"
              + "Response body:\n" + response.getContent() + "\n"
              + "Request body:\n" + body);
    }

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
   * Method for checking status code of response Get request for API
   *
   * @param api testing api (String)
   * @param status required http status for response (int)
   * @param message hint about reason of error (String)
   * @param login login
   * @param password password
   * @return instance of CheckResult class containing result of checks (CheckResult)
   */
  CheckResult testGetApiAuth(String api, int status, String login, String password, String message) {
    HttpResponse response = get(api).basicAuth(login, password).send();

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
    String login = userJson.get("email").getAsString().toLowerCase();
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

  CheckResult testChangePassword(String api, String body, int status, String user, String answer) {
    JsonObject userJson = getJson(user).getAsJsonObject();
    String pass = userJson.get("password").getAsString();
    String login = userJson.get("email").getAsString().toLowerCase();
    HttpResponse response = post(api, body).basicAuth(login, pass).send();
    if (response.getStatusCode() != status) {
      return CheckResult.wrong("POST " + api + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + "Response body:\n" + response.getContent() + "\n"
              + "Request body:\n" + body);
    }
    // Check JSON in response
    if (status == 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("email", userJson.get("email").getAsString().toLowerCase())
                      .value("status", answer));
    }
    if (status == 400) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("status", 400)
                      .value("error", "Bad Request")
                      .value("message", answer)
                      .value("path", "/api/auth/changepass")
                      .anyOtherValues());
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

  private CheckResult testBreachedPass(String api, String login, String password, String body, String message) {
    JsonObject json = getJson(body).getAsJsonObject();
    HttpResponse response;
    for (int index = 0; index < breachedPass.length; index++) {
      if (json.has("password")) {
        json.remove("password");
        json.addProperty("password", breachedPass[index]);
      } else if (json.has("new_password")) {
        json.remove("new_password");
        json.addProperty("new_password", breachedPass[index]);
      }
      if (login.isEmpty() || password.isEmpty()) {
        response = post(api, json.toString()).send();
      } else {
        response = post(api, json.toString()).basicAuth(login, password).send();
      }

      if (response.getStatusCode() != 400) {
        return CheckResult.wrong("POST " + api + " should respond with "
                + "status code 400 , responded: " + response.getStatusCode() + "\n"
                + "Response body:\n" + response.getContent() + "\n"
                + "Request body:\n" + getPrettyJson(json) + "\n"
                + message);
      }
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("status", 400)
                      .value("error", "Bad Request")
                      .value("message", "The password is in the hacker's database!")
                      .anyOtherValues());

    }
    return CheckResult.correct();
  }

  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[] {

          // Test wrong POST request for signup api
          () -> testPostApi(signUpApi, jDEmptyName, 400, "Empty name field!"),
          () -> testPostApi(signUpApi, jDNoName, 400, "Name field is absent!"),
          () -> testPostApi(signUpApi, jDEmptyLastName, 400, "Empty lastname field!"),
          () -> testPostApi(signUpApi, jDNoLastName, 400, "Lastname field is absent!"),
          () -> testPostApi(signUpApi, jDEmptyEmail, 400, "Empty email field!"),
          () -> testPostApi(signUpApi, jDNoEmail, 400, "Email field is absent!"),
          () -> testPostApi(signUpApi, jDEmptyPassword, 400, "Empty password field!"),
          () -> testPostApi(signUpApi, jDNoPassword, 400, "Password field is absent!"),
          () -> testPostApi(signUpApi, jDWrongEmail1, 400, "Wrong email!"),
          () -> testPostApi(signUpApi, jDWrongEmail2, 400, "Wrong email!"),
          // Test user registration on signup api
          // Test user registration on signup api
          () -> testBreachedPass(signUpApi, "", "",
                  jDCorrectUser, "Sending password from breached list"),
          () -> testPostSignUpResponse(jDCorrectUser, 200),
          () -> testPostApi(signUpApi, jDCorrectUser, 400, "User must be unique!"),
          () -> testUserDuplicates(jDCorrectUser),
          () -> testPostApi(signUpApi, jDLower, 400, "User must be unique (ignorecase)!"),
          () -> testPostSignUpResponse(maxMusLower, 200),
          () -> testPostApi(signUpApi, maxMusLower, 400, "User must be unique!"),
          () -> testPostApi(signUpApi, maxMusCorrectUser, 400, "User must be unique (ignorecase)!"),
          // Test authentication, positive tests
          () -> testUserRegistration(jDLower, 200, "User must login!"),
          () -> testUserRegistration(jDCorrectUser, 200, "Login case insensitive!"),
          () -> testUserRegistration(maxMusLower, 200, "User must login!"),
          () -> testUserRegistration(maxMusCorrectUser, 200, "Login case insensitive!"),
          // Test authentication, negative tests
          () -> testUserRegistration(jDWrongPassword, 401, "Wrong password!"),
          () -> testUserRegistration(jDWrongEmail1, 401, "Wrong user!"),
          () -> testUserRegistration(maxMusWrongPassword, 401, "Wrong password!"),
          () -> testUserRegistration(captainNemoWrongUser, 401, "Wrong user"),
          () -> testGetApi(paymentApi, 401, "This api only for authenticated user"),
          // Test changing password
          () -> testPostApi(changePassApi, jDDuplicatePass, 401, "This api only for authenticated user"),
          () -> testChangePassword(changePassApi, jDShortPass1, 400, jDCorrectUser,
                  "Password length must be 12 chars minimum!"),
          () -> testChangePassword(changePassApi, jDDuplicatePass, 400, jDCorrectUser,
                  "The passwords must be different!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass1, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass2, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass3, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass4, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass5, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass6, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass7, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass8, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass9, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass10, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDShortPass11, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The password length must be at least 12 chars!"),
          () -> testPostApiWithAuth(changePassApi, jDDuplicatePass, 400,
                  "JohnDoe@acme.com", "oMoa3VvqnLxW", "The passwords must be different!"),
          () -> testBreachedPass(changePassApi, "JohnDoe@acme.com", "oMoa3VvqnLxW",
                  jDDuplicatePass, "Sending password from breached list"),
          // Test persistence
          () -> restartApplication(),
          () -> testUserRegistration(maxMusCorrectUser, 200, "User must login, after restarting!" +
                  " Check persistence."),
          () -> testChangePassword(changePassApi, jDPass, 200, jDCorrectUser, "The password has been updated successfully"),
          () -> testGetApiAuth(paymentApi, 401,"JohnDoe@acme.com",
                  "oMoa3VvqnLxW", "Password must be changed!"),
          () -> testGetApiAuth(paymentApi, 200,"JohnDoe@acme.com",
                  "aNob5VvqzRtb", "Password must be changed!"),
          () -> testChangePassword(changePassApi, jDPass, 200, maxMusCorrectUser, "The password has been updated successfully"),
          () -> testGetApiAuth(paymentApi, 401,"MaxMustermann@acme.com",
                  "ai0y9bMvyF6G", "Password must be changed!"),
          () -> testGetApiAuth(paymentApi, 200,"MaxMustermann@acme.com",
                  "aNob5VvqzRtb", "Password must be changed!")
  };
}