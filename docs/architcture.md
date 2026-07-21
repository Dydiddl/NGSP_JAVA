# NGSP_JAVA 아키텍처 설계

## 1. 문서 개요

### 1.1 문서 목적

이 문서는 `NGSP_JAVA` 프로젝트의 전체 아키텍처와 각 계층의 책임을 정의합니다.

프로젝트가 확장되더라도 다음 문제가 발생하지 않도록 공통 기준을 정하는 것이 목적입니다.

* UI 클래스에 데이터베이스 코드가 들어가는 문제
* 같은 검증 코드가 여러 파일에 중복되는 문제
* 메뉴와 기능 구현이 강하게 결합되는 문제
* 사람, 공사, 현장 등의 기능이 서로 뒤섞이는 문제
* 프로젝트 규모가 커질수록 수정 범위가 예측되지 않는 문제

이 문서는 현재 구현된 구조를 설명하는 것뿐만 아니라, 앞으로 기능을 추가할 때 따라야 할 설계 기준을 제시합니다.

---

## 2. 프로젝트 개요

### 2.1 프로젝트명

```text
NGSP_JAVA
Namgang Landscape System Project - Java Application
```

### 2.2 프로젝트 목표

NGSP_JAVA는 남강조경의 회사 업무를 통합 관리하기 위한 Java 기반 프로그램입니다.

초기에는 터미널에서 작동하는 CLI 프로그램으로 개발하며, 향후 필요에 따라 GUI 또는 웹 기반 시스템으로 확장할 수 있는 구조를 목표로 합니다.

주요 관리 대상은 다음과 같습니다.

* 사람
* 근로자
* 회사 및 조직
* 공사
* 현장
* 작업 기록
* 출근 기록
* 임금 및 급여
* 계약
* 서류
* 자재
* 장비

### 2.3 기본 기술

```text
Language        Java
User Interface  CLI
Database        SQLite
Database API    JDBC
Input File      CSV
Build Tool      추후 결정
Version Control Git / GitHub
```

---

## 3. 설계 원칙

### 3.1 단일 책임 원칙

각 클래스는 하나의 핵심 책임을 가져야 합니다.

예를 들어 `PersonMenu`는 사람 관리 메뉴를 출력하고 사용자 선택을 처리합니다. 사람을 데이터베이스에 저장하는 SQL은 담당하지 않습니다.

```text
Menu        화면 출력과 흐름 제어
Input       사용자 입력 수집
Service     업무 규칙 처리
Repository  데이터베이스 접근
Model       데이터 표현
Config      설정값 관리
```

하나의 클래스가 메뉴 출력, 입력 검증, SQL 실행까지 모두 담당하지 않도록 합니다.

---

### 3.2 계층 간 의존 방향

상위 계층은 하위 계층을 사용할 수 있지만, 하위 계층은 상위 계층을 알지 못하게 합니다.

```text
UI
 ↓
Service
 ↓
Repository
 ↓
Database
```

예를 들어 `PersonRepository`는 `PersonMenu`를 알지 못합니다.

Repository가 화면에 메시지를 출력하거나 사용자 입력을 요청해서는 안 됩니다.

---

### 3.3 UI와 업무 로직 분리

UI는 사용자에게 정보를 보여주고 입력을 전달하는 역할만 합니다.

잘못된 예:

```java
public void registerPerson() {
    String sql = "INSERT INTO person ...";
    Connection connection = DriverManager.getConnection(...);
}
```

위 코드가 메뉴 클래스 안에 있다면 UI와 데이터베이스가 결합됩니다.

권장 흐름:

```text
PersonRegistrationMenu
→ PersonInputReader
→ PersonService
→ PersonRepository
→ SQLite
```

---

### 3.4 데이터 입력 방식과 저장 방식 분리

사람을 등록하는 입력 방식은 여러 가지가 될 수 있습니다.

```text
콘솔 직접 입력
CSV 파일 입력
엑셀 파일 입력
웹 화면 입력
외부 API 입력
```

그러나 입력 이후의 등록 절차는 하나로 통합되어야 합니다.

```text
직접 입력 ─┐
CSV 입력 ──┼→ PersonCreate → PersonService → PersonRepository
엑셀 입력 ─┘
```

입력 방식마다 별도의 SQL 저장 코드를 만들지 않습니다.

---

### 3.5 작은 기능 단위로 완성

프로젝트 전체 기능을 동시에 개발하지 않습니다.

하나의 기능을 다음 계층까지 완전히 연결한 후 다음 기능으로 이동합니다.

```text
메뉴
→ 입력
→ 검증
→ 서비스
→ 저장소
→ 데이터베이스
→ 결과 출력
```

사람 등록 기능을 완성한 뒤 사람 조회를 구현하고, 그다음 수정과 삭제를 구현합니다.

---

### 3.6 필요할 때 분리

처음부터 모든 기능을 작은 클래스로 과도하게 분리하지 않습니다.

처음에는 Service 내부의 private 메서드로 관리할 수 있습니다.

```java
private String normalizePhone(String phone) {
    return phone.replaceAll("[^0-9]", "");
}
```

같은 기능이 여러 곳에서 반복 사용되거나 책임이 커질 때 별도 클래스로 분리합니다.

```text
PersonValidator
PersonNormalizer
PersonFormatter
```

---

## 4. 전체 아키텍처

### 4.1 논리 계층

NGSP_JAVA는 다음 계층으로 구성합니다.

```text
┌──────────────────────────────────┐
│          Application Entry       │
│               Main               │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│             UI Layer             │
│ Menu / Input / Output Formatting │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│           Service Layer          │
│ Validation / Business Rules      │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│         Repository Layer         │
│ SQL / Data Mapping / CRUD        │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│             Data Layer           │
│       SQLite / CSV / Files       │
└──────────────────────────────────┘
```

---

### 4.2 전체 실행 흐름

```text
사용자
  ↓
MainMenu
  ↓
도메인 메뉴
  ↓
세부 기능 메뉴
  ↓
InputReader
  ↓
Create 또는 Request 모델
  ↓
Service
  ↓
Repository
  ↓
SQLite
```

조회 기능은 반대 방향으로 결과가 반환됩니다.

```text
SQLite
  ↓
Repository
  ↓
Service
  ↓
List<Model>
  ↓
Menu 또는 Formatter
  ↓
사용자 출력
```

---

## 5. 패키지 구조

현재 및 향후 권장 패키지 구조는 다음과 같습니다.

```text
src/
├── Main.java
│
├── config/
│   ├── CsvConfig.java
│   ├── DatabaseConfig.java
│   ├── PathConfig.java
│   └── UiConfig.java
│
├── data/
│   └── database/
│       ├── DatabaseConnection.java
│       └── DatabaseInitializer.java
│
├── model/
│   ├── Person.java
│   └── PersonCreate.java
│
├── repository/
│   └── PersonRepository.java
│
├── service/
│   └── PersonService.java
│
├── validator/
│   └── PersonValidator.java
│
├── normalizer/
│   └── PersonNormalizer.java
│
├── formatter/
│   └── PersonFormatter.java
│
├── importer/
│   └── PersonCsvImporter.java
│
└── ui/
    ├── input/
    │   ├── MenuInputReader.java
    │   └── PersonInputReader.java
    │
    └── menu/
        ├── MainMenu.java
        ├── PersonMenu.java
        └── PersonRegistrationMenu.java
```

모든 패키지를 즉시 만들 필요는 없습니다.

현재 기능 구현에 필요한 패키지부터 순차적으로 추가합니다.

---

## 6. 계층별 책임

## 6.1 Main

### 책임

* 애플리케이션 시작
* 공통 객체 생성
* 의존성 연결
* 데이터베이스 초기화
* 최상위 메뉴 실행
* Scanner 자원 관리

### 담당하지 않는 것

* 메뉴 내용 출력
* 사람 등록 규칙
* SQL 실행
* CSV 파싱
* 전화번호 검증

### 예상 구조

```java
public class Main {

    public static void main(String[] args) {

        DatabaseInitializer databaseInitializer =
                new DatabaseInitializer();

        databaseInitializer.initialize();

        try (Scanner scanner = new Scanner(System.in)) {

            MenuInputReader menuInputReader =
                    new MenuInputReader(scanner);

            PersonRepository personRepository =
                    new PersonRepository();

            PersonService personService =
                    new PersonService(personRepository);

            PersonInputReader personInputReader =
                    new PersonInputReader(scanner);

            PersonRegistrationMenu registrationMenu =
                    new PersonRegistrationMenu(
                            menuInputReader,
                            personInputReader,
                            personService
                    );

            PersonMenu personMenu =
                    new PersonMenu(
                            menuInputReader,
                            registrationMenu,
                            personService
                    );

            MainMenu mainMenu =
                    new MainMenu(
                            menuInputReader,
                            personMenu
                    );

            mainMenu.run();
        }
    }
}
```

Main은 객체를 조립하지만 실제 업무 기능을 직접 수행하지 않습니다.

---

## 6.2 Config 계층

Config 계층은 프로그램 전체에서 사용하는 설정값을 관리합니다.

### CsvConfig

담당 항목:

* CSV 인코딩
* CSV 구분자
* 기본 파일명
* 필수 헤더

예:

```java
public static final Charset ENCODING =
        StandardCharsets.UTF_8;
```

### DatabaseConfig

담당 항목:

* JDBC URL
* SQLite 연결 관련 설정

예:

```java
public static final String DB_URL =
        "jdbc:sqlite:" + PathConfig.DATABASE_PATH;
```

### PathConfig

담당 항목:

* 데이터베이스 파일 경로
* CSV 입력 경로
* 데이터 디렉토리 경로

### UiConfig

담당 항목:

* 프로그램 이름
* 메뉴 구분선
* 공통 메시지
* 입력 오류 메시지

Config 클래스는 인스턴스를 만들지 못하도록 제한합니다.

```java
private UiConfig() {
}
```

---

## 6.3 UI 계층

UI 계층은 사용자와 프로그램 사이의 상호작용을 담당합니다.

```text
ui.menu
ui.input
```

### ui.menu

메뉴 출력과 화면 흐름을 담당합니다.

예:

```text
MainMenu
PersonMenu
PersonRegistrationMenu
```

메뉴 클래스의 주요 책임:

* 메뉴 항목 출력
* 선택값 받기
* 하위 메뉴 호출
* Service 호출
* 성공 또는 실패 메시지 출력
* Enter 입력 대기

### ui.input

사용자로부터 구체적인 값을 입력받습니다.

예:

```text
MenuInputReader
PersonInputReader
```

`MenuInputReader`는 메뉴 번호 입력을 처리합니다.

```java
int choice = menuInputReader.readChoice();
```

`PersonInputReader`는 사람 정보 입력을 처리합니다.

```java
String name = personInputReader.readName();
String phone = personInputReader.readPhone();
int genderId = personInputReader.readGenderId();
String address = personInputReader.readAddress();
```

### UI 계층에서 금지할 내용

* JDBC Connection 생성
* SQL 문자열 작성
* 데이터베이스 테이블 생성
* CSV 파일 직접 파싱
* 업무 규칙 결정
* 중복 데이터 확인 로직

---

## 6.4 Model 계층

Model은 프로그램에서 사용하는 데이터를 표현합니다.

### PersonCreate

새로운 사람을 등록할 때 사용합니다.

```java
public record PersonCreate(
        String name,
        String phone,
        int genderId,
        String address
) {
}
```

특징:

* 아직 데이터베이스 ID가 없음
* 등록에 필요한 값만 보유
* 직접 입력과 CSV 입력이 공통으로 사용

### Person

데이터베이스에 저장된 사람을 표현합니다.

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

특징:

* 데이터베이스 ID 포함
* 조회 결과 표현
* 수정 및 삭제 대상 식별
* 공사와 근로 기록 연결 시 사용

### 모델 설계 원칙

모델에는 화면 출력 코드나 SQL을 넣지 않습니다.

잘못된 예:

```java
public void save() {
    // SQL 실행
}
```

현재 프로젝트에서는 데이터 저장 책임을 Repository에 둡니다.

---

## 6.5 Service 계층

Service는 애플리케이션의 업무 규칙을 담당합니다.

### PersonService의 책임

* 사람 등록 요청 처리
* 입력값 검증
* 값 정규화
* 중복 확인
* Repository 호출
* Repository 예외를 업무 수준의 예외로 변환
* 여러 Repository 작업을 하나의 업무로 묶기

예:

```java
public long register(PersonCreate request) {

    String normalizedPhone =
            normalizePhone(request.phone());

    validateName(request.name());
    validatePhone(normalizedPhone);

    PersonCreate normalizedPerson =
            new PersonCreate(
                    request.name().trim(),
                    normalizedPhone,
                    request.genderId(),
                    request.address().trim()
            );

    return personRepository.insert(normalizedPerson);
}
```

### Service 계층이 필요한 이유

현재 사람 등록은 단순해 보여도 추후 다음 규칙이 추가될 수 있습니다.

* 동일 전화번호 등록 금지
* 퇴사자의 재등록 처리
* 성별 ID 유효성 확인
* 근로자 유형 기본값 설정
* 주소 공백 처리
* 등록 일시 기록
* 변경 이력 저장
* 여러 테이블 동시 저장

이러한 규칙을 UI나 Repository에 넣지 않고 Service에 모읍니다.

---

## 6.6 Repository 계층

Repository는 데이터베이스 접근을 전담합니다.

### PersonRepository의 책임

* INSERT
* SELECT
* UPDATE
* DELETE
* SQL 작성
* PreparedStatement 값 설정
* ResultSet을 Person 객체로 변환
* 생성된 ID 반환

예상 메서드:

```java
public long insert(PersonCreate person);

public Optional<Person> findById(long id);

public List<Person> findAll();

public boolean existsByPhone(String phone);

public boolean update(Person person);

public boolean deleteById(long id);
```

### Repository 계층의 원칙

SQL Injection 방지를 위해 `PreparedStatement`를 사용합니다.

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
```

문자열 결합으로 SQL을 만들지 않습니다.

잘못된 예:

```java
String sql =
        "INSERT INTO person VALUES ('"
        + name
        + "')";
```

### Repository에서 금지할 내용

* 사용자 입력 요청
* 메뉴 출력
* 성공 메시지 출력
* 전화번호 표시 형식 결정
* 등록을 계속할지 질문

---

## 6.7 Data 계층

Data 계층은 실제 데이터 소스와 연결합니다.

### DatabaseConnection

SQLite 연결을 생성합니다.

```java
public static Connection getConnection()
        throws SQLException {

    return DriverManager.getConnection(
            DatabaseConfig.DB_URL
    );
}
```

### DatabaseInitializer

프로그램 실행에 필요한 기본 테이블과 초기 데이터를 준비합니다.

담당 항목:

* 데이터 디렉토리 생성
* SQLite 파일 생성
* 외래키 활성화
* gender 테이블 생성
* 기본 성별 데이터 입력
* person 테이블 생성

초기화는 기존 데이터를 삭제하지 않아야 합니다.

```sql
CREATE TABLE IF NOT EXISTS person
```

```sql
INSERT OR IGNORE INTO gender
```

### 데이터베이스 자원 관리

Connection, PreparedStatement, ResultSet은 `try-with-resources`로 관리합니다.

```java
try (
        Connection connection =
                DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql)
) {
    // SQL 실행
}
```

---

## 6.8 Validator 계층

Validator는 값이 업무 규칙에 맞는지 검사합니다.

예:

```java
public final class PersonValidator {

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "이름은 비어 있을 수 없습니다."
            );
        }
    }

    public static void validatePhone(String phone) {
        if (!phone.matches("010\\d{8}")) {
            throw new IllegalArgumentException(
                    "전화번호는 010으로 시작하는 11자리여야 합니다."
            );
        }
    }
}
```

Validator는 값을 수정하지 않습니다.

```text
검사만 수행
유효하지 않으면 예외 발생
유효하면 아무것도 반환하지 않음
```

---

## 6.9 Normalizer 계층

Normalizer는 입력값을 저장 가능한 표준 형식으로 변환합니다.

예:

```java
public static String normalizePhone(String phone) {
    return phone.replaceAll("[^0-9]", "");
}
```

입력:

```text
010-1234-5678
010 1234 5678
01012345678
```

저장:

```text
01012345678
```

Normalizer와 Validator의 차이는 다음과 같습니다.

```text
Normalizer  값을 변환
Validator   값이 규칙에 맞는지 확인
```

처리 순서:

```text
입력
→ 정규화
→ 검증
→ 저장
```

---

## 6.10 Formatter 계층

Formatter는 저장된 값을 사용자에게 보여줄 형식으로 변환합니다.

예:

```java
public static String formatPhone(String phone) {
    return phone.substring(0, 3)
            + "-"
            + phone.substring(3, 7)
            + "-"
            + phone.substring(7);
}
```

저장:

```text
01012345678
```

출력:

```text
010-1234-5678
```

Formatter는 데이터베이스 저장값을 변경하지 않습니다.

---

## 6.11 Importer 계층

Importer는 외부 파일을 프로그램 내부 모델로 변환합니다.

### PersonCsvImporter의 책임

* CSV 파일 존재 여부 확인
* 인코딩 적용
* 헤더 검사
* 각 행 읽기
* 각 행을 PersonCreate로 변환
* 오류 행 정보 수집
* PersonService에 등록 요청 전달

권장 흐름:

```text
CSV 파일
→ PersonCsvImporter
→ PersonCreate
→ PersonService.register()
→ PersonRepository.insert()
```

Importer가 Repository를 직접 호출하지 않고 Service를 호출하는 이유는 직접 등록과 CSV 등록에 같은 업무 규칙을 적용하기 위해서입니다.

---

## 7. 사람 등록 아키텍처

### 7.1 직접 등록 흐름

```text
사용자
  ↓
PersonMenu
  ↓
PersonRegistrationMenu
  ↓
PersonInputReader
  ↓
PersonCreate
  ↓
PersonService.register()
  ↓
정규화
  ↓
검증
  ↓
중복 확인
  ↓
PersonRepository.insert()
  ↓
SQLite
  ↓
생성된 Person ID 반환
  ↓
등록 성공 메시지 출력
```

### 7.2 예외 흐름

```text
잘못된 입력
→ IllegalArgumentException
→ UI에서 메시지 출력
→ 사용자에게 다시 입력 요청
```

```text
DB 연결 실패
→ SQLException
→ Repository 또는 Service에서 변환
→ UI에서 일반적인 오류 메시지 출력
→ 로그에는 상세 원인 기록
```

사용자에게 SQL 오류 원문 전체를 그대로 출력하지 않는 것을 권장합니다.

---

## 8. 사람 조회 아키텍처

### 8.1 전체 조회 흐름

```text
PersonMenu
→ PersonService.getAllPeople()
→ PersonRepository.findAll()
→ SELECT 실행
→ ResultSet
→ List<Person>
→ PersonFormatter
→ 목록 출력
```

### 8.2 ID 조회 흐름

```text
사용자 ID 입력
→ PersonInputReader
→ PersonService.getPersonById()
→ PersonRepository.findById()
→ Optional<Person>
→ 결과 출력 또는 없음 안내
```

조회 결과가 없을 가능성이 있으므로 `Optional<Person>` 사용을 고려합니다.

---

## 9. 사람 수정 아키텍처

수정 기능은 기존 정보를 먼저 조회한 후 변경값을 입력받습니다.

```text
수정할 사람 ID 입력
→ 기존 Person 조회
→ 기존 정보 출력
→ 수정값 입력
→ 빈 입력은 기존 값 유지
→ 검증 및 정규화
→ PersonRepository.update()
→ 결과 출력
```

수정 요청 전용 모델을 추가할 수 있습니다.

```text
PersonUpdate
```

예:

```java
public record PersonUpdate(
        long id,
        String name,
        String phone,
        int genderId,
        String address
) {
}
```

등록용 모델과 수정용 모델의 요구사항이 달라질 때 별도로 분리합니다.

---

## 10. 사람 삭제 아키텍처

삭제 기능은 바로 실행하지 않고 대상 확인 단계를 거칩니다.

```text
삭제할 사람 ID 입력
→ Person 조회
→ 대상 정보 출력
→ 삭제 확인
→ PersonService.delete()
→ PersonRepository.deleteById()
```

향후 데이터 연관 관계가 생기면 물리적 삭제보다 논리적 삭제를 검토합니다.

물리적 삭제:

```sql
DELETE FROM person WHERE id = ?
```

논리적 삭제:

```sql
UPDATE person
SET status = 'INACTIVE'
WHERE id = ?
```

공사, 급여, 출근 기록과 연결된 사람은 기록 보존을 위해 논리적 삭제가 더 적합할 수 있습니다.

---

## 11. 데이터베이스 설계 방향

### 11.1 gender 테이블

```sql
CREATE TABLE IF NOT EXISTS gender (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);
```

초기 데이터:

```text
1 Male
2 Female
```

또는 사용자 화면에서는 다음처럼 표시할 수 있습니다.

```text
1 남성
2 여성
```

DB 저장값과 화면 표시값은 별도로 관리할 수 있습니다.

---

### 11.2 person 테이블

```sql
CREATE TABLE IF NOT EXISTS person (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    phone TEXT UNIQUE,
    gender_id INTEGER NOT NULL,
    address TEXT,
    FOREIGN KEY (gender_id)
        REFERENCES gender(id)
);
```

향후 고려 필드:

```text
status
person_type
birth_date
organization_id
created_at
updated_at
deleted_at
```

초기 단계에서는 필요한 필드만 구현하고 실제 기능이 생길 때 확장합니다.

---

## 12. 의존성 생성 방식

현재 단계에서는 Spring과 같은 프레임워크를 도입하지 않습니다.

`Main`에서 객체를 직접 생성하고 생성자 주입 방식으로 연결합니다.

```text
Main
 ├─ MenuInputReader
 ├─ PersonInputReader
 ├─ PersonRepository
 ├─ PersonService
 ├─ PersonRegistrationMenu
 ├─ PersonMenu
 └─ MainMenu
```

### 생성자 주입 원칙

필요한 객체는 클래스 내부에서 임의로 생성하지 않고 외부에서 전달받는 것을 권장합니다.

권장:

```java
public PersonService(
        PersonRepository personRepository
) {
    this.personRepository = personRepository;
}
```

지양:

```java
public PersonService() {
    this.personRepository =
            new PersonRepository();
}
```

외부 주입을 사용하면 다음 장점이 있습니다.

* 객체 교체가 쉬움
* 테스트가 쉬움
* 클래스 간 결합 감소
* 생성 위치 파악이 쉬움
* 프로그램 전체 구조가 Main에 드러남

---

## 13. Scanner 관리 원칙

프로그램 전체에서 하나의 `Scanner`만 사용합니다.

```java
try (Scanner scanner = new Scanner(System.in)) {
    // 모든 입력 객체에 같은 Scanner 전달
}
```

금지 사항:

* 메뉴마다 새로운 Scanner 생성
* 하위 클래스에서 Scanner 종료
* System.in을 여러 객체에서 독립적으로 닫기

Scanner는 Main에서 생성하고 Main에서만 닫습니다.

---

## 14. 메뉴 이동 원칙

각 메뉴는 자신의 반복 루프를 가집니다.

```text
MainMenu.run()
PersonMenu.run()
PersonRegistrationMenu.run()
```

하위 메뉴에서 `0`을 선택하면 현재 메뉴의 반복을 종료하고 호출한 상위 메뉴로 돌아갑니다.

```java
boolean running = true;

while (running) {
    int choice = menuInputReader.readChoice();

    switch (choice) {
        case 0 -> running = false;
    }
}
```

`System.exit()`를 하위 메뉴에서 호출하지 않습니다.

프로그램 전체 종료는 MainMenu 흐름에서 결정합니다.

---

## 15. 오류 처리 원칙

### 사용자 입력 오류

예:

```text
문자 대신 숫자를 입력해야 하는 경우
필수값이 비어 있는 경우
전화번호 형식이 잘못된 경우
존재하지 않는 메뉴 번호
```

처리 방식:

```text
오류 메시지 출력
→ 재입력 요청
→ 프로그램은 종료하지 않음
```

### 업무 규칙 오류

예:

```text
중복 전화번호
존재하지 않는 성별 ID
이미 삭제된 사람
```

Service에서 예외를 발생시키고 UI에서 안내합니다.

### 시스템 오류

예:

```text
DB 연결 실패
CSV 읽기 실패
파일 권한 오류
SQL 실행 실패
```

상세 원인은 로그에 남기고 사용자에게는 이해할 수 있는 메시지를 표시합니다.

---

## 16. 트랜잭션 설계

한 번의 업무가 여러 SQL 작업으로 구성되면 트랜잭션을 사용합니다.

예:

```text
사람 등록
+ 근로자 유형 등록
+ 기본 소속 등록
```

처리 원칙:

```java
connection.setAutoCommit(false);

try {
    // 여러 SQL 실행
    connection.commit();
} catch (SQLException error) {
    connection.rollback();
    throw error;
}
```

현재 단순한 person 한 건 등록은 하나의 INSERT로 처리할 수 있지만, 기능이 확장되면 Service 단위의 트랜잭션 설계를 검토합니다.

---

## 17. 테스트 전략

### 17.1 단위 테스트

각 클래스의 독립적인 규칙을 테스트합니다.

대상:

* 전화번호 정규화
* 이름 검증
* 전화번호 검증
* Formatter
* CSV 행 변환

### 17.2 Repository 테스트

테스트용 SQLite DB를 사용합니다.

확인 항목:

* 사람 등록
* 생성 ID 반환
* 전체 조회
* ID 조회
* 수정
* 삭제
* 중복 전화번호 처리

### 17.3 통합 테스트

다음 흐름이 실제로 연결되는지 확인합니다.

```text
PersonService
→ PersonRepository
→ SQLite
```

### 17.4 수동 CLI 테스트

실제 메뉴 번호를 직접 입력하며 다음을 확인합니다.

* 상위 메뉴 복귀
* 잘못된 입력 재처리
* 메시지를 읽을 수 있는 대기 처리
* 등록 성공 후 메뉴 복귀
* 조회 결과 표시
* 종료 동작

---

## 18. 향후 도메인 확장

사람 관리 구조가 완성되면 같은 패턴으로 다른 도메인을 추가합니다.

```text
Project
WorkSite
Organization
Contract
DailyWork
Attendance
Payroll
Equipment
Material
Document
```

각 도메인은 다음 구조를 기본으로 합니다.

```text
model/
    Project.java
    ProjectCreate.java

repository/
    ProjectRepository.java

service/
    ProjectService.java

ui/menu/
    ProjectMenu.java

ui/input/
    ProjectInputReader.java
```

모든 도메인이 반드시 같은 파일 수를 가져야 하는 것은 아닙니다.

도메인의 실제 복잡도에 따라 필요한 클래스만 추가합니다.

---

## 19. 개발 단계

### 1단계: 메뉴 연결

```text
Main
→ MainMenu
→ PersonMenu
→ PersonRegistrationMenu
```

완료 기준:

* 모든 메뉴 진입 가능
* 0번 선택 시 이전 메뉴 복귀
* 안내 메시지 후 Enter 대기

### 2단계: SQLite 기반 구성

```text
DatabaseConfig
DatabaseConnection
DatabaseInitializer
```

완료 기준:

* DB 파일 자동 생성
* person 및 gender 테이블 생성
* 프로그램 재실행 시 기존 데이터 유지

### 3단계: 사람 등록

```text
PersonInputReader
PersonService
PersonRepository.insert()
```

완료 기준:

* 직접 입력
* 검증
* 정규화
* SQLite 저장
* 생성 ID 출력

### 4단계: 사람 조회

```text
PersonRepository.findAll()
PersonService.getAllPeople()
```

완료 기준:

* 등록 데이터 목록 확인
* 전화번호 출력 형식 적용

### 5단계: 수정 및 삭제

완료 기준:

* ID 기반 대상 조회
* 수정값 반영
* 삭제 전 확인
* 존재하지 않는 ID 처리

### 6단계: CSV 등록

```text
PersonCsvImporter
```

완료 기준:

* 헤더 검사
* 각 행 변환
* 오류 행 안내
* 기존 등록 Service 재사용
* 등록 성공 및 실패 건수 출력

---

## 20. 현재 적용 범위

현재 프로젝트에 즉시 적용할 핵심 구조는 다음과 같습니다.

```text
Main
Config
Model
UI
DatabaseConnection
DatabaseInitializer
PersonRepository
PersonService
```

다음 구조는 실제 필요가 생길 때 추가합니다.

```text
Validator
Normalizer
Formatter
Importer
Controller
Dependency Injection Framework
Logging Framework
```

중앙 Controller나 Spring 도입은 현재 프로젝트 규모에서는 우선순위가 아닙니다.

---

## 21. 아키텍처 결정 요약

| 항목         | 결정                         |
| ---------- | -------------------------- |
| 사용자 인터페이스  | CLI                        |
| 데이터베이스     | SQLite                     |
| DB 접근 방식   | JDBC                       |
| 데이터 접근 계층  | Repository                 |
| 업무 로직 계층   | Service                    |
| 입력 처리      | UI InputReader             |
| 등록 모델      | PersonCreate               |
| 조회 모델      | Person                     |
| 객체 생성 위치   | Main                       |
| 의존성 전달     | 생성자 주입                     |
| Scanner 관리 | 애플리케이션 전체에서 하나             |
| 메뉴 이동      | 각 메뉴의 run 루프               |
| 입력값 저장     | 정규화 후 검증                   |
| 출력 형식      | Formatter                  |
| CSV 처리     | Importer에서 PersonCreate 변환 |
| CSV 저장     | PersonService 재사용          |
| 삭제 방식      | 초기 물리 삭제, 향후 논리 삭제 검토      |
| 프레임워크      | 현재 사용하지 않음                 |

---

## 22. 최종 아키텍처 목표

NGSP_JAVA의 최종 구조는 기능이 추가되더라도 기존 계층을 크게 변경하지 않고 확장할 수 있어야 합니다.

```text
사용자 입력 방식이 바뀌어도
Service와 Repository는 유지

데이터베이스가 바뀌어도
UI와 Service는 최대한 유지

메뉴 구조가 바뀌어도
업무 규칙과 SQL은 유지
```

궁극적으로 다음 구조를 유지하는 것이 목표입니다.

```text
UI는 사용자와 대화한다.

Service는 업무를 판단한다.

Repository는 데이터를 저장하고 조회한다.

Model은 데이터를 표현한다.

Config는 설정을 관리한다.

Main은 모든 객체를 연결하고 프로그램을 시작한다.
```

이 원칙을 기준으로 사람 관리 기능을 먼저 완성하고, 검증된 구조를 공사·현장·작업·급여 관리 기능에 반복 적용합니다.
