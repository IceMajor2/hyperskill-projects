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
}
