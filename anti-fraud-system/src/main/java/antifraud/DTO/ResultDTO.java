package antifraud.DTO;

import antifraud.Enum.TransactionStatus;

public class ResultDTO {

    private TransactionStatus result;
    private String info;

    public ResultDTO() {
    }

    public ResultDTO(TransactionStatus result, String info) {
        this.result = result;
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public TransactionStatus getResult() {
        return result;
    }

    public void setResult(TransactionStatus result) {
        this.result = result;
    }
}
