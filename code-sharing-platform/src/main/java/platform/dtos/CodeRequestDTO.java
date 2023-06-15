package platform.dtos;

import jakarta.validation.constraints.NotNull;

public class CodeRequestDTO {

    @NotNull
    private String code;
    @NotNull
    private Long time;
    @NotNull
    private Long views;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "{ \"code\": \"%s\", \"time\": %d, \"views\": %d }"
                .formatted(this.code, this.time, this.views);
    }
}
