import account.AccountServiceApplication;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.common.JsonUtils.getPrettyJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

class TestReq {

  private Map<String, Object> properties = new LinkedHashMap<>();

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

  public TestReq setProps(String key, Object value) {
    properties.put(key, value);
    return this;
  }

}

public class AccountServiceTest extends SpringTest {

  private  final String signUpApi = "/api/auth/signup";
  private  final String changePassApi = "/api/auth/changepass";
  private  final String getEmployeePaymentApi = "/api/empl/payment";
  private final String postPaymentApi = "/api/acct/payments";
  private final String putRoleApi = "/api/admin/user/role";
  private final String adminApi = "/api/admin/user/";


  static String[] breachedPass= new String[]{"PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
          "PasswordForApril", "PasswordForMay", "PasswordForJune",
          "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember",
          "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"};

  List<Integer> userIdList = new ArrayList<>();

  private final TestReq ivanIvanov = new TestReq().setProps("name", "Ivan")
          .setProps("lastname", "Ivanov")
          .setProps("email", "IvanIvanov@acme.com")
          .setProps("password", "rXoa4CvqpLxW");
  private final TestReq petrPetrov = new TestReq().setProps("name", "Petr")
          .setProps("lastname", "Petrov")
          .setProps("email", "PetrPetrov@acme.com")
          .setProps("password", "nWza98hjkLPE");
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
  private final TestReq ivanHoe = new TestReq().setProps("name", "Ivan")
          .setProps("lastname", "Hoe")
          .setProps("email", "IvanHoe@acme.com")
          .setProps("password", "nWza98hjkLPE");

  private final String ivanIvanovCorrectUser = ivanIvanov.toJson();
  private final String petrPetrovCorrectUser = petrPetrov.toJson();
  private final String ivanHoeCorrectUser = ivanHoe.toJson();
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
  private final String maxMusWrongEmail = new TestReq(maxMus).setProps("email", "maxmustermann@google.com").toJson();
  private final String captainNemoWrongUser = captainNemo.toJson();
  private final String jDNewPass = new TestReq(johnDoe).setProps("password", "aNob5VvqzRtb").toJson();

  private final String jDDuplicatePass = new TestReq().setProps("new_password", "oMoa3VvqnLxW").toJson();
  private final String jDShortPass = new TestReq().setProps("new_password", "oMoa3Vvqn").toJson();
  private final String jDPass = new TestReq().setProps("new_password", "aNob5VvqzRtb").toJson();

  private String paymentsList = convert(new String[]{
          new TestReq().setProps("employee", "ivanivanov@acme.com").setProps("period", "01-2021")
                  .setProps("salary", 654321).toJson(),
          new TestReq().setProps("employee", "ivanivanov@acme.com").setProps("period", "02-2021")
                  .setProps("salary", 987).toJson(),
          new TestReq().setProps("employee", "ivanivanov@acme.com").setProps("period", "03-2021")
                  .setProps("salary", 21).toJson(),
          new TestReq().setProps("employee", "maxmustermann@acme.com").setProps("period", "01-2021")
                  .setProps("salary", 123456).toJson(),
          new TestReq().setProps("employee", "maxmustermann@acme.com").setProps("period", "02-2021")
                  .setProps("salary", 456789).toJson(),
          new TestReq().setProps("employee", "maxmustermann@acme.com").setProps("period", "03-2021")
                  .setProps("salary", 12).toJson()
  });
  private final String wrongPaymentListData = convert(new String[]{new TestReq().setProps("employee", "maxmustermann@acme.com")
          .setProps("period", "13-2021").setProps("salary", 123456).toJson()});
  private  String wrongPaymentListSalary = convert(new String[]{new TestReq().setProps("employee", "johndoe@acme.com")
          .setProps("period", "11-2022").setProps("salary", -1).toJson()});
  private final String wrongPaymentListDuplicate = convert(new String[]{
          new TestReq().setProps("employee", "maxmustermann@acme.com").setProps("period", "01-2021")
                  .setProps("salary", 123456).toJson(),
          new TestReq().setProps("employee", "maxmustermann@acme.com").setProps("period", "01-2021")
                  .setProps("salary", 456789).toJson()
  });
  private final String updatePayment = new TestReq().setProps("employee", "maxmustermann@acme.com").setProps("period", "01-2021")
          .setProps("salary", 77777).toJson();
  private final String updatePaymentResponse = new TestReq().setProps("name", "Max").setProps("lastname", "Mustermann")
          .setProps("period", "January-2021").setProps("salary", "777 dollar(s) 77 cent(s)").toJson();
  private final String updatePaymentWrongDate = new TestReq().setProps("employee", "maxmustermann@acme.com")
          .setProps("period", "13-2021").setProps("salary", 1234567).toJson();
  private final String updatePaymentWrongSalary = new TestReq().setProps("employee", "maxmustermann@acme.com")
          .setProps("period", "11-2022").setProps("salary", -1).toJson();
  private final String correctPaymentResponse = convert(new String[]{
          new TestReq().setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("period", "March-2021").setProps("salary", "0 dollar(s) 12 cent(s)").toJson(),
          new TestReq().setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("period", "February-2021").setProps("salary", "4567 dollar(s) 89 cent(s)").toJson(),
          new TestReq().setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("period", "January-2021").setProps("salary", "1234 dollar(s) 56 cent(s)").toJson()
  });
  private final String correctPaymentResponseIvanov = convert(new String[]{
          new TestReq().setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("period", "March-2021").setProps("salary", "0 dollar(s) 21 cent(s)").toJson(),
          new TestReq().setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("period", "February-2021").setProps("salary", "9 dollar(s) 87 cent(s)").toJson(),
          new TestReq().setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("period", "January-2021").setProps("salary", "6543 dollar(s) 21 cent(s)").toJson()
  });
  private final String firstResponseAdminApi = convert(new String[]{
          new TestReq().setProps("id", 1).setProps("name", "John").setProps("lastname", "Doe")
                  .setProps("email", "johndoe@acme.com").setProps("roles", new String[] {"ROLE_ADMINISTRATOR"}).toJson(),
          new TestReq().setProps("id", 2).setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("email", "maxmustermann@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson(),
          new TestReq().setProps("id", 3).setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("email", "ivanivanov@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson(),
          new TestReq().setProps("id", 4).setProps("name", "Petr").setProps("lastname", "Petrov")
                  .setProps("email", "petrpetrov@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson()
  });
  private final String secondResponseAdminApi = convert(new String[]{
          new TestReq().setProps("id", 1).setProps("name", "John").setProps("lastname", "Doe")
                  .setProps("email", "johndoe@acme.com").setProps("roles", new String[] {"ROLE_ADMINISTRATOR"}).toJson(),
          new TestReq().setProps("id", 2).setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("email", "maxmustermann@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson(),
          new TestReq().setProps("id", 3).setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("email", "ivanivanov@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson()
  });
  private final String thirdResponseAdminApi = convert(new String[]{
          new TestReq().setProps("id", 1).setProps("name", "John").setProps("lastname", "Doe")
                  .setProps("email", "johndoe@acme.com").setProps("roles", new String[] {"ROLE_ADMINISTRATOR"}).toJson(),
          new TestReq().setProps("id", 2).setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("email", "maxmustermann@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson(),
          new TestReq().setProps("id", 3).setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("email", "ivanivanov@acme.com").
                  setProps("roles", new String[] {"ROLE_ACCOUNTANT", "ROLE_USER"}).toJson()
  });
  private final String fourthResponseAdminApi = convert(new String[]{
          new TestReq().setProps("id", 1).setProps("name", "John").setProps("lastname", "Doe")
                  .setProps("email", "johndoe@acme.com").setProps("roles", new String[] {"ROLE_ADMINISTRATOR"}).toJson(),
          new TestReq().setProps("id", 2).setProps("name", "Max").setProps("lastname", "Mustermann")
                  .setProps("email", "maxmustermann@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson(),
          new TestReq().setProps("id", 3).setProps("name", "Ivan").setProps("lastname", "Ivanov")
                  .setProps("email", "ivanivanov@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson(),
          new TestReq().setProps("id", 4).setProps("name", "Ivan").setProps("lastname", "Hoe")
                  .setProps("email", "ivanhoe@acme.com").setProps("roles", new String[] {"ROLE_USER"}).toJson()
  });

  public AccountServiceTest() {
    super(AccountServiceApplication.class, "../service_db.mv.db");
  }

  private String convert(String[] trs) {
    JsonArray  jsonArray = new JsonArray();
    for (String tr : trs) {
      JsonElement jsonObject = JsonParser.parseString(tr);
      jsonArray.add(jsonObject);
    }
    return jsonArray.toString();
  }


  CheckResult testApi(String user, String body, int status, String api, String method, String message) {

    HttpResponse response = checkResponseStatus(user, body, status, api, method, message);

    return CheckResult.correct();
  }

  /**
   * Method for checking response on Post request for signup API
   *
   * @param body string representation of body content in JSON format (String)
   * @param status required http status for response (int)
   * @return instance of CheckResult class containing result of checks (CheckResult)
   */
  CheckResult testPostSignUpResponse(String body, int status, String[] role) {

    HttpResponse response = checkResponseStatus(null, body, status, signUpApi, "POST", "");

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
                    .value("email", isString(s -> s.equalsIgnoreCase(rightResponse.get("email").getAsString())))
                    .value("roles", role));

    if (userIdList.contains(jsonResponse.get("id").getAsInt())) {
      return CheckResult.wrong("User ID must be unique!\n" +
              "Received response:\n" +
              jsonResponse);
    }


    userIdList.add(jsonResponse.get("id").getAsInt());
    return CheckResult.correct();
  }

  /**
   * Method for check the prohibition of requests specified types
   *
   * @param api testing api (String)
   * @param deniedMethods list of prohibited type requests
   * @param body string representation of body content in JSON format (String)
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

    HttpResponse response = checkResponseStatus(user, "", status, getEmployeePaymentApi, "GET", message);

    return CheckResult.correct();
  }

  CheckResult testChangePassword(String api, String body, int status, String user) {
    JsonObject userJson = getJson(user).getAsJsonObject();

    HttpResponse response = checkResponseStatus(user, body, status, api, "POST", "");

    // Check JSON in response
    if (status == 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("email", userJson.get("email").getAsString().toLowerCase())
                      .value("status", "The password has been updated successfully"));
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
                + "Request body:\n" + json.toString() + "\n"
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


  CheckResult testPostPaymentResponse(String user, String body, int status, String message) {

    HttpResponse response = checkResponseStatus(user, body, status, postPaymentApi, "POST", message);

    // Check JSON in response
    if (response.getStatusCode() == 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("status", "Added successfully!")
                      .anyOtherValues());
    }
    if (response.getStatusCode() == 400) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("error", "Bad Request")
                      .value("path", "/api/acct/payments")
                      .value("status", 400)
                      .anyOtherValues());
    }
    return CheckResult.correct();
  }

  CheckResult testPutPaymentResponse(String user, String body, int status, String message) {

    HttpResponse response = checkResponseStatus(user, body, status, postPaymentApi, "PUT", message);

    // Check JSON in response
    if (response.getStatusCode() == 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("status", "Updated successfully!")
                      .anyOtherValues());
    }
    if (response.getStatusCode() == 400) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("error", "Bad Request")
                      .value("path", "/api/acct/payments")
                      .value("status", 400)
                      .anyOtherValues());
    }
    return CheckResult.correct();
  }

  CheckResult testGetPaymentResponse(String user, int status, String correctResponse, String message) {

    HttpResponse response = checkResponseStatus(user, "", status, getEmployeePaymentApi , "GET", message);

    JsonArray correctJson = getJson(correctResponse).getAsJsonArray();
    JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();

    // Check is it array of JSON in response or something else
    if (!response.getJson().isJsonArray()) {
      return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
              response.getContent().getClass());
    }

    if (responseJson.size() == 0)  {
      return CheckResult.wrong("No data in response body" + "\n"
              + "in response " + getPrettyJson(responseJson)  + "\n"
              + "must be " + getPrettyJson(correctJson));
    }

    if (correctJson.size() != responseJson.size()) {
      return CheckResult.wrong("New data should not be added" + "\n"
              + "in response " + getPrettyJson(responseJson)  + "\n"
              + "must be " + getPrettyJson(correctJson));
    }

    // Check JSON in response
    if (response.getStatusCode() == 200) {
      for (int i = 0; i < responseJson.size(); i++) {
        if (!responseJson.get(i).equals(correctJson.get(i))) {
          return CheckResult.wrong("Get " + getEmployeePaymentApi  +" wrong data in response body" + "\n"
                  + "in response " + getPrettyJson(responseJson) + "\n"
                  + "must be " + getPrettyJson(correctJson));
        }
      }
    }
    return CheckResult.correct();
  }

  CheckResult testGetPaymentResponseParam(String user, int status, String request, String correctResponse, String message) {
    JsonObject userJson = getJson(user).getAsJsonObject();
    String password = userJson.get("password").getAsString();
    String login = userJson.get("email").getAsString().toLowerCase();
    JsonObject json = getJson(correctResponse).getAsJsonObject();
    JsonObject jsonRequest = getJson(request).getAsJsonObject();
    String param = jsonRequest.get("period").getAsString();
    HttpResponse response = get(getEmployeePaymentApi).addParam("period", param).basicAuth(login, password).send();
    if (response.getStatusCode() != status) {
      throw new WrongAnswer("GET" + " " + getEmployeePaymentApi + "?" + param + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + message + "\n"
              + "Response body:\n" + response.getContent() + "\n");
    }
    // Check JSON in response
    if (response.getStatusCode() == 200) {
      if (!response.getJson().equals(json)) {
        return CheckResult.wrong("Get " + getEmployeePaymentApi  + "?period=" + param
                + " wrong data in response body" + "\n"
                + "in response " + getPrettyJson(response.getJson()) + "\n"
                + "must be " + getPrettyJson(json));
      }
    }

    if (response.getStatusCode() == 400) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("error", "Bad Request")
                      .value("path", "/api/empl/payment")
                      .value("status", 400)
                      .anyOtherValues());
    }
    return CheckResult.correct();
  }

  private CheckResult testGetAdminApi(String api, int status, String user, String answer, String message) {

    HttpResponse response = checkResponseStatus(user, "", status, api, "GET", message);

    // Check is it array of JSON in response or something else
    if (!response.getJson().isJsonArray()) {
      return CheckResult.wrong("Wrong object in response, expected array of JSON but was \n" +
              response.getContent().getClass());
    }

    JsonArray correctJson = getJson(answer).getAsJsonArray();
    JsonArray responseJson = getJson(response.getContent()).getAsJsonArray();


    if (responseJson.size() == 0)  {
      return CheckResult.wrong("No data in response body" + "\n"
              + "in response " + getPrettyJson(responseJson)  + "\n"
              + "must be " + getPrettyJson(correctJson));
    }

    if (responseJson.size() != correctJson.size())  {
      return CheckResult.wrong("Wrong dataa in response body" + "\n"
              + "in response " + getPrettyJson(responseJson)  + "\n"
              + "must be " + getPrettyJson(correctJson));
    }


    // Check JSON in response
    if (response.getStatusCode() == 200) {
      for (int i = 0; i < responseJson.size(); i++) {
        String[] roles = new String[correctJson.get(i).getAsJsonObject().getAsJsonArray("roles").size()];
        for(int j=0; j<correctJson.get(i).getAsJsonObject().getAsJsonArray("roles").size(); j++) {
          roles[j]=correctJson.get(i).getAsJsonObject().getAsJsonArray("roles").get(j).getAsString();
        }
        expect(responseJson.get(i).getAsJsonObject().toString()).asJson()
                .check(isObject()
                        .value("id", isInteger())
                        .value("name", correctJson.get(i).getAsJsonObject().get("name").getAsString())
                        .value("lastname", correctJson.get(i).getAsJsonObject().get("lastname").getAsString())
                        .value("email", correctJson.get(i).getAsJsonObject().get("email").getAsString())
                        .value("roles", isArray( roles )));
      }
    }
    return CheckResult.correct();
  }


  CheckResult testDeleteAdminApi(String api, HttpStatus status, String user, String param,
                                 String answer, String message) {

    HttpResponse response = checkResponseStatus(user, "", status.value(),
            api + param, "DELETE", message);

    // Check JSON in response
    if (response.getStatusCode() == 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("user", param.toLowerCase())
                      .value("status", answer));
    }

    if (response.getStatusCode() != 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("error", status.getReasonPhrase())
                      .value("path", api + param)
                      .value("status", status.value())
                      .value("message", answer)
                      .anyOtherValues());
    }
    return CheckResult.correct();
  }

  CheckResult testPutAdminApi(String api, HttpStatus status, String user, String reqUser,
                              String role, String operation, String[] respRoles, String message) {

    JsonObject jsonUser = getJson(reqUser).getAsJsonObject();
    JsonObject request = new JsonObject();
    request.addProperty("user", jsonUser.get("email").getAsString());
    request.addProperty("operation", operation);
    request.addProperty("role", role);

    HttpResponse response = checkResponseStatus(user, request.toString(), status.value(), api, "PUT", message);

    // Check JSON in response
    if (response.getStatusCode() == 200) {
      expect(response.getContent()).asJson()
              .check(isObject()
                      .value("id", isInteger())
                      .value("name", jsonUser.get("name").getAsString())
                      .value("lastname", jsonUser.get("lastname").getAsString())
                      .value("email", jsonUser.get("email").getAsString().toLowerCase())
                      .value("roles", isArray(respRoles)));
    }

    if (response.getStatusCode() != 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("error", status.getReasonPhrase())
                      .value("path", api)
                      .value("status", status.value())
                      .value("message", respRoles[0])
                      .anyOtherValues());
    }
    return CheckResult.correct();
  }

  CheckResult testRoleModelNegative(String api, String method, HttpStatus status, String user, String body, String message) {

    HttpResponse response = checkResponseStatus(user, body, status.value(), api, method.toUpperCase(), message);

    // Check JSON in response
    if (response.getStatusCode() != 200) {
      expect(response.getContent()).asJson().check(
              isObject()
                      .value("error", status.getReasonPhrase())
                      .value("path", api)
                      .value("status", status.value())
                      .value("message", "Access Denied!")
                      .anyOtherValues());
    }
    return CheckResult.correct();
  }

  /**
   * Method for testing api response
   *
   * @param user string representation of user information in JSON format (String)
   * @param body request body (String)
   * @param status expected response status (int)
   * @param api testing api (String)
   * @param method method for api (String)
   * @param message test hints for student (String)
   * @return response (HttpResponse)
   */
  private HttpResponse checkResponseStatus(String user, String body,
                                           int status, String api, String method, String message) {
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
      String login = userJson.get("email").getAsString().toLowerCase();
      request = request.basicAuth(login, password);
    }
    HttpResponse response = request.send();

    if (response.getStatusCode() != status) {
      throw new WrongAnswer(method + " " + api  + " should respond with "
              + "status code " + status + ", responded: " + response.getStatusCode() + "\n"
              + message + "\n"
              + "Response body:\n" + response.getContent() + "\n");
    }
    return response;
  }

  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[] {

          // Testing user registration negative tests
          () -> testApi(null, jDEmptyName, 400, signUpApi, "POST", "Empty name field!"), // 1
          () -> testApi(null, jDNoName, 400, signUpApi, "POST", "Name field is absent!"), // 2
          () -> testApi(null, jDEmptyLastName, 400, signUpApi, "POST", "Empty lastname field!"), // 3
          () -> testApi(null, jDNoLastName, 400, signUpApi, "POST", "Lastname field is absent!"), // 4
          () -> testApi(null, jDEmptyEmail, 400, signUpApi, "POST", "Empty email field!"), // 5
          () -> testApi(null, jDNoEmail, 400, signUpApi, "POST", "Email field is absent!"), // 6
          () -> testApi(null, jDEmptyPassword, 400, signUpApi, "POST", "Empty password field!"), // 7
          () -> testApi(null, jDNoPassword, 400, signUpApi, "POST", "Password field is absent!"),// 8
          () -> testApi(null, jDWrongEmail1, 400, signUpApi, "POST", "Wrong email!"), // 9
          () -> testApi(null, jDWrongEmail2, 400, signUpApi, "POST", "Wrong email!"), // 10
          () -> testBreachedPass(signUpApi, "", "", jDCorrectUser,
                  "Sending password from breached list"), // 11

          // Testing user registration positive tests
          () -> testPostSignUpResponse(jDCorrectUser, 200, new String[] {"ROLE_ADMINISTRATOR"}), // 12
          () -> testPostSignUpResponse(maxMusLower, 200, new String[] {"ROLE_USER"}), // 13
          () -> testPostSignUpResponse(ivanIvanovCorrectUser, 200, new String[] {"ROLE_USER"}), // 14
          () -> testPostSignUpResponse(petrPetrovCorrectUser, 200, new String[] {"ROLE_USER"}), // 15

//          // Testing user registration negative tests
          () -> testApi(null, jDCorrectUser, 400, signUpApi, "POST", "User must be unique!"), // 16
          () -> testUserDuplicates(jDCorrectUser), // 17
          () -> testApi(null, jDLower, 400, signUpApi, "POST",
                  "User must be unique (ignorecase)!"), // 18

//
//          // Test authentication, positive tests
          () -> testUserRegistration(maxMusLower, 200, "User must login!"), // 19
          () -> testUserRegistration(maxMusCorrectUser, 200, "Login case insensitive!"), // 20
//
//          // Test authentication, negative tests
          () -> testUserRegistration(maxMusWrongPassword, 401, "Wrong password!"), // 21
          () -> testUserRegistration(maxMusWrongEmail, 401, "Wrong password!"), // 22
          () -> testUserRegistration(captainNemoWrongUser, 401, "Wrong user"), // 23
          () -> testApi(null, "", 401, getEmployeePaymentApi, "GET",
                  "This api only for authenticated user"), // 24
//
//          // Testing changing password
          () -> testApi(null, jDDuplicatePass, 401, changePassApi, "POST",
                  "This api only for authenticated user"), // 25
          () -> testApi(jDCorrectUser, jDShortPass, 400, changePassApi, "POST",
                  "The password length must be at least 12 chars!"), // 26
          () -> testApi(jDCorrectUser, jDDuplicatePass, 400, changePassApi, "POST",
                  "The passwords must be different!"), // 27
          () -> testBreachedPass(changePassApi, "JohnDoe@acme.com", "oMoa3VvqnLxW",
                  jDDuplicatePass, "Sending password from breached list"), // 28
          () -> testChangePassword(changePassApi, jDPass, 200, jDCorrectUser), // 29
          () -> testApi(jDCorrectUser, "", 401, adminApi, "GET",
                  "Password must be changed!"), // 30
          () -> testApi(jDNewPass, "", 200, adminApi, "GET",
                  "Password must be changed!"), // 31

          // Testing persistence
          () -> restartApplication(), // 32
          () -> testUserRegistration(maxMusCorrectUser, 200, "User must login, after restarting!" +
                  " Check persistence."), // 33

          // Testing admin functions
          // Delete user
          () -> testApi(maxMusCorrectUser, "", 403, "/api/admin/user/", "DELETE",
                  "Api must be available only to admin user"), // 34
          () -> testApi(maxMusCorrectUser, "", 403, "/api/admin/user/johndoe@acme.com", "DELETE",
                  "Api must be available only to admin user"), // 35
          () -> testGetAdminApi("/api/admin/user/", 200, jDNewPass,
                  firstResponseAdminApi, "Api must be available to admin user"), // 36
          () -> testDeleteAdminApi("/api/admin/user/", HttpStatus.OK, jDNewPass,
                  "petrpetrov@acme.com", "Deleted successfully!", "Trying to delete user"), // 37
          () -> testGetAdminApi("/api/admin/user/", 200, jDNewPass,
                  secondResponseAdminApi, "User must be deleted!"), // 38
          () -> testDeleteAdminApi("/api/admin/user/", HttpStatus.BAD_REQUEST,
                  jDNewPass, "johndoe@acme.com",
                  "Can't remove ADMINISTRATOR role!", "Trying to delete admin"), // 39
          () -> testDeleteAdminApi("/api/admin/user/", HttpStatus.NOT_FOUND,
                  jDNewPass, "johndoe@goole.com",
                  "User not found!", "Trying to delete non existing user"), // 40
          () -> testPostSignUpResponse(ivanHoeCorrectUser, 200, new String[] {"ROLE_USER"}), // 41
          () -> testUserRegistration(ivanHoeCorrectUser, 200, "User \"ivanhoe@acme.com\" must be added!"), // 42
          () -> testGetAdminApi("/api/admin/user/", 200, jDNewPass,
                  fourthResponseAdminApi, "User \"ivanhoe@acme.com\" must be added!"), // 43
          () -> testDeleteAdminApi("/api/admin/user/", HttpStatus.OK, jDNewPass,
                  "ivanhoe@acme.com", "Deleted successfully!", "Trying to delete user \"ivanhoe@acme.com\""), // 44

          // Testing persistence
          () -> restartApplication(), // 45
          () -> testGetAdminApi("/api/admin/user/", 200, jDNewPass,
                  secondResponseAdminApi, "User must be deleted!"), // 46



          // Changing roles
          () -> testPutAdminApi(putRoleApi, HttpStatus.OK, jDNewPass,
                  ivanIvanovCorrectUser, "ACCOUNTANT", "GRANT",
                  new String[] {"ROLE_ACCOUNTANT", "ROLE_USER"}, ""), // 47
          () -> testGetAdminApi("/api/admin/user/", 200, jDNewPass,
                  thirdResponseAdminApi, "Role must be changed!"), // 48
          () -> testPutAdminApi(putRoleApi, HttpStatus.OK, jDNewPass,
                  ivanIvanovCorrectUser, "ACCOUNTANT", "REMOVE",
                  new String[] {"ROLE_USER"}, ""),
          () -> testGetAdminApi("/api/admin/user/", 200, jDNewPass,
                  secondResponseAdminApi, "Role must be changed!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.OK, jDNewPass,
                  ivanIvanovCorrectUser, "ACCOUNTANT", "GRANT",
                  new String[] {"ROLE_ACCOUNTANT", "ROLE_USER"}, ""),

          // Testing admin functions, negative tests
          () -> testPutAdminApi(putRoleApi, HttpStatus.NOT_FOUND, jDNewPass,
                  ivanIvanovCorrectUser, "NEW_ROLE", "GRANT",
                  new String[] {"Role not found!"}, "Trying add not existing role!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.BAD_REQUEST, jDNewPass,
                  ivanIvanovCorrectUser, "ADMINISTRATOR", "GRANT",
                  new String[] {"The user cannot combine administrative and business roles!"},
                  "Trying add administrative role to business user!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.BAD_REQUEST, jDNewPass,
                  jDNewPass, "USER", "GRANT",
                  new String[] {"The user cannot combine administrative and business roles!"},
                  "Trying add business role to administrator!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.BAD_REQUEST, jDNewPass,
                  jDNewPass, "ADMINISTRATOR", "REMOVE",
                  new String[] {"Can't remove ADMINISTRATOR role!"}, "Trying remove administrator role!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.BAD_REQUEST, jDNewPass,
                  maxMusCorrectUser, "USER", "REMOVE",
                  new String[] {"The user must have at least one role!"}, "Trying remove single role!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.BAD_REQUEST, jDNewPass,
                  maxMusCorrectUser, "ACCOUNTANT", "REMOVE",
                  new String[] {"The user does not have a role!"}, "Trying remove not granted role!"),
          () -> testPutAdminApi(putRoleApi, HttpStatus.NOT_FOUND, jDNewPass,
                  captainNemoWrongUser, "ACCOUNTANT", "REMOVE",
                  new String[] {"User not found!"}, "Trying remove role from non existing user!"),

          // Testing role model negative case
          () -> testRoleModelNegative(putRoleApi, "PUT", HttpStatus.FORBIDDEN, ivanIvanovCorrectUser,
                  "", "Trying to access administrative endpoint with business user"),
          () -> testRoleModelNegative("/api/admin/user/", "GET", HttpStatus.FORBIDDEN, ivanIvanovCorrectUser,
                  "", "Trying to access administrative endpoint with business user"),
          () -> testRoleModelNegative("/api/admin/user", "DELETE", HttpStatus.FORBIDDEN, ivanIvanovCorrectUser,
                  "", "Trying to access administrative endpoint with business user"),
          () -> testRoleModelNegative(postPaymentApi, "POST", HttpStatus.FORBIDDEN, jDNewPass,
                  "", "Trying to access business endpoint with administrative user"),
          () -> testRoleModelNegative(postPaymentApi, "POST", HttpStatus.FORBIDDEN, maxMusCorrectUser,
                  "", "Trying to access endpoint with wrong role"),
          () -> testRoleModelNegative(getEmployeePaymentApi, "GET", HttpStatus.FORBIDDEN, jDNewPass,
                  "", "Trying to access business endpoint with administrative user"),

          // Testing business logic
          () -> testPostPaymentResponse(ivanIvanovCorrectUser, paymentsList, 200, "Payment list must be added"),
          () -> testGetPaymentResponse(maxMusCorrectUser, 200, correctPaymentResponse,
                  "Wrong status code!"),
          () -> testGetPaymentResponse(ivanIvanovCorrectUser, 200, correctPaymentResponseIvanov,
                  "Wrong status code!"),
          () -> testPostPaymentResponse(ivanIvanovCorrectUser, wrongPaymentListSalary, 400, "Wrong salary in payment list"),
          () -> testGetPaymentResponse(maxMusCorrectUser, 200, correctPaymentResponse,
                  "Wrong status code!"),
          () -> testPostPaymentResponse(ivanIvanovCorrectUser, wrongPaymentListData, 400, "Wrong data in payment list"),
          () -> testGetPaymentResponse(maxMusCorrectUser, 200, correctPaymentResponse,
                  "Wrong status code!"),
          () -> testPostPaymentResponse(ivanIvanovCorrectUser, wrongPaymentListDuplicate, 400, "Duplicated entry in payment list"),
          () -> testGetPaymentResponse(maxMusCorrectUser, 200, correctPaymentResponse,
                  "Wrong status code!"),
          () -> testPutPaymentResponse(ivanIvanovCorrectUser, updatePaymentWrongDate, 400,"Wrong date in request body!"),
          () -> testPutPaymentResponse(ivanIvanovCorrectUser, updatePaymentWrongSalary, 400, "Wrong salary in request body!"),
          () -> testPutPaymentResponse(ivanIvanovCorrectUser, updatePayment, 200, "Salary must be update!"),
          () -> testGetPaymentResponseParam(maxMusCorrectUser, 200, updatePayment, updatePaymentResponse,
                  "Salary must be update!"),
          () -> testGetPaymentResponseParam(maxMusCorrectUser, 400, updatePaymentWrongDate, updatePaymentResponse,
                  "Wrong date in request!"),
  };

}