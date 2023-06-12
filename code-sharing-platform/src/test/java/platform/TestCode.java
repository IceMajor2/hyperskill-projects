package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestCode {

    private String code;
    private String date;

    public TestCode(String code) {
        this.code = code;
        this.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
