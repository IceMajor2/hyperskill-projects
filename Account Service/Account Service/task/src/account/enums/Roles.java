package account.enums;

public enum Roles {
    ROLE_ADMINISTRATOR(true), ROLE_ACCOUNTANT(false), ROLE_USER(false);

    private boolean admin;

    private Roles(boolean isAdmin) {
        this.admin = isAdmin;
    }

    public boolean isAdmin() {
        return admin;
    }
}
