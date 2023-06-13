package platform.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import platform.models.Code;

@JsonPropertyOrder({"id", "time", "views"})
public class CodeResponseDTO {

    private String id;
    private long time;
    private long views;

    public CodeResponseDTO(Code code) {
        this.id = code.getId().toString();
        this.time = code.getTime();
        this.views = code.getViews();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
