package account.enums;

public enum OperationType {
    GRANT, REMOVE;

    public String inLowerCaseExceptFirst() {
        StringBuilder sb = new StringBuilder("");
        sb.append(this.toString().charAt(0));
        sb.append(this.toString().substring(1, this.toString().length()).toLowerCase());
        return sb.toString();
    }
}
