package co.igorski.centralcommittee.model;

public enum Role {
    ADMIN("admin"), TESTER("tester"), SUPER("super");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static String[] getAllValues() {
        Role[] roles = Role.values();
        String[] textValues = new String[roles.length];

        for (int i = 0; i < roles.length; i++) {
            textValues[i] = roles[i].toString();
        }

        return textValues;
    }
}
