package account.enums;

public enum Roles {
    ROLE_ADMINISTRATOR(true), ROLE_ACCOUNTANT(false), ROLE_USER(false);

    private boolean admin;

    Roles(boolean isAdmin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }
}
