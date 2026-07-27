package model;

public enum PersonStatus {
    AVAILABLE("활성화"),
    UNAVAILABLE("투입불가"),
    ARCHIVED("보관");

    private final String displayName;

    PersonStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
