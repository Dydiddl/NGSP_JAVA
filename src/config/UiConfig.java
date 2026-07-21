package config;

public final class UiConfig {
    public static final String APPLICATION_NAME =
            "남강조경 시스템";
    public static final String MAIN_MENU_TITLE =
            "메인 메뉴";
    public static final String PERSON_MENU_TITLE =
            "인사 메뉴";
    public static final String DIVIDER =
            "=======================================";
    public static final int INVALID_CHOICE_VALUE = -1;

    private UiConfig() {
        // 객체 생성을 막기 위한 private 생성자
        // final: 상속금지, private: New금지
    }
}
