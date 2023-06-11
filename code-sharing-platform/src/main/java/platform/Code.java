package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Code {

    private String code;
    private String time;

    public Code(String code) {
        this.code = code;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public Code() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
