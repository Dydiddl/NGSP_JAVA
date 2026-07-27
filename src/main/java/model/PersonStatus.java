package model;

public enum PersonStatus {
    ACTIVE("활성화"),
    INACTIVE("투입불가"),
    ARCHIVED("보관");

    private final String displayName;

    PersonStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
