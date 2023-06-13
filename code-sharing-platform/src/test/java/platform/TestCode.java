package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestCode {

    private String code;
    private String date;
    private LocalDateTime dateObj;

    public TestCode(String code) {
        this.code = code;
        this.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.dateObj = LocalDateTime.parse
                (this.date, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public TestCode(String code, LocalDateTime date) {
        this.code = code;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.dateObj = date;
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

    public LocalDateTime getDateObj() {
        return dateObj;
    }

    public void setDateObj(LocalDateTime dateObj) {
        this.dateObj = dateObj;
    }
}
