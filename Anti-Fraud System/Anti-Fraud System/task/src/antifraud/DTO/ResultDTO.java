package antifraud.DTO;

public class ResultDTO {

    private String result;
    private String info;

    public ResultDTO() {
    }

    public ResultDTO(String result, String info) {
        this.result = result;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
