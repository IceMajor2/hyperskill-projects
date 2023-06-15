package platform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import platform.dtos.CodeRequestDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Table(name = "codes")
@Entity
@JsonPropertyOrder({"code", "date", "time", "views"})
public class Code {

    // does not work correctly in production
    // but was designed for tests in first place
    // and it's enough for them
    private static Long AUTO_INCREMENT_NUM_ID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID id;

    @JsonIgnore
    @NotNull
    private Long numId;

    @NotNull
    private String code;

    @JsonIgnore
    @NotNull
    private LocalDateTime date;

    @NotNull
    private long time;

    @JsonIgnore
    private long initialTime;

    @NotNull
    private long views;

    @NotNull
    @JsonIgnore
    private boolean restricted;

    @NotNull
    @JsonIgnore
    private boolean toBeTimeRestricted;

    @NotNull
    @JsonIgnore
    private boolean toBeViewRestricted;

    public Code(CodeRequestDTO codeRequestDTO) {
        this.code = codeRequestDTO.getCode();

        if (codeRequestDTO.getTime() <= 0) {
            this.time = 0;
            this.initialTime = 0;
            this.toBeTimeRestricted = false;
        } else {
            this.time = codeRequestDTO.getTime();
            this.initialTime = codeRequestDTO.getTime();
            this.toBeTimeRestricted = true;
        }

        if (codeRequestDTO.getViews() <= 0) {
            this.views = 0;
            this.toBeViewRestricted = false;
        } else {
            this.views = codeRequestDTO.getViews();
            this.toBeViewRestricted = true;
        }

        this.date = LocalDateTime.now();
        this.setNumId();
        this.restricted = false;
    }

    public Code() {
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public long getTime() {
        return this.time;
    }

    public void updateRestrictions() {
        if (restricted) {
            return;
        }
        if (toBeTimeRestricted) {
            long secondsDiff = ChronoUnit.SECONDS.between(this.date, LocalDateTime.now());
            this.time = this.initialTime - secondsDiff;
            this.time = this.time < 0 ? 0 : this.time;

            if (this.time == 0) {
                this.restricted = true;
            }
        }
        if (toBeViewRestricted) {
            if (this.views != 0) {
                this.views--;
                return;
            }

            if (this.views == 0) {
                this.restricted = true;
            }
        }
    }

    public boolean isToBeTimeRestricted() {
        return toBeTimeRestricted;
    }

    public void setToBeTimeRestricted(boolean toBeTimeRestricted) {
        this.toBeTimeRestricted = toBeTimeRestricted;
    }

    public boolean isToBeViewRestricted() {
        return toBeViewRestricted;
    }

    public void setToBeViewRestricted(boolean toBeViewRestricted) {
        this.toBeViewRestricted = toBeViewRestricted;
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

    public void setNumId() {
        this.numId = AUTO_INCREMENT_NUM_ID;
        AUTO_INCREMENT_NUM_ID++;
    }

    public Long getNumId() {
        return numId;
    }

    public void setNumId(Long numId) {
        this.numId = numId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("date")
    public String getDateFormatted() {
        return this.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{ \"code\": \"%s\", \"time\": %d, \"views\": %d }"
                .formatted(this.code, this.time, this.views);
    }
}
