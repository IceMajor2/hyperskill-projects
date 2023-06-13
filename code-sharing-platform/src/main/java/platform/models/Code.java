package platform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import platform.dtos.CodeRequestDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Table(name = "codes")
@Entity
@JsonPropertyOrder({"code", "date", "time", "views"})
public class Code {

    private static Long AUTO_INCREMENT_NUM_ID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID id;

    @JsonIgnore
    @Nonnull
    private Long numId;

    @Nonnull
    private String code;

    @JsonIgnore
    @Nonnull
    private LocalDateTime date;

    @Nonnull
    private long time;

    @Nonnull
    private long views;

    @Nonnull
    private boolean restricted;

    public Code(CodeRequestDTO codeRequestDTO) {
        this.code = codeRequestDTO.getCode();
        this.time = codeRequestDTO.getTime();
        this.views = codeRequestDTO.getViews();
        this.date = LocalDateTime.now();
        this.setNumId();
        this.restricted = this.determineRestriction(this.time, this.views);
    }

    public Code() {
    }

    public boolean determineRestriction(long time, long views) {
        if(time <= 0 && views <= 0) {
            return false;
        }
        return true;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
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

    public String toString() {
        return this.code;
    }
}
