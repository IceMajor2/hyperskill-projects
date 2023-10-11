package account.enums;

public enum AccountAction {
    LOCK(false), UNLOCK(true);

    private boolean accountShouldBeNonLocked;

    AccountAction(boolean accountShouldBeNonLocked) {
        this.accountShouldBeNonLocked = accountShouldBeNonLocked;
    }

    public boolean accountShouldBeNonLocked() {
        return accountShouldBeNonLocked;
    }

    public String inLowerCaseExceptFirst() {
        StringBuilder sb = new StringBuilder("");
        sb.append(this.toString().charAt(0));
        sb.append(this.toString().substring(1, this.toString().length()).toLowerCase());
        return sb.toString();
    }
}
