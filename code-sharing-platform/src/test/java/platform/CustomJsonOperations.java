package platform;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import platform.models.Code;

public class CustomJsonOperations {

    public static JSONObject createJson(ResponseEntity<String> response) {
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        try {
            JSONObject actualJson = new JSONObject(documentContext.jsonString());
            return actualJson;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject createJson(Code code) {
        JSONObject json = new JSONObject();
        try {
            json.put("date", code.getDateFormatted());
            json.put("code", code.getCode());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject createJson(Object... pairs) {
        JSONObject json = new JSONObject();
        for (int i = 1; i < pairs.length; i += 2) {
            try {
                json.put(pairs[i - 1].toString(), pairs[i]);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
