package ui.menu.person;

import ui.input.MenuInputReader;
import ui.input.PersonInputReader;

public class personUpdateMenu {
    // 메뉴 목록
    // 1. 이름 변경
    // 2. 전화번호 변경
    // 3. 성별 변경
    // 4. 주소 변경
    // 5. 통장 변경 (은행, 계좌번호)
    // 0. 사람 관리 메뉴로 이동
    private final MenuInputReader menuInputReader;
    private final PersonInputReader personInputReader;

    public personUpdateMenu(
            MenuInputReader menuInputReader,
            PersonInputReader personInputReader
    ) {
        this.menuInputReader = menuInputReader;
        this.personInputReader = personInputReader;
    }

    public void rus(){

    }


}
