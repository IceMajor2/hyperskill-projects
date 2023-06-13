package platform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import platform.dtos.CodeDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Table(name = "codes")
@Entity
@JsonPropertyOrder({"date, code"})
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

    public Code(String code) {
        this.code = code;
        this.date = LocalDateTime.now();
        this.setNumId();
    }

    public Code(CodeDTO codeDTO) {
        this.code = codeDTO.getCode();
        this.date = LocalDateTime.now();
        this.setNumId();
    }

    public Code(String code, LocalDateTime date) {
        this.code = code;
        this.date = date;
        this.setNumId();
    }

    public Code() {
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
