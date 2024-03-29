package account.enumerated;

public enum Roles {
    ROLE_ACCOUNTANT(false), ROLE_ADMINISTRATOR(true), ROLE_AUDITOR(false), ROLE_USER(false);

    private boolean admin;

    private Roles(boolean isAdmin) {
        this.admin = isAdmin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String noPrefix() {
        return this.toString().substring(5);
    }
}
