# NGSP_JAVA 코딩 규칙

## 1. 문서 개요

### 1.1 문서 목적

이 문서는 `NGSP_JAVA` 프로젝트에서 코드를 작성하고 수정할 때 따라야 할 공통 규칙을 정의합니다.

주요 목적은 다음과 같습니다.

* 코드 작성 방식의 일관성 유지
* 클래스와 메서드의 책임 명확화
* 새로운 기능 추가 시 구조가 무너지는 문제 방지
* 중복 코드 감소
* 수정 범위를 예측할 수 있는 구조 유지
* 다른 개발자 또는 미래의 개발자가 코드를 쉽게 이해하도록 구성
* AI가 생성한 코드를 프로젝트 구조에 맞게 검토할 기준 마련

이 문서의 규칙은 절대적인 Java 표준 전체를 정의하는 것이 아니라, `NGSP_JAVA` 프로젝트에 적용할 실용적인 기준입니다.

---

## 2. 기본 개발 원칙

### 2.1 작동보다 구조를 먼저 확인한다

코드가 실행된다는 이유만으로 좋은 코드라고 판단하지 않습니다.

기능을 구현하기 전에 다음 내용을 확인합니다.

```text
이 코드는 어느 계층에 속하는가?

이 클래스가 담당해야 하는 책임인가?

이미 같은 역할을 하는 코드가 존재하는가?

향후 다른 기능에서도 재사용할 가능성이 있는가?

이 코드를 수정하면 어느 범위까지 영향을 주는가?
```

---

### 2.2 하나의 클래스는 하나의 핵심 책임을 가진다

각 클래스는 하나의 주요 역할을 담당합니다.

```text
Main                  객체 생성과 프로그램 시작
Menu                  화면 출력과 메뉴 이동
InputReader           사용자 입력
Service               업무 규칙
Repository            데이터베이스 접근
Model                  데이터 표현
Validator              유효성 검사
Normalizer             저장 형식 정규화
Formatter              출력 형식 변환
Importer               외부 파일 변환
Config                 설정값 관리
```

잘못된 예:

```java
public final class PersonMenu {

    public void registerPerson() {
        String name = scanner.nextLine();

        String sql = """
                INSERT INTO person (name)
                VALUES (?)
                """;

        DriverManager.getConnection(...);
    }
}
```

위 코드는 UI, 입력, SQL, 데이터베이스 연결을 한 클래스에서 처리합니다.

권장 구조:

```text
PersonMenu
→ PersonInputReader
→ PersonService
→ PersonRepository
→ SQLite
```

---

### 2.3 작은 기능을 완전히 연결한 후 확장한다

여러 기능의 빈 껍데기를 동시에 만들기보다 하나의 기능을 끝까지 연결합니다.

권장 순서:

```text
사람 직접 등록
→ 사람 전체 조회
→ 사람 상세 조회
→ 사람 수정
→ 사람 삭제
→ CSV 등록
```

사람 등록 기능의 완료 기준:

```text
메뉴 진입
→ 사용자 입력
→ 정규화
→ 검증
→ Service
→ Repository
→ SQLite 저장
→ 결과 출력
→ 오류 처리
```

---

### 2.4 과도한 분리를 피한다

메서드 하나만 사용하는 작은 기능을 무조건 새로운 클래스로 분리하지 않습니다.

초기에는 Service의 private 메서드로 둘 수 있습니다.

```java
private String normalizePhone(String phone) {
    return phone.replaceAll("[^0-9]", "");
}
```

다음 경우 별도 클래스로 분리합니다.

* 여러 클래스에서 반복 사용됨
* 코드가 길어져 핵심 흐름을 방해함
* 독립적인 테스트가 필요함
* 명확한 별도 책임을 가짐

분리 후 예:

```text
PersonNormalizer
PersonValidator
PersonFormatter
```

---

## 3. 언어 및 문자 규칙

### 3.1 코드 식별자는 영어를 사용한다

다음 항목은 영어로 작성합니다.

* 클래스 이름
* 메서드 이름
* 변수 이름
* 상수 이름
* 패키지 이름
* 데이터베이스 테이블 및 컬럼
* 파일 이름

권장:

```java
PersonService personService;
String phoneNumber;
long personId;
```

지양:

```java
PersonService 사람서비스;
String 전화번호;
long 사람아이디;
```

코드 식별자를 영어로 작성하는 이유:

* Java 표준 라이브러리와 자연스럽게 연결됨
* 검색과 문서 확인이 쉬움
* IDE 자동완성과 잘 맞음
* 다른 개발자와 협업하기 쉬움
* 운영체제 및 도구 간 인코딩 문제 감소
* 일반적인 Java 관례와 일치함

---

### 3.2 사용자 화면과 메시지는 한글을 사용한다

CLI 사용자는 한국어를 사용하므로 화면 메시지는 한글로 작성합니다.

```java
System.out.println("사람 등록이 완료되었습니다.");
```

지양:

```java
System.out.println("Person registration completed.");
```

내부 코드:

```java
registerPerson();
```

사용자 화면:

```text
사람을 등록합니다.
```

코드 언어와 사용자 인터페이스 언어를 구분합니다.

---

### 3.3 주석과 문서는 한글을 기본으로 한다

프로젝트를 혼자 개발하고 학습 목적으로도 사용하므로 주석과 프로젝트 문서는 한글을 기본으로 합니다.

```java
// 전화번호에서 숫자가 아닌 문자를 제거한다.
String normalizedPhone =
        phone.replaceAll("[^0-9]", "");
```

널리 사용되는 기술 용어는 영어를 함께 사용할 수 있습니다.

```java
// 데이터베이스 트랜잭션(Transaction)을 시작한다.
```

---

## 4. 명명 규칙

## 4.1 패키지 이름

패키지 이름은 소문자 단수형 또는 역할 이름을 사용합니다.

```text
config
model
repository
service
validator
normalizer
formatter
importer
ui.menu
ui.input
data.database
```

지양:

```text
Models
PersonServices
UI_Menu
databaseUtils
```

패키지 이름에 대문자, 밑줄, 복수형을 혼용하지 않습니다.

---

## 4.2 클래스 이름

클래스와 Record 이름은 `PascalCase`를 사용합니다.

```java
MainMenu
PersonService
PersonRepository
DatabaseInitializer
MenuInputReader
PersonCreate
```

역할을 이름에 포함합니다.

```text
PersonMenu
PersonInputReader
PersonValidator
PersonCsvImporter
```

모호한 이름은 피합니다.

지양:

```text
Manager
Helper
Processor
Util
Data
Handler
```

이러한 이름이 반드시 나쁜 것은 아니지만, 정확한 책임을 표현할 수 있을 때만 사용합니다.

예:

```text
PersonManager
```

보다는:

```text
PersonService
```

가 책임을 더 명확히 표현합니다.

---

## 4.3 인터페이스 이름

인터페이스를 도입할 경우 역할 자체를 이름으로 사용합니다.

```java
public interface PersonRepository {
}
```

구현체:

```java
public final class SqlitePersonRepository
        implements PersonRepository {
}
```

다음처럼 접두사 `I`를 붙이지 않습니다.

```java
IPersonRepository
```

현재 프로젝트 초기 단계에서는 구현체가 하나뿐이라면 인터페이스를 미리 만들지 않아도 됩니다.

---

## 4.4 메서드 이름

메서드는 `camelCase`를 사용하고 동사로 시작합니다.

```java
registerPerson()
findAll()
findById()
deleteById()
readChoice()
waitForEnter()
normalizePhone()
validateName()
formatPhone()
```

Boolean 반환 메서드는 질문 형태로 작성합니다.

```java
existsByPhone()
isValidPhone()
isActive()
hasAddress()
```

지양:

```java
person()
phoneCheck()
data()
runPerson()
personDelete()
```

---

## 4.5 CRUD 메서드 이름

Repository의 기본 CRUD 메서드는 다음 형태를 권장합니다.

```java
insert(PersonCreate person)
findById(long id)
findAll()
update(Person person)
deleteById(long id)
existsByPhone(String phone)
```

Service에서는 업무 의미가 드러나는 이름을 사용할 수 있습니다.

```java
register(PersonCreate person)
getPerson(long id)
getAllPeople()
changePersonInformation(PersonUpdate update)
removePerson(long id)
```

Repository는 데이터 처리 용어를 사용하고, Service는 업무 의미를 표현합니다.

---

## 4.6 변수 이름

변수는 `camelCase`를 사용합니다.

```java
String personName;
String normalizedPhone;
int genderId;
long generatedId;
List<Person> people;
```

가능하면 의미가 드러나는 이름을 사용합니다.

권장:

```java
PersonCreate personCreate;
Person savedPerson;
Connection connection;
PreparedStatement statement;
ResultSet resultSet;
```

지양:

```java
PersonCreate pc;
Person p;
Connection con;
PreparedStatement ps;
ResultSet rs;
```

반복문처럼 범위가 매우 짧은 경우에는 짧은 이름을 사용할 수 있습니다.

```java
for (Person person : people) {
    System.out.println(person.name());
}
```

---

## 4.7 컬렉션 이름

여러 값을 가진 변수는 복수형으로 작성합니다.

```java
List<Person> people;
List<String> errorMessages;
Map<Long, Person> peopleById;
```

지양:

```java
List<Person> person;
```

---

## 4.8 상수 이름

상수는 `UPPER_SNAKE_CASE`를 사용합니다.

```java
public static final String DB_URL = "...";
public static final String MENU_PROMPT = "...";
public static final int MIN_NAME_LENGTH = 2;
```

상수가 아닌 일반 변수에 대문자 이름을 사용하지 않습니다.

---

## 4.9 Boolean 변수 이름

Boolean 변수는 상태를 묻는 형태로 작성합니다.

```java
boolean running;
boolean registered;
boolean exists;
boolean confirmed;
boolean hasNextPage;
```

필요한 경우 `is`, `has`, `can`, `should`를 사용합니다.

```java
boolean isActive;
boolean hasPhone;
boolean canDelete;
boolean shouldContinue;
```

지양:

```java
boolean flag;
boolean check;
boolean result;
```

---

## 5. 파일 및 디렉토리 규칙

### 5.1 Java 파일 이름

파일 이름은 public 클래스 이름과 동일해야 합니다.

```text
PersonService.java
→ public final class PersonService
```

```text
PersonCreate.java
→ public record PersonCreate
```

---

### 5.2 소스와 데이터 분리

Java 소스와 업무 데이터를 분리합니다.

```text
src/                 Java 소스
data/database/       SQLite 데이터
data/input/          CSV 입력
docs/                설계 문서
```

데이터베이스 파일을 `src` 내부에 저장하지 않습니다.

---

### 5.3 패키지와 디렉토리 일치

패키지 선언과 폴더 구조는 일치해야 합니다.

파일:

```text
src/ui/menu/PersonMenu.java
```

패키지:

```java
package ui.menu;
```

---

## 6. 클래스 작성 규칙

### 6.1 가능한 경우 final을 사용한다

상속을 목적으로 설계하지 않은 클래스는 `final` 사용을 권장합니다.

```java
public final class PersonService {
}
```

이유:

* 불필요한 상속 방지
* 클래스 의도 명확화
* 동작 변경 가능성 감소

모든 클래스에 의무적으로 적용할 필요는 없지만, 현재 프로젝트의 Service, Repository, Menu, InputReader는 대부분 상속이 필요하지 않습니다.

---

### 6.2 필드는 private으로 선언한다

```java
private final PersonRepository personRepository;
```

지양:

```java
public PersonRepository personRepository;
```

외부에서 직접 필드를 변경하지 못하도록 합니다.

---

### 6.3 의존성 필드는 final로 선언한다

생성 후 변경할 필요가 없는 의존성은 `final`로 선언합니다.

```java
private final MenuInputReader menuInputReader;
private final PersonService personService;
```

생성자에서 주입합니다.

```java
public PersonMenu(
        MenuInputReader menuInputReader,
        PersonService personService
) {
    this.menuInputReader = menuInputReader;
    this.personService = personService;
}
```

---

### 6.4 클래스 내부에서 의존 객체를 직접 생성하지 않는다

지양:

```java
public final class PersonMenu {

    private final PersonService personService =
            new PersonService(
                    new PersonRepository()
            );
}
```

권장:

```java
public final class PersonMenu {

    private final PersonService personService;

    public PersonMenu(
            PersonService personService
    ) {
        this.personService = personService;
    }
}
```

객체 생성과 연결은 `Main`에서 담당합니다.

---

### 6.5 유틸리티 클래스

상태 없이 static 메서드와 상수만 제공하는 클래스는 객체 생성을 막습니다.

```java
public final class PersonFormatter {

    private PersonFormatter() {
    }

    public static String formatPhone(
            String phone
    ) {
        // ...
    }
}
```

적용 대상 예:

```text
UiConfig
PathConfig
DatabaseConfig
PersonFormatter
PersonNormalizer
PersonValidator
```

다만 의존성 주입이나 상태가 필요한 클래스는 static 유틸리티로 만들지 않습니다.

---

## 7. Record 사용 규칙

### 7.1 단순 데이터 전달 객체는 record를 우선 검토한다

데이터를 보관하고 전달하는 것이 주요 역할인 모델은 `record`가 적합합니다.

```java
public record PersonCreate(
        String name,
        String phone,
        int genderId,
        String address
) {
}
```

조회 모델:

```java
public record Person(
        long id,
        String name,
        String phone,
        int genderId,
        String address
) {
}
```

---

### 7.2 Record에 무거운 업무 로직을 넣지 않는다

Record에는 간단한 자기 검증이나 계산 메서드는 넣을 수 있지만, 데이터베이스 접근이나 서비스 업무를 넣지 않습니다.

지양:

```java
public record Person(...) {

    public void save() {
        DriverManager.getConnection(...);
    }
}
```

---

### 7.3 용도별 모델을 구분한다

등록과 조회 데이터가 다르면 별도 모델을 사용합니다.

```text
PersonCreate  등록용, ID 없음
Person        조회용, ID 있음
PersonUpdate  수정용
PersonDetail  상세 조회용
```

모든 기능을 하나의 거대한 `Person` 클래스에 넣지 않습니다.

단, 요구사항이 같다면 불필요하게 모델을 늘리지 않습니다.

---

## 8. 메서드 작성 규칙

### 8.1 하나의 메서드는 하나의 작업 단위를 담당한다

좋은 예:

```java
public void run() {
    while (true) {
        printMenu();
        int choice = readChoice();

        if (choice == 0) {
            return;
        }

        handleChoice(choice);
    }
}
```

다음 기능을 별도 메서드로 분리할 수 있습니다.

```java
private void printMenu();
private int readChoice();
private void handleChoice(int choice);
```

단, 메서드가 지나치게 짧고 한 번만 사용되며 오히려 흐름을 방해한다면 분리하지 않아도 됩니다.

---

### 8.2 메서드 길이

정확한 줄 수를 절대 기준으로 삼지는 않습니다.

다음 상황이면 분리를 검토합니다.

* 한 화면에 전체 메서드가 보이지 않음
* 여러 단계의 업무를 동시에 처리함
* 중첩된 조건문이 많음
* 메서드 이름만으로 내부 작업을 설명하기 어려움
* 일부 코드에 별도 이름을 붙일 수 있음
* 테스트하기 어려움

---

### 8.3 매개변수 수

매개변수가 너무 많아지면 모델 또는 요청 객체를 사용합니다.

지양:

```java
registerPerson(
        String name,
        String phone,
        int genderId,
        String address
);
```

권장:

```java
register(
        PersonCreate person
);
```

단순 메서드에 매개변수가 2~3개 있는 것은 문제가 아닙니다.

---

### 8.4 반환값을 활용한다

작업 성공 여부나 생성 결과가 필요한 경우 적절한 값을 반환합니다.

사람 등록:

```java
public long insert(PersonCreate person);
```

생성된 ID 반환:

```java
long personId =
        personRepository.insert(person);
```

삭제:

```java
public boolean deleteById(long id);
```

조회:

```java
public Optional<Person> findById(long id);
```

---

### 8.5 void를 남용하지 않는다

결과가 필요한 작업을 무조건 `void`로 처리하지 않습니다.

지양:

```java
public void insert(PersonCreate person) {
    // 저장 성공 여부를 알 수 없음
}
```

권장:

```java
public long insert(PersonCreate person) {
    // 생성된 ID 반환
}
```

---

### 8.6 조기 반환을 사용한다

중첩을 줄이기 위해 조건이 맞지 않으면 조기에 반환할 수 있습니다.

지양:

```java
if (person != null) {
    if (person.phone() != null) {
        if (!person.phone().isBlank()) {
            // 처리
        }
    }
}
```

권장:

```java
if (person == null) {
    return;
}

if (person.phone() == null
        || person.phone().isBlank()) {
    return;
}

// 처리
```

---

## 9. 코드 서식 규칙

### 9.1 들여쓰기

공백 4칸을 사용합니다.

탭과 공백을 혼용하지 않습니다.

```java
public void run() {
    while (true) {
        printMenu();
    }
}
```

IDE의 자동 서식 기능을 사용합니다.

IntelliJ IDEA:

```text
Ctrl + Alt + L
```

---

### 9.2 중괄호

여는 중괄호는 선언 또는 제어문과 같은 줄에 작성합니다.

```java
if (choice == 0) {
    return;
}
```

지양:

```java
if (choice == 0)
{
    return;
}
```

한 줄 조건문이라도 중괄호를 사용하는 것을 권장합니다.

권장:

```java
if (choice == 0) {
    return;
}
```

지양:

```java
if (choice == 0)
    return;
```

---

### 9.3 빈 줄

논리적인 코드 묶음 사이에 빈 줄을 사용합니다.

```java
String normalizedPhone =
        normalizePhone(person.phone());

validateName(person.name());
validatePhone(normalizedPhone);

PersonCreate normalizedPerson =
        new PersonCreate(
                person.name().trim(),
                normalizedPhone,
                person.genderId(),
                person.address()
        );

return personRepository.insert(
        normalizedPerson
);
```

빈 줄을 지나치게 많이 사용해 코드가 분리되어 보이지 않도록 합니다.

---

### 9.4 긴 줄

한 줄이 지나치게 길어지지 않도록 줄바꿈합니다.

권장 기준:

```text
한 줄 약 100~120자 이내
```

예:

```java
PersonRegistrationMenu registrationMenu =
        new PersonRegistrationMenu(
                menuInputReader,
                personInputReader,
                personService
        );
```

---

### 9.5 메서드 호출 줄바꿈

매개변수가 짧으면 한 줄로 작성할 수 있습니다.

```java
personService.register(personCreate);
```

매개변수가 많거나 길면 세로로 정렬합니다.

```java
PersonCreate person =
        new PersonCreate(
                name,
                phone,
                genderId,
                address
        );
```

---

## 10. 접근 제어 규칙

### 10.1 필요한 최소 범위만 공개한다

기본 기준:

```text
public     다른 패키지에서 사용해야 할 때
private    클래스 내부에서만 사용할 때
protected  상속 설계가 명확할 때만
package-private 같은 패키지 내부에만 공개할 때
```

대부분의 보조 메서드는 `private`으로 선언합니다.

```java
private void printMenu() {
}
```

---

### 10.2 모든 것을 public으로 만들지 않는다

지양:

```java
public void printMenu();
public void handleChoice();
public void printInvalidChoice();
public void createPrompt();
```

외부에서 직접 호출할 필요가 없는 메서드는 private으로 둡니다.

```java
public void run();

private void printMenu();
private void handleChoice(int choice);
```

---

## 11. Null 처리 규칙

### 11.1 필수값은 가능한 한 null을 허용하지 않는다

예:

```text
PersonCreate.name
PersonCreate.genderId
```

필수값이 null이면 생성 또는 Service 처리 단계에서 거부합니다.

---

### 11.2 선택값은 null 사용 기준을 명확히 한다

예:

```text
address
phone
```

선택 입력이 없는 경우 빈 문자열과 null을 혼용하지 않습니다.

권장:

```text
값 없음 → null
값 있음 → trim한 문자열
```

---

### 11.3 Optional 사용 범위

조회 결과가 없을 수 있는 경우 `Optional`을 사용할 수 있습니다.

```java
public Optional<Person> findById(long id);
```

지양:

* 필드 타입으로 무조건 Optional 사용
* 메서드 매개변수에 Optional 사용
* Collection 자체를 Optional로 감싸기

목록 조회 결과가 없으면 빈 리스트를 반환합니다.

```java
public List<Person> findAll();
```

결과 없음:

```java
return List.of();
```

다음은 지양합니다.

```java
return null;
```

---

## 12. 문자열 처리 규칙

### 12.1 사용자 입력은 trim한다

```java
String name =
        scanner.nextLine().trim();
```

공백이 의미 있는 입력은 예외로 판단합니다.

---

### 12.2 문자열 비교는 equals를 사용한다

권장:

```java
if ("0".equals(input)) {
    return;
}
```

또는 null이 없다고 확실한 경우:

```java
if (input.equals("0")) {
    return;
}
```

지양:

```java
if (input == "0") {
}
```

`==`는 문자열 내용이 아니라 참조를 비교합니다.

---

### 12.3 빈 문자열 검사

권장:

```java
if (name == null || name.isBlank()) {
}
```

`isBlank()`는 빈 문자열뿐 아니라 공백만 있는 문자열도 검사합니다.

```text
""
" "
"   "
```

---

### 12.4 Text Block 사용

여러 줄 SQL이나 긴 출력 문구는 Java Text Block을 사용할 수 있습니다.

```java
String sql = """
        SELECT
            id,
            name,
            phone,
            gender_id,
            address
        FROM person
        ORDER BY id
        """;
```

문자열 연결보다 읽기 쉽도록 작성합니다.

---

## 13. 입력 처리 규칙

### 13.1 Scanner는 하나만 사용한다

`Main`에서 하나의 Scanner를 생성하고 모든 InputReader에 전달합니다.

```java
try (Scanner scanner = new Scanner(System.in)) {
    MenuInputReader menuInputReader =
            new MenuInputReader(scanner);

    PersonInputReader personInputReader =
            new PersonInputReader(scanner);
}
```

각 클래스에서 새로운 Scanner를 만들지 않습니다.

---

### 13.2 하위 클래스에서 Scanner를 닫지 않는다

`System.in`을 사용하는 Scanner는 Main에서만 종료합니다.

지양:

```java
public void close() {
    scanner.close();
}
```

---

### 13.3 모든 입력은 nextLine을 기본으로 한다

`nextInt()`와 `nextLine()` 혼용 문제를 피하기 위해 문자열로 입력받은 뒤 변환합니다.

```java
String input =
        scanner.nextLine().trim();

try {
    return Integer.parseInt(input);
} catch (NumberFormatException error) {
    System.out.println(
            "숫자로 입력해 주세요."
    );
}
```

---

### 13.4 사용자 입력 오류는 재입력시킨다

잘못된 입력 때문에 프로그램을 종료하지 않습니다.

```java
while (true) {
    String input =
            scanner.nextLine().trim();

    try {
        return Integer.parseInt(input);
    } catch (NumberFormatException error) {
        System.out.println(
                "숫자로 입력해 주세요."
        );
    }
}
```

---

## 14. UI 코딩 규칙

### 14.1 메뉴 클래스는 run 메서드를 진입점으로 한다

```java
public void run() {
}
```

상위 메뉴는 하위 메뉴의 `run()`을 호출합니다.

```java
personMenu.run();
```

---

### 14.2 0번의 의미

```text
MainMenu              0 = 프로그램 종료
하위 메뉴             0 = 이전 메뉴로 복귀
등록·수정·삭제 확인   0 = 취소
```

하위 메뉴에서 `System.exit()`를 사용하지 않습니다.

---

### 14.3 결과 출력 후 Enter 대기

기능 실행 결과는 사용자가 읽을 수 있도록 Enter 입력을 기다립니다.

```java
System.out.println(
        "사람 등록이 완료되었습니다."
);

menuInputReader.waitForEnter();
```

고정 시간 대기를 사용하지 않습니다.

지양:

```java
Thread.sleep(3000);
```

---

### 14.4 준비 중 기능 처리

아직 구현되지 않은 메뉴는 명확히 안내합니다.

```java
System.out.println(
        "공사 관리 기능은 준비 중입니다."
);

menuInputReader.waitForEnter();
```

비어 있는 메서드나 아무 반응이 없는 메뉴를 만들지 않습니다.

---

### 14.5 사용자에게 내부 예외를 그대로 출력하지 않는다

지양:

```java
System.out.println(error.getMessage());
```

`SQLException`의 원문이 사용자에게 그대로 노출될 수 있습니다.

권장:

```java
System.out.println(
        "사람 정보를 저장하지 못했습니다."
);
```

개발 단계에서는 별도로 로그나 스택 트레이스를 확인합니다.

---

## 15. Service 코딩 규칙

### 15.1 업무 규칙은 Service에 둔다

예:

* 전화번호 중복 확인
* 값 정규화
* 이름 검증
* 성별 ID 검증
* 삭제 가능 여부 판단
* 여러 Repository 작업 조정

```java
public long register(
        PersonCreate person
) {
    String normalizedPhone =
            PersonNormalizer.normalizePhone(
                    person.phone()
            );

    PersonValidator.validateName(
            person.name()
    );

    PersonValidator.validatePhone(
            normalizedPhone
    );

    if (personRepository.existsByPhone(
            normalizedPhone
    )) {
        throw new DuplicatePhoneException(
                "이미 등록된 전화번호입니다."
        );
    }

    PersonCreate normalizedPerson =
            new PersonCreate(
                    person.name().trim(),
                    normalizedPhone,
                    person.genderId(),
                    normalizeAddress(
                            person.address()
                    )
            );

    return personRepository.insert(
            normalizedPerson
    );
}
```

---

### 15.2 Service에서 사용자 입력을 받지 않는다

지양:

```java
public void register() {
    Scanner scanner =
            new Scanner(System.in);

    String name =
            scanner.nextLine();
}
```

Service는 이미 구성된 데이터를 전달받습니다.

```java
public long register(
        PersonCreate person
);
```

---

### 15.3 Service에서 화면 출력하지 않는다

지양:

```java
System.out.println(
        "사람 등록 성공"
);
```

Service는 결과를 반환하거나 예외를 발생시킵니다.

UI가 결과를 출력합니다.

---

## 16. Repository 코딩 규칙

### 16.1 SQL은 Repository에서만 실행한다

```text
PersonRepository
ProjectRepository
WorkSiteRepository
```

Menu나 Service에서 직접 SQL을 작성하지 않습니다.

---

### 16.2 PreparedStatement를 사용한다

권장:

```java
String sql = """
        INSERT INTO person (
            name,
            phone,
            gender_id,
            address
        )
        VALUES (?, ?, ?, ?)
        """;

try (
        Connection connection =
                DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql)
) {
    statement.setString(1, person.name());
    statement.setString(2, person.phone());
    statement.setInt(3, person.genderId());
    statement.setString(4, person.address());

    statement.executeUpdate();
}
```

지양:

```java
String sql =
        "INSERT INTO person VALUES ('"
        + person.name()
        + "')";
```

---

### 16.3 SQL 컬럼을 명시한다

권장:

```sql
INSERT INTO person (
    name,
    phone,
    gender_id,
    address
)
VALUES (?, ?, ?, ?)
```

지양:

```sql
INSERT INTO person
VALUES (?, ?, ?, ?, ?)
```

테이블 컬럼 순서가 변경될 때 오류가 발생할 수 있습니다.

---

### 16.4 SELECT * 사용을 피한다

권장:

```sql
SELECT
    id,
    name,
    phone,
    gender_id,
    address
FROM person
```

지양:

```sql
SELECT *
FROM person
```

필요한 컬럼과 Java 매핑 관계를 명확히 합니다.

---

### 16.5 자원은 try-with-resources로 관리한다

```java
try (
        Connection connection =
                DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        ResultSet resultSet =
                statement.executeQuery()
) {
    // 결과 처리
}
```

수동으로 close를 반복하지 않습니다.

---

### 16.6 ResultSet 매핑은 별도 메서드로 분리할 수 있다

```java
private Person mapPerson(
        ResultSet resultSet
) throws SQLException {

    return new Person(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getInt("gender_id"),
            resultSet.getString("address")
    );
}
```

여러 조회 메서드에서 같은 매핑 코드를 반복하지 않습니다.

---

## 17. 데이터베이스 규칙

### 17.1 테이블과 컬럼은 snake_case를 사용한다

```text
person
gender
work_site
daily_work
gender_id
created_at
```

---

### 17.2 테이블 이름은 단수형을 사용한다

```text
person
project
organization
```

복수형과 단수형을 혼용하지 않습니다.

---

### 17.3 기본키는 id로 통일한다

```sql
id INTEGER PRIMARY KEY AUTOINCREMENT
```

---

### 17.4 외래키는 참조 테이블명_id 형식을 사용한다

```text
gender_id
person_id
project_id
work_site_id
```

---

### 17.5 외래키를 활성화한다

SQLite 연결을 생성할 때 다음 설정을 적용합니다.

```sql
PRAGMA foreign_keys = ON;
```

---

### 17.6 실제 데이터 파일은 Git에 올리지 않는다

`.gitignore`:

```gitignore
data/database/*.db
data/input/*.csv
backup/
```

개인정보가 들어간 데이터베이스와 CSV 파일을 GitHub에 업로드하지 않습니다.

---

## 18. 예외 처리 규칙

### 18.1 예외를 무조건 잡지 않는다

처리할 수 없는 예외를 잡고 무시하지 않습니다.

지양:

```java
try {
    personRepository.insert(person);
} catch (Exception error) {
}
```

이 경우 오류가 발생했지만 프로그램에서는 성공한 것처럼 보일 수 있습니다.

---

### 18.2 가장 구체적인 예외를 잡는다

지양:

```java
catch (Exception error) {
}
```

권장:

```java
catch (SQLException error) {
}
```

사용자 숫자 입력:

```java
catch (NumberFormatException error) {
}
```

---

### 18.3 예외 원인을 보존한다

새로운 예외로 변환할 때 원래 예외를 전달합니다.

```java
catch (SQLException error) {
    throw new DataAccessException(
            "사람 등록에 실패했습니다.",
            error
    );
}
```

지양:

```java
catch (SQLException error) {
    throw new RuntimeException(
            "오류"
    );
}
```

원인이 사라져 디버깅이 어려워집니다.

---

### 18.4 사용자 오류와 시스템 오류를 구분한다

사용자 오류:

```text
이름이 비어 있음
전화번호 형식 오류
중복 전화번호
존재하지 않는 ID
```

시스템 오류:

```text
DB 연결 실패
SQL 문법 오류
파일 읽기 실패
권한 오류
```

사용자 오류는 이해 가능한 안내를 제공하고, 시스템 오류는 로그와 사용자 메시지를 분리합니다.

---

### 18.5 예외 메시지는 구체적으로 작성한다

지양:

```text
오류가 발생했습니다.
```

권장:

```text
이미 등록된 전화번호입니다.
존재하지 않는 사람 ID입니다.
CSV 파일을 찾을 수 없습니다.
데이터베이스 연결에 실패했습니다.
```

---

## 19. 검증·정규화·출력 규칙

### 19.1 처리 순서

```text
입력
→ 정규화
→ 검증
→ 저장
```

조회:

```text
DB 값
→ Formatter
→ 사용자 출력
```

---

### 19.2 Normalizer

값을 표준 형식으로 변경합니다.

```java
public static String normalizePhone(
        String phone
) {
    return phone.replaceAll(
            "[^0-9]",
            ""
    );
}
```

---

### 19.3 Validator

값이 규칙에 맞는지 검사합니다.

```java
public static void validatePhone(
        String phone
) {
    if (!phone.matches("010\\d{8}")) {
        throw new IllegalArgumentException(
                "전화번호는 010으로 시작하는 "
                        + "11자리여야 합니다."
        );
    }
}
```

Validator는 값을 수정하지 않습니다.

---

### 19.4 Formatter

저장된 값을 사용자에게 보여줄 형식으로 변환합니다.

```java
public static String formatPhone(
        String phone
) {
    return phone.substring(0, 3)
            + "-"
            + phone.substring(3, 7)
            + "-"
            + phone.substring(7);
}
```

Formatter는 DB 값을 변경하지 않습니다.

---

## 20. 주석 작성 규칙

### 20.1 코드로 알 수 있는 내용을 반복하지 않는다

지양:

```java
// personName 변수에 이름을 저장한다.
String personName = inputName();
```

코드 자체로 충분히 알 수 있습니다.

---

### 20.2 왜 그렇게 했는지를 설명한다

권장:

```java
// nextInt()와 nextLine()을 섞을 때 발생하는
// 개행 문자 문제를 피하기 위해 문자열로 읽은 후 변환한다.
String input = scanner.nextLine();
```

---

### 20.3 임시 코드에는 TODO를 사용한다

```java
// TODO: CSV 등록 기능 구현 후 PersonService와 연결한다.
```

TODO에는 가능하면 해야 할 작업을 구체적으로 적습니다.

지양:

```java
// TODO
```

완료된 TODO는 제거합니다.

---

### 20.4 주석으로 사용하지 않는 코드를 보관하지 않는다

지양:

```java
// PersonService service = new PersonService();
// service.register(person);
```

사용하지 않는 코드는 Git 이력에서 확인할 수 있으므로 삭제합니다.

---

## 21. JavaDoc 규칙

### 21.1 모든 메서드에 JavaDoc을 강제하지 않는다

이름만으로 역할이 명확한 private 메서드에는 JavaDoc이 필요하지 않습니다.

```java
private void printMenu() {
}
```

---

### 21.2 공개 API나 복잡한 규칙에는 JavaDoc을 작성한다

```java
/**
 * 신규 사람 정보를 등록하고 생성된 ID를 반환한다.
 *
 * @param person 등록할 사람 정보
 * @return 데이터베이스에서 생성된 사람 ID
 * @throws DuplicatePhoneException
 *         같은 전화번호가 이미 등록된 경우
 */
public long register(
        PersonCreate person
) {
}
```

---

### 21.3 JavaDoc은 코드와 함께 수정한다

코드와 맞지 않는 오래된 JavaDoc은 없는 것보다 해롭습니다.

메서드 동작이 변경되면 설명도 함께 수정합니다.

---

## 22. Import 규칙

### 22.1 사용하지 않는 import는 제거한다

IDE의 import 정리 기능을 사용합니다.

IntelliJ IDEA:

```text
Ctrl + Alt + O
```

---

### 22.2 와일드카드 import를 사용하지 않는다

지양:

```java
import java.util.*;
```

권장:

```java
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
```

어떤 클래스를 사용하는지 명확히 합니다.

---

### 22.3 Import 순서

일반적으로 IDE 기본 정렬을 사용합니다.

```text
java.*
javax.*
외부 라이브러리
프로젝트 패키지
```

프로젝트에서 임의의 import 정렬 방식을 별도로 만들지 않습니다.

---

## 23. Collection 규칙

### 23.1 구현체보다 인터페이스 타입으로 선언한다

권장:

```java
List<Person> people =
        new ArrayList<>();
```

지양:

```java
ArrayList<Person> people =
        new ArrayList<>();
```

구체적인 구현체 기능이 필요한 경우는 예외입니다.

---

### 23.2 빈 컬렉션을 반환한다

권장:

```java
return List.of();
```

지양:

```java
return null;
```

호출자가 매번 null을 검사하지 않도록 합니다.

---

### 23.3 외부 변경을 막아야 하면 불변 컬렉션을 사용한다

```java
return List.copyOf(people);
```

현재 단순 Repository 조회에서는 일반 List를 반환해도 되지만, 객체 상태 보호가 필요하면 불변 컬렉션을 검토합니다.

---

## 24. 숫자와 금액 규칙

### 24.1 의미 없는 숫자를 코드에 직접 작성하지 않는다

지양:

```java
if (name.length() < 2) {
}
```

규칙이 여러 곳에서 사용된다면 상수로 분리합니다.

```java
private static final int MIN_NAME_LENGTH = 2;
```

한 번만 사용되고 의미가 명확하면 직접 사용해도 됩니다.

---

### 24.2 금액은 double을 사용하지 않는다

향후 계약금액, 급여, 자재비 등 금액 계산에는 `double`을 사용하지 않습니다.

권장 후보:

```text
정수 원 단위 → long
소수 금액 또는 정밀 계산 → BigDecimal
```

예:

```java
long contractAmount;
BigDecimal rate;
```

부동소수점 오차를 피하기 위한 원칙입니다.

---

### 24.3 ID 자료형

Java 객체의 데이터베이스 ID는 `long` 사용을 권장합니다.

```java
long personId;
```

SQLite `INTEGER PRIMARY KEY`는 64비트 정수와 연결됩니다.

---

## 25. 날짜와 시간 규칙

Java 날짜·시간 API를 사용합니다.

```java
LocalDate
LocalDateTime
Instant
ZonedDateTime
```

지양:

```java
java.util.Date
```

예:

```java
LocalDate workDate;
LocalDate contractStartDate;
```

시간대가 중요한 경우:

```java
ZoneId.of("Asia/Seoul");
```

문자열을 날짜 대신 장기간 직접 조작하지 않습니다.

---

## 26. 로깅 규칙

초기 학습 단계에서는 `System.out.println()`과 `System.err.println()`으로 시작할 수 있습니다.

구분:

```text
System.out   사용자에게 보여줄 정상 출력
System.err   오류 또는 개발 확인 출력
```

프로젝트가 커지면 로깅 프레임워크 도입을 검토합니다.

예:

```text
SLF4J
Logback
java.util.logging
```

DB 예외와 CSV 오류가 많아질 때 도입하는 것이 적절합니다.

사용자 메시지와 개발 로그를 분리합니다.

```text
사용자:
사람 등록에 실패했습니다.

개발 로그:
SQLException: UNIQUE constraint failed...
```

---

## 27. 테스트 규칙

### 27.1 하나의 테스트는 하나의 동작을 확인한다

예:

```text
전화번호 하이픈이 제거되는가
전화번호가 010으로 시작하지 않으면 실패하는가
중복 전화번호 등록이 거부되는가
존재하지 않는 ID 조회가 Optional.empty를 반환하는가
```

---

### 27.2 테스트 메서드 이름은 동작을 표현한다

예:

```java
void normalizePhoneRemovesHyphens() {
}
```

또는 한글 메서드 이름을 사용할 수 있지만, 프로젝트 코드 규칙과 일관성을 위해 영어를 권장합니다.

```java
void registerThrowsWhenPhoneAlreadyExists() {
}
```

---

### 27.3 실제 업무 DB로 테스트하지 않는다

테스트 DB:

```text
data/database/test_database.db
```

또는:

```text
jdbc:sqlite::memory:
```

실제 데이터가 있는 `database.db`에 테스트 데이터를 넣지 않습니다.

---

## 28. Git 규칙

### 28.1 하나의 커밋은 하나의 의미 있는 변경을 담는다

권장 커밋:

```text
사람 등록 메뉴 연결
SQLite 연결 클래스 추가
사람 테이블 초기화 구현
전화번호 정규화 추가
사람 조회 기능 구현
```

지양:

```text
여러 기능 추가 및 수정
작업함
코드 변경
최종
진짜 최종
```

---

### 28.2 커밋 메시지 형식

현재 프로젝트는 한글 커밋 메시지를 사용해도 됩니다.

권장:

```text
사람 등록 하위 메뉴 연결
MenuInputReader 입력 오류 처리 추가
PersonRepository 사람 등록 구현
전화번호 정규화 및 검증 추가
```

선택적으로 접두사를 사용할 수 있습니다.

```text
feat: 사람 등록 기능 추가
fix: 메뉴 입력 후 개행 문제 수정
refactor: PersonMenu 의존성 생성자 주입
docs: 데이터베이스 설계 문서 추가
test: 전화번호 검증 테스트 추가
```

접두사를 사용하기 시작하면 일관되게 유지합니다.

---

### 28.3 커밋 전 확인

```text
프로그램이 컴파일되는가

기존 기능이 정상 작동하는가

불필요한 파일이 포함되지 않았는가

실제 DB나 개인정보 CSV가 포함되지 않았는가

사용하지 않는 import가 남아 있지 않은가

디버깅용 출력이 남아 있지 않은가
```

---

### 28.4 큰 변경 전 커밋

리팩터링이나 구조 변경 전 현재 정상 상태를 먼저 커밋합니다.

```text
정상 작동 상태 커밋
→ 리팩터링
→ 테스트
→ 리팩터링 결과 커밋
```

문제가 생겼을 때 쉽게 되돌릴 수 있습니다.

---

## 29. 리팩터링 규칙

### 29.1 기능 변경과 구조 변경을 가능하면 분리한다

지양:

```text
사람 등록 기능 추가
+ 패키지 전체 이동
+ 이름 전부 변경
+ DB 구조 변경
```

권장:

```text
1. 기존 코드 이름 정리
2. 정상 작동 확인 및 커밋
3. 사람 등록 기능 구현
4. 정상 작동 확인 및 커밋
```

---

### 29.2 중복이 보이면 즉시 판단하되 무조건 분리하지 않는다

같은 코드가 두 곳에 생겼다면 다음을 확인합니다.

```text
정말 같은 책임인가?

앞으로 함께 변경될 가능성이 높은가?

공통화하면 이름이 명확해지는가?

공통화 때문에 의존성이 더 복잡해지지 않는가?
```

두 코드가 우연히 비슷하지만 의미가 다르면 분리 유지할 수 있습니다.

---

### 29.3 IDE 리팩터링 기능을 사용한다

파일명, 클래스명, 메서드명 변경 시 단순 문자열 수정보다 IDE의 Rename 기능을 사용합니다.

IntelliJ IDEA:

```text
Shift + F6
```

참조 누락을 줄일 수 있습니다.

---

### 29.4 리팩터링 후 반드시 실행한다

최소 확인 흐름:

```text
프로그램 실행
→ 메인 메뉴
→ 사람 메뉴
→ 하위 메뉴
→ 이전 메뉴 복귀
→ 프로그램 종료
```

DB 관련 변경이면 등록과 조회까지 확인합니다.

---

## 30. AI 생성 코드 사용 규칙

AI가 생성한 코드는 바로 프로젝트에 붙여 넣고 완료된 것으로 판단하지 않습니다.

반드시 다음 내용을 확인합니다.

```text
현재 패키지 구조와 맞는가?

존재하지 않는 클래스를 가정하고 있지 않은가?

Scanner를 새로 만들고 있지 않은가?

UI에서 SQL을 실행하고 있지 않은가?

Repository가 화면 출력을 하고 있지 않은가?

현재 Java 버전에서 지원되는 문법인가?

예외를 무시하고 있지 않은가?

불필요한 프레임워크를 추가하지 않았는가?

기존 코드와 중복되는 기능이 없는가?

코드 각 줄의 의미를 이해할 수 있는가?
```

한 번에 전체 프로젝트 변경을 요청하기보다 작은 범위로 요청합니다.

권장 요청:

```text
PersonMenu에서 1번 선택 시
PersonRegistrationMenu.run()이 호출되게 수정하라.

다른 파일은 수정하지 마라.
```

지양:

```text
전체 ERP 시스템을 완성해라.
```

---

## 31. 금지 또는 지양 사항

다음 방식은 특별한 이유가 없다면 사용하지 않습니다.

### 구조

* Menu에서 SQL 실행
* Repository에서 사용자 입력
* Service에서 화면 출력
* 하위 메뉴에서 `System.exit()`
* 클래스 내부에서 의존 객체 무조건 생성
* 하나의 클래스에 등록·조회·수정·삭제·CSV·SQL 모두 작성

### Java

* 문자열 비교에 `==` 사용
* 사용하지 않는 import 방치
* `catch (Exception)` 남용
* 예외를 잡고 아무 처리도 하지 않음
* 빈 컬렉션 대신 null 반환
* 금액 계산에 double 사용
* `nextInt()`와 `nextLine()` 무분별한 혼용
* 의미 없는 `Manager`, `Helper`, `Util` 이름 남용
* 주석 처리된 오래된 코드 보관

### 데이터베이스

* 문자열 연결로 SQL 생성
* `SELECT *` 남용
* 실제 DB를 테스트에 사용
* 실제 DB와 CSV를 GitHub에 업로드
* 초기화 과정에서 기존 데이터 삭제
* 외래키 활성화 누락

### UI

* 결과 메시지 출력 직후 메뉴 재출력
* 고정 대기시간으로 메시지 표시
* 사용자에게 SQL 오류 원문 노출
* 삭제 전 확인 없이 즉시 삭제
* 아무 반응 없는 미구현 메뉴

---

## 32. 코드 리뷰 확인표

새로운 코드를 작성하거나 AI에게 코드를 받은 후 다음 내용을 확인합니다.

### 책임

```text
[ ] 코드가 올바른 계층에 있는가?
[ ] 클래스가 한 가지 핵심 책임을 가지는가?
[ ] UI와 업무 로직이 분리되어 있는가?
[ ] SQL은 Repository에만 있는가?
```

### 이름

```text
[ ] 클래스 이름이 역할을 표현하는가?
[ ] 메서드가 동사로 시작하는가?
[ ] Boolean 이름이 질문 형태인가?
[ ] 축약어를 과도하게 사용하지 않았는가?
```

### 입력과 오류

```text
[ ] 잘못된 입력으로 프로그램이 종료되지 않는가?
[ ] 사용자 오류와 시스템 오류가 구분되는가?
[ ] 예외를 무시하지 않는가?
[ ] 결과 메시지를 읽을 수 있는가?
```

### 데이터베이스

```text
[ ] PreparedStatement를 사용하는가?
[ ] try-with-resources를 사용하는가?
[ ] SQL 컬럼을 명시했는가?
[ ] 기존 데이터가 손상되지 않는가?
```

### 유지보수

```text
[ ] 같은 코드가 불필요하게 반복되지 않는가?
[ ] 현재 필요하지 않은 구조를 과도하게 만들지 않았는가?
[ ] 메서드가 너무 많은 일을 하지 않는가?
[ ] 수정 범위를 예측할 수 있는가?
```

### 완료 전

```text
[ ] 코드가 컴파일되는가?
[ ] 실제 메뉴를 직접 실행해 보았는가?
[ ] 이전 기능도 정상 작동하는가?
[ ] 개인정보 파일이 Git에 포함되지 않았는가?
[ ] 관련 문서를 수정했는가?
```

---

## 33. 현재 프로젝트 적용 우선순위

모든 규칙을 한 번에 완벽하게 적용하려 하지 않습니다.

현재 우선 적용할 규칙은 다음과 같습니다.

### 1순위: 책임 분리

```text
Menu
Service
Repository
Model
```

각 계층의 역할을 섞지 않습니다.

---

### 2순위: 의존성 연결

```text
Main에서 객체 생성
→ 생성자 주입
→ 내부 new 최소화
```

---

### 3순위: 입력 안정성

```text
Scanner 하나
nextLine 기반 입력
잘못된 입력 재처리
결과 후 Enter 대기
```

---

### 4순위: 데이터베이스 안전성

```text
PreparedStatement
try-with-resources
외래키 활성화
실제 DB Git 제외
```

---

### 5순위: 이름과 코드 정리

```text
영어 식별자
명확한 클래스 역할
camelCase
PascalCase
UPPER_SNAKE_CASE
```

---

### 6순위: 테스트와 문서화

```text
기능별 수동 테스트
핵심 규칙 단위 테스트
설계 문서 갱신
변경 사항 커밋
```

---

## 34. 규칙 변경 기준

이 문서는 프로젝트 진행에 따라 수정할 수 있습니다.

규칙을 변경할 수 있는 경우:

* 현재 규칙이 실제 개발을 불필요하게 어렵게 만듦
* Java 버전 또는 기술 스택이 변경됨
* 같은 문제가 반복적으로 발생함
* 프로젝트 규모가 커져 새로운 기준이 필요함
* 팀원이 추가되어 협업 기준이 필요함

규칙 변경 시 다음을 확인합니다.

```text
왜 변경하는가?

기존 코드에 어떤 영향을 주는가?

모든 코드에 즉시 적용할 것인가?

새로 작성하는 코드부터 적용할 것인가?

관련 문서를 함께 수정했는가?
```

---

## 35. 최종 코딩 원칙

NGSP_JAVA는 다음 원칙을 중심으로 개발합니다.

```text
코드는 작동하는 것만으로 충분하지 않다.

각 클래스의 책임을 명확히 한다.

UI는 사용자와 대화한다.

Service는 업무를 판단한다.

Repository는 데이터를 저장하고 조회한다.

Model은 데이터를 표현한다.

Main은 객체를 만들고 연결한다.

코드 식별자는 영어로 작성한다.

사용자 메시지는 한글로 작성한다.

이름으로 코드의 역할을 설명한다.

예외를 숨기지 않고 적절한 계층에서 처리한다.

중복 코드는 제거하되 과도하게 분리하지 않는다.

실제 데이터와 개인정보를 Git에 올리지 않는다.

작은 기능을 완성하고 확인한 후 다음 단계로 이동한다.

이해하지 못한 코드는 프로젝트에 추가하지 않는다.

구조 변경 전에는 정상 상태를 커밋한다.

코드와 설계 문서는 함께 갱신한다.
```

이 규칙의 최종 목적은 코드를 형식적으로 아름답게 만드는 것이 아닙니다.

```text
기능이 늘어나도 구조를 이해할 수 있고,

오류가 발생했을 때 원인을 찾을 수 있으며,

기존 기능을 망가뜨리지 않고 수정할 수 있고,

몇 달 뒤 다시 보아도 개발을 이어갈 수 있는 코드
```

를 만드는 것이 목표입니다.
