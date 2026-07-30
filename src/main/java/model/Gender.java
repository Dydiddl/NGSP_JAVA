package model;

import java.util.Set;

public enum Gender {

    MALE("남성", Set.of("남", "남자", "M", "Male", "male")),
    FEMALE("여성", Set.of("여", "여자", "여성", "F", "FEMAILE", "Female", "female"));

    private final String displayName;
    private final Set<String> aliases;

    Gender(String displayName, Set<String> aliases) {
        this.displayName = displayName;
        this.aliases = aliases;
    }

    public String getDisplayName() {
        return displayName;
    }
    public Set<String> getAliases(){return aliases;}
}