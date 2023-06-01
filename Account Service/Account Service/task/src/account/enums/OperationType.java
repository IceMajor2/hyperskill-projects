package account.enums;

public enum OperationType {
    GRANT("GRANT"), REMOVE("REMOVE");

    private String label;

    OperationType(String label) {
        this.label = label;
    }

    public String inLowerCaseExceptFirst() {
        StringBuilder sb = new StringBuilder("");
        sb.append(label.charAt(0));
        sb.append(label.substring(1, label.length()).toLowerCase());
        return sb.toString();
    }
}
