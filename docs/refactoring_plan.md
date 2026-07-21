# NGSP_JAVA 리팩터링 계획

## 1. 문서 개요

### 1.1 문서 목적

이 문서는 현재 `NGSP_JAVA` 프로젝트를 안정적으로 개선하기 위한 리팩터링 방향과 실행 순서를 정의합니다.

리팩터링의 주요 목적은 다음과 같습니다.

* 현재 정상 작동하는 기능 보존
* 메뉴와 실제 업무 기능 연결
* 클래스별 책임 명확화
* UI와 업무 로직 분리
* SQLite 데이터베이스 연결
* 중복 코드 감소
* 기능 추가 시 수정 범위 최소화
* 향후 공사·현장·작업·급여 기능 확장 기반 마련
* 한 번에 너무 많은 코드를 수정하여 프로젝트가 망가지는 문제 방지

이 문서는 현재 프로젝트의 코드를 모두 폐기하고 새로 작성하기 위한 계획이 아닙니다.

현재 구조에서 잘된 부분은 유지하고, 부족한 부분만 단계적으로 개선하는 것을 원칙으로 합니다.

---

## 2. 현재 프로젝트 상태

### 2.1 현재 구현된 구조

현재 프로젝트는 다음 실행 구조를 가지고 있습니다.

```text
Main
→ MainMenu
→ PersonMenu
→ PersonRegistrationMenu
```

현재 구현된 주요 구성 요소:

```text
Main.java
config/
data/
model/
ui/input/
ui/menu/
```

현재 확인된 특징:

* `Main`에서 하나의 `Scanner`를 생성함
* `MainMenu`가 최상위 메뉴를 담당함
* `PersonMenu`가 사람 관리 메뉴를 담당함
* `PersonRegistrationMenu`가 사람 등록 하위 메뉴를 담당함
* 메뉴 클래스에 반복 실행을 위한 `run()` 메서드가 있음
* `MenuInputReader`를 통해 메뉴 입력을 처리함
* 각 메뉴에서 `0`을 선택하면 상위 메뉴로 돌아감
* 기능 실행 후 Enter 입력을 기다리는 방식이 적용됨
* `Person`, `PersonCreate` 모델이 분리되어 있음
* SQLite 및 CSV 관련 기본 설정 클래스가 준비되어 있음
* 실제 Service와 Repository 기능은 아직 본격적으로 연결되지 않음

---

### 2.2 현재 구조의 장점

현재 구조에서 유지해야 할 부분은 다음과 같습니다.

#### 계층형 메뉴 구조

```text
MainMenu
→ 도메인 메뉴
→ 세부 기능 메뉴
```

이 구조는 향후 다음처럼 확장하기 적합합니다.

```text
MainMenu
├── PersonMenu
├── ProjectMenu
├── WorkSiteMenu
├── DailyWorkMenu
└── PayrollMenu
```

#### Scanner 공유

프로그램 전체에서 하나의 `Scanner`를 사용하는 방식은 유지합니다.

```java
try (Scanner scanner = new Scanner(System.in)) {
    // 모든 입력 객체에 같은 Scanner 전달
}
```

#### 입력 책임 분리

`MenuInputReader`가 메뉴 입력을 담당하는 구조는 유지합니다.

향후 `PersonInputReader`를 추가하여 사람 정보 입력도 분리합니다.

#### 등록 모델과 조회 모델 분리

```text
PersonCreate
Person
```

이 구분은 데이터베이스 등록 전 데이터와 저장 후 데이터를 구별하는 데 적합합니다.

#### 사용자 입력 기반 대기

```text
결과 메시지 출력
→ Enter 입력 대기
→ 메뉴 복귀
```

고정 대기시간보다 적절하므로 모든 기능에서 공통으로 사용합니다.

---

## 3. 현재 개선이 필요한 부분

### 3.1 메뉴와 실제 기능이 연결되지 않음

현재 일부 메뉴는 다음과 같은 준비 중 메시지만 출력합니다.

```text
사람 등록 기능을 준비 중입니다.
```

메뉴 이동 구조는 완성되어 있지만 실제 업무 기능은 연결되지 않은 상태입니다.

개선 방향:

```text
PersonRegistrationMenu
→ PersonInputReader
→ PersonService
→ PersonRepository
→ SQLite
```

---

### 3.2 Service 계층이 없음

현재 메뉴에서 실제 업무 처리를 담당할 Service 계층이 필요합니다.

예상 클래스:

```text
service/
└── PersonService.java
```

`PersonService`가 담당할 기능:

* 사람 등록 요청 처리
* 입력값 정규화
* 입력값 검증
* 전화번호 중복 확인
* Repository 호출
* 조회·수정·삭제 업무 흐름 조정

---

### 3.3 Repository 계층이 없음

SQLite와 직접 통신할 Repository 계층이 필요합니다.

예상 클래스:

```text
repository/
└── PersonRepository.java
```

`PersonRepository`가 담당할 기능:

```java
long insert(PersonCreate person);

Optional<Person> findById(long id);

List<Person> findAll();

boolean existsByPhone(String phone);

boolean update(Person person);

boolean deleteById(long id);
```

---

### 3.4 데이터베이스 연결과 초기화가 미완성됨

필요한 구성:

```text
data/database/
├── DatabaseConnection.java
└── DatabaseInitializer.java
```

책임:

```text
DatabaseConnection
→ SQLite 연결 생성
→ 외래키 활성화

DatabaseInitializer
→ 디렉토리 생성
→ gender 테이블 생성
→ gender 초기값 삽입
→ person 테이블 생성
```

---

### 3.5 사람 정보 입력 클래스가 없음

사람 등록 화면에서 구체적인 정보를 입력받을 클래스가 필요합니다.

```text
ui/input/
└── PersonInputReader.java
```

예상 입력:

```text
이름
전화번호
성별
주소
```

`PersonInputReader`는 사용자 입력을 받고 `PersonCreate` 생성에 필요한 값을 반환합니다.

---

### 3.6 검증과 정규화 위치가 확정되지 않음

전화번호 예:

```text
입력: 010-1234-5678
저장: 01012345678
출력: 010-1234-5678
```

필요한 처리:

```text
Normalizer
→ 입력값을 저장 형식으로 변환

Validator
→ 변환된 값이 규칙에 맞는지 확인

Formatter
→ 저장값을 사용자 표시 형식으로 변환
```

초기에는 `PersonService`의 private 메서드로 구현할 수 있습니다.

중복 사용이 확인되면 다음 클래스로 분리합니다.

```text
normalizer/PersonNormalizer.java
validator/PersonValidator.java
formatter/PersonFormatter.java
```

---

## 4. 리팩터링 기본 원칙

## 4.1 정상 작동 상태를 먼저 보존한다

리팩터링 시작 전 현재 코드를 실행하여 다음 흐름을 확인합니다.

```text
프로그램 실행
→ 메인 메뉴
→ 사람 관리 메뉴
→ 사람 등록 메뉴
→ 이전 메뉴 복귀
→ 프로그램 종료
```

모든 흐름이 정상 작동하면 Git에 커밋합니다.

권장 커밋 메시지:

```text
refactor: 리팩터링 전 메뉴 정상 상태 저장
```

이 커밋은 문제가 발생했을 때 되돌아갈 기준점이 됩니다.

---

## 4.2 한 번에 하나의 책임만 변경한다

다음 작업을 한 번에 처리하지 않습니다.

```text
패키지 전체 변경
+ 모든 클래스 이름 변경
+ SQLite 연결
+ 사람 CRUD 구현
+ CSV 등록 구현
```

권장 방식:

```text
1. 데이터베이스 연결 클래스 추가
2. 실행 확인 및 커밋
3. 데이터베이스 초기화 추가
4. 실행 확인 및 커밋
5. 사람 등록 Repository 추가
6. 실행 확인 및 커밋
```

---

## 4.3 기능 추가와 구조 변경을 분리한다

예를 들어 사람 등록 기능을 구현하면서 모든 패키지의 이름을 동시에 바꾸지 않습니다.

권장 순서:

```text
구조 정리
→ 실행 확인
→ 커밋
→ 기능 구현
→ 실행 확인
→ 커밋
```

---

## 4.4 기존 파일을 먼저 활용한다

새로운 클래스를 추가하기 전에 현재 클래스에서 책임을 담당할 수 있는지 확인합니다.

예:

`PersonRegistrationMenu`가 이미 존재한다면 새로운 `PersonCreateMenu`를 만들기 전에 기존 클래스에 직접 등록 흐름을 연결합니다.

다음과 같은 중복 클래스 생성을 피합니다.

```text
PersonRegistrationMenu
PersonRegisterMenu
PersonCreateMenu
RegisterPersonMenu
```

하나의 이름을 결정하여 사용합니다.

---

## 4.5 구현되지 않은 추상화를 미리 만들지 않는다

현재 Repository 구현체가 SQLite 하나뿐이라면 처음부터 다음 구조가 반드시 필요한 것은 아닙니다.

```text
PersonRepository interface
SqlitePersonRepository
RepositoryFactory
RepositoryProvider
```

초기에는 다음으로 시작할 수 있습니다.

```text
PersonRepository.java
```

향후 다른 저장 방식이 필요해질 때 인터페이스를 분리합니다.

---

## 4.6 리팩터링 중 기존 기능을 유지한다

리팩터링은 기능을 없애는 작업이 아닙니다.

매 단계마다 다음을 확인합니다.

```text
기존 메뉴가 실행되는가?

이전 메뉴로 돌아갈 수 있는가?

프로그램이 정상 종료되는가?

Scanner가 중복 생성되지 않았는가?

결과 메시지를 읽을 수 있는가?
```

---

## 5. 목표 아키텍처

리팩터링 이후 기본 구조는 다음을 목표로 합니다.

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
│   ├── PersonCreate.java
│   └── PersonUpdate.java
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

모든 폴더와 클래스를 즉시 만드는 것은 아닙니다.

필요한 단계에서 순차적으로 추가합니다.

---

## 6. 리팩터링 단계 요약

```text
0단계  현재 상태 보존
1단계  패키지와 이름 점검
2단계  데이터베이스 연결
3단계  데이터베이스 초기화
4단계  사람 등록 Repository
5단계  사람 등록 Service
6단계  사람 입력 UI
7단계  직접 등록 연결
8단계  사람 조회
9단계  사람 수정
10단계 사람 삭제
11단계 CSV 등록
12단계 검증·정규화·출력 분리
13단계 테스트 추가
14단계 문서와 코드 동기화
```

---

# 7. 0단계: 현재 상태 보존

## 7.1 목표

현재 정상 작동하는 메뉴 구조를 기준점으로 보존합니다.

## 7.2 수행 작업

* 전체 프로젝트 컴파일
* Main 실행
* 모든 현재 메뉴 직접 입력
* `0`을 통한 이전 메뉴 이동 확인
* 프로그램 종료 확인
* 불필요한 디버깅 출력 제거
* 현재 상태 Git 커밋

## 7.3 확인 항목

```text
[ ] MainMenu가 표시된다.
[ ] PersonMenu로 이동할 수 있다.
[ ] PersonRegistrationMenu로 이동할 수 있다.
[ ] 하위 메뉴에서 이전 메뉴로 돌아갈 수 있다.
[ ] 결과 메시지 후 Enter 대기가 작동한다.
[ ] 프로그램이 정상 종료된다.
```

## 7.4 완료 커밋 예

```text
chore: 리팩터링 전 현재 동작 상태 저장
```

---

# 8. 1단계: 패키지와 이름 점검

## 8.1 목표

기능 구현 전에 현재 클래스 이름과 패키지가 역할에 맞는지 확인합니다.

## 8.2 점검 기준

```text
Main              프로그램 시작
MainMenu          메인 메뉴
PersonMenu        사람 관리 메뉴
PersonRegistrationMenu 사람 등록 메뉴
MenuInputReader   메뉴 선택 입력
Person            저장된 사람 모델
PersonCreate      신규 등록 모델
```

## 8.3 수행 작업

* 클래스명 중복 여부 확인
* 패키지 선언과 디렉토리 일치 확인
* 사용하지 않는 클래스 확인
* 사용하지 않는 import 제거
* 클래스 파일명과 public 클래스명 일치 확인
* 오탈자 수정
* IDE Rename 기능 사용

## 8.4 주의사항

이 단계에서 업무 기능을 추가하지 않습니다.

이름과 위치만 정리한 후 반드시 실행합니다.

## 8.5 완료 기준

```text
[ ] 동일한 역할의 중복 클래스가 없다.
[ ] 파일명과 클래스명이 일치한다.
[ ] 패키지와 폴더가 일치한다.
[ ] 프로그램이 이전과 동일하게 작동한다.
```

---

# 9. 2단계: 데이터베이스 연결 분리

## 9.1 목표

SQLite 연결 생성을 전담하는 클래스를 추가합니다.

## 9.2 추가 클래스

```text
data/database/DatabaseConnection.java
```

## 9.3 책임

```text
JDBC URL 사용
SQLite Connection 생성
외래키 활성화
Connection 반환
```

## 9.4 예상 코드 구조

```java
public final class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static Connection getConnection()
            throws SQLException {

        Connection connection =
                DriverManager.getConnection(
                        DatabaseConfig.DB_URL
                );

        try (Statement statement =
                     connection.createStatement()) {

            statement.execute(
                    "PRAGMA foreign_keys = ON"
            );
        }

        return connection;
    }
}
```

## 9.5 완료 기준

```text
[ ] SQLite JDBC 드라이버가 연결된다.
[ ] 지정 경로에 DB 파일이 생성된다.
[ ] Connection을 생성하고 닫을 수 있다.
[ ] 외래키 설정이 활성화된다.
[ ] 메뉴 기능은 기존대로 작동한다.
```

## 9.6 완료 커밋 예

```text
feat: SQLite 데이터베이스 연결 클래스 추가
```

---

# 10. 3단계: 데이터베이스 초기화 분리

## 10.1 목표

프로그램 실행에 필요한 테이블을 안전하게 준비합니다.

## 10.2 추가 클래스

```text
data/database/DatabaseInitializer.java
```

## 10.3 초기화 순서

```text
데이터 디렉토리 생성
→ gender 테이블 생성
→ 기본 성별 데이터 삽입
→ person 테이블 생성
```

## 10.4 Main 변경

기존:

```text
Main
→ Scanner 생성
→ MainMenu 실행
```

변경:

```text
Main
→ DatabaseInitializer.initialize()
→ Scanner 생성
→ MainMenu 실행
```

## 10.5 데이터 보존 규칙

사용:

```sql
CREATE TABLE IF NOT EXISTS
```

```sql
INSERT OR IGNORE
```

금지:

```sql
DROP TABLE
```

```sql
DELETE FROM person
```

## 10.6 완료 기준

```text
[ ] 첫 실행 시 테이블이 생성된다.
[ ] 재실행 시 오류가 발생하지 않는다.
[ ] 재실행해도 기존 데이터가 유지된다.
[ ] gender 기본값 1, 2가 한 번만 존재한다.
[ ] 초기화 실패 시 명확한 오류가 발생한다.
```

## 10.7 완료 커밋 예

```text
feat: SQLite 테이블 초기화 기능 추가
```

---

# 11. 4단계: PersonRepository 등록 기능

## 11.1 목표

사람 정보를 SQLite에 저장하는 기능을 Repository로 분리합니다.

## 11.2 추가 클래스

```text
repository/PersonRepository.java
```

## 11.3 첫 번째 구현 메서드

```java
public long insert(
        PersonCreate person
);
```

## 11.4 SQL

```sql
INSERT INTO person (
    name,
    phone,
    gender_id,
    address
)
VALUES (?, ?, ?, ?);
```

## 11.5 구현 원칙

* `PreparedStatement` 사용
* `try-with-resources` 사용
* 생성된 ID 반환
* Repository에서 화면 출력 금지
* Repository에서 Scanner 사용 금지
* SQL 예외를 무시하지 않음

## 11.6 완료 기준

```text
[ ] PersonCreate를 저장할 수 있다.
[ ] 생성된 person.id를 반환한다.
[ ] 잘못된 gender_id 저장이 거부된다.
[ ] 중복 전화번호 저장이 거부된다.
[ ] DB 자원이 자동으로 닫힌다.
```

## 11.7 완료 커밋 예

```text
feat: PersonRepository 사람 등록 구현
```

---

# 12. 5단계: PersonService 등록 기능

## 12.1 목표

사람 등록 업무 규칙을 Repository와 UI 사이에 배치합니다.

## 12.2 추가 클래스

```text
service/PersonService.java
```

## 12.3 책임

```text
이름 정리
전화번호 정규화
입력값 검증
전화번호 중복 확인
PersonRepository 호출
등록 ID 반환
```

## 12.4 예상 메서드

```java
public long register(
        PersonCreate person
);
```

## 12.5 처리 순서

```text
PersonCreate 전달
→ null 확인
→ 문자열 trim
→ 전화번호 정규화
→ 값 검증
→ 중복 확인
→ 정규화된 PersonCreate 생성
→ Repository.insert()
→ 생성된 ID 반환
```

## 12.6 Repository 추가 메서드

```java
public boolean existsByPhone(
        String phone
);
```

## 12.7 완료 기준

```text
[ ] UI 없이 Service 단독 등록이 가능하다.
[ ] 전화번호 하이픈이 제거된다.
[ ] 잘못된 전화번호는 저장 전에 거부된다.
[ ] 중복 전화번호에 명확한 오류가 발생한다.
[ ] Service에서 화면을 출력하지 않는다.
```

## 12.8 완료 커밋 예

```text
feat: 사람 등록 서비스 및 검증 흐름 추가
```

---

# 13. 6단계: PersonInputReader 추가

## 13.1 목표

사람 등록에 필요한 입력을 메뉴 클래스에서 분리합니다.

## 13.2 추가 클래스

```text
ui/input/PersonInputReader.java
```

## 13.3 예상 메서드

```java
public String readName();

public String readPhone();

public int readGenderId();

public String readAddress();

public PersonCreate readPersonCreate();
```

## 13.4 입력 책임

`PersonInputReader`가 처리할 내용:

* 사용자 입력 안내
* `nextLine()` 호출
* 기본 공백 제거
* 숫자 입력 변환
* 입력이 비어 있는지 확인
* 잘못된 입력 재요청

Service가 처리할 내용:

* 업무 규칙 검증
* 전화번호 저장 형식 변환
* 중복 확인
* DB 기준값 확인

## 13.5 완료 기준

```text
[ ] Main에서 사용하던 Scanner를 전달받는다.
[ ] 새로운 Scanner를 만들지 않는다.
[ ] 이름, 전화번호, 성별, 주소를 입력받는다.
[ ] 잘못된 성별 입력 시 재입력한다.
[ ] 입력 결과로 PersonCreate를 생성한다.
```

## 13.6 완료 커밋 예

```text
feat: 사람 정보 입력 클래스 추가
```

---

# 14. 7단계: 직접 등록 UI 연결

## 14.1 목표

기존 준비 중 메시지를 실제 사람 등록 기능으로 교체합니다.

## 14.2 변경 대상

```text
PersonRegistrationMenu.java
Main.java
```

## 14.3 새로운 실행 흐름

```text
PersonRegistrationMenu
→ PersonInputReader.readPersonCreate()
→ PersonService.register()
→ 생성 ID 반환
→ 등록 성공 메시지
→ Enter 대기
→ 등록 메뉴 복귀
```

## 14.4 Main 객체 조립

```text
PersonRepository 생성
→ PersonService 생성
→ PersonInputReader 생성
→ PersonRegistrationMenu에 전달
→ PersonMenu에 전달
→ MainMenu에 전달
```

## 14.5 성공 화면

```text
사람 등록이 완료되었습니다.

등록 번호: 1
이름: 홍길동
전화번호: 010-1234-5678

계속하려면 Enter 키를 누르세요.
```

## 14.6 실패 화면

```text
사람 등록에 실패했습니다.
이미 등록된 전화번호입니다.

계속하려면 Enter 키를 누르세요.
```

## 14.7 완료 기준

```text
[ ] 메뉴에서 직접 등록을 선택할 수 있다.
[ ] 입력 정보가 SQLite에 저장된다.
[ ] 생성된 ID가 출력된다.
[ ] 오류 발생 후 프로그램이 종료되지 않는다.
[ ] 결과를 읽은 후 메뉴로 돌아간다.
```

## 14.8 완료 커밋 예

```text
feat: 사람 직접 등록 UI와 SQLite 연결
```

---

# 15. 8단계: 사람 조회 기능

## 15.1 목표

등록된 사람 정보를 조회할 수 있도록 합니다.

## 15.2 Repository 메서드

```java
public List<Person> findAll();

public Optional<Person> findById(
        long id
);
```

## 15.3 Service 메서드

```java
public List<Person> getAllPeople();

public Person getPerson(
        long id
);
```

또는 없는 결과를 Service에서도 `Optional`로 유지할 수 있습니다.

## 15.4 UI 흐름

초기 구현:

```text
PersonMenu
→ 2. 사람 조회
→ 전체 목록 출력
→ Enter 대기
→ PersonMenu 복귀
```

향후 확장:

```text
사람 조회
├── 전체 조회
├── ID 조회
├── 이름 검색
└── 전화번호 검색
```

## 15.5 완료 기준

```text
[ ] 저장된 사람 전체를 조회한다.
[ ] 데이터가 없으면 안내한다.
[ ] 전화번호를 하이픈 형식으로 출력한다.
[ ] 성별 ID를 사용자용 값으로 출력한다.
[ ] Repository에서 UI 출력을 하지 않는다.
```

## 15.6 완료 커밋 예

```text
feat: 사람 전체 조회 기능 추가
```

---

# 16. 9단계: 사람 수정 기능

## 16.1 목표

기존 사람 정보를 조회하고 변경할 수 있도록 합니다.

## 16.2 추가 모델 검토

```text
model/PersonUpdate.java
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

## 16.3 Repository 메서드

```java
public boolean update(
        Person person
);
```

또는 `PersonUpdate`를 받을 수 있습니다.

## 16.4 UI 흐름

```text
수정할 ID 입력
→ 기존 사람 조회
→ 기존값 출력
→ 변경값 입력
→ 빈 입력은 기존값 유지
→ 수정 내용 확인
→ Service 호출
→ 결과 출력
→ Enter 대기
```

## 16.5 완료 기준

```text
[ ] 존재하는 사람만 수정할 수 있다.
[ ] 존재하지 않는 ID를 안내한다.
[ ] 기존값을 유지할 수 있다.
[ ] 수정된 전화번호도 정규화·검증된다.
[ ] 다른 사람의 전화번호와 중복되지 않는다.
```

## 16.6 완료 커밋 예

```text
feat: 사람 정보 수정 기능 추가
```

---

# 17. 10단계: 사람 삭제 기능

## 17.1 목표

사람 정보를 삭제하거나 비활성화할 수 있도록 합니다.

## 17.2 초기 결정

현재 학습 단계에서는 물리 삭제를 구현할 수 있습니다.

```sql
DELETE FROM person
WHERE id = ?;
```

그러나 공사·근무·급여 기록이 연결되기 전 논리 삭제 전환 여부를 결정해야 합니다.

## 17.3 Repository 메서드

```java
public boolean deleteById(
        long id
);
```

## 17.4 UI 흐름

```text
삭제할 ID 입력
→ 대상 조회
→ 사람 정보 출력
→ 삭제 확인
→ Service 호출
→ 결과 출력
→ Enter 대기
```

## 17.5 완료 기준

```text
[ ] 존재하지 않는 ID는 삭제되지 않는다.
[ ] 삭제 전에 대상 정보가 표시된다.
[ ] 사용자 확인 없이 삭제되지 않는다.
[ ] 취소할 수 있다.
[ ] 성공과 실패 결과가 구분된다.
```

## 17.6 완료 커밋 예

```text
feat: 사람 삭제 기능 추가
```

---

# 18. 11단계: CSV 등록 기능

## 18.1 목표

CSV 파일의 여러 사람을 기존 등록 흐름으로 저장합니다.

## 18.2 추가 클래스

```text
importer/PersonCsvImporter.java
```

## 18.3 핵심 원칙

CSV Importer가 Repository를 직접 호출하지 않습니다.

```text
CSV
→ PersonCsvImporter
→ PersonCreate
→ PersonService.register()
→ PersonRepository.insert()
```

직접 등록과 CSV 등록에 동일한 업무 규칙을 적용합니다.

## 18.4 처리 내용

```text
파일 존재 확인
→ 인코딩 적용
→ 헤더 검사
→ 행 읽기
→ PersonCreate 변환
→ Service 등록
→ 성공·실패 결과 수집
```

## 18.5 결과 모델 검토

필요하면 다음과 같은 결과 모델을 추가합니다.

```text
PersonImportResult
```

예상 데이터:

```text
전체 행 수
성공 건수
실패 건수
오류 행 번호
오류 사유
```

## 18.6 완료 기준

```text
[ ] CSV 필수 헤더를 검사한다.
[ ] 전화번호 형식을 정규화한다.
[ ] 잘못된 행 때문에 프로그램이 종료되지 않는다.
[ ] 성공 및 실패 건수를 출력한다.
[ ] 직접 등록과 동일한 Service를 사용한다.
```

## 18.7 완료 커밋 예

```text
feat: CSV 사람 일괄 등록 기능 추가
```

---

# 19. 12단계: 검증·정규화·출력 분리

## 19.1 목표

Service나 UI에 반복되는 보조 로직을 필요에 따라 별도 클래스로 분리합니다.

## 19.2 분리 조건

다음 조건 중 하나 이상이면 분리합니다.

```text
같은 로직이 두 곳 이상에서 반복됨

Service의 핵심 흐름을 읽기 어려워짐

독립 테스트가 필요함

다른 도메인에서도 재사용 가능함
```

## 19.3 예상 클래스

```text
validator/PersonValidator.java
normalizer/PersonNormalizer.java
formatter/PersonFormatter.java
```

## 19.4 역할 구분

```text
PersonNormalizer
→ 값을 표준 저장 형식으로 변환

PersonValidator
→ 값이 업무 규칙에 맞는지 검사

PersonFormatter
→ 저장값을 사용자 표시 형식으로 변환
```

## 19.5 주의사항

클래스를 만들기 위해 억지로 메서드를 이동하지 않습니다.

현재 한 곳에서만 사용되는 한 줄짜리 메서드는 기존 클래스에 유지할 수 있습니다.

## 19.6 완료 기준

```text
[ ] 중복 코드가 감소한다.
[ ] 클래스 역할이 더 명확해진다.
[ ] 의존성이 불필요하게 복잡해지지 않는다.
[ ] 기존 기능의 동작이 동일하다.
```

## 19.7 완료 커밋 예

```text
refactor: 사람 검증과 정규화 로직 분리
```

---

# 20. 13단계: 테스트 추가

## 20.1 목표

리팩터링과 기능 추가로 기존 동작이 깨지는 것을 빠르게 확인합니다.

## 20.2 우선 테스트 대상

### 단위 테스트

```text
전화번호 정규화
전화번호 검증
이름 검증
전화번호 출력 형식
성별 출력 변환
```

### Repository 테스트

```text
사람 등록
전체 조회
ID 조회
수정
삭제
전화번호 중복
외래키 오류
```

### Service 테스트

```text
정상 등록
중복 전화번호
잘못된 전화번호
존재하지 않는 사람 조회
수정 시 중복 확인
```

### 수동 UI 테스트

```text
잘못된 메뉴 입력
문자 입력
이전 메뉴 이동
등록 결과 Enter 대기
삭제 취소
프로그램 종료
```

## 20.3 실제 DB 보호

테스트는 실제 업무 DB를 사용하지 않습니다.

```text
data/database/test_database.db
```

또는:

```text
jdbc:sqlite::memory:
```

## 20.4 완료 기준

```text
[ ] 핵심 규칙에 단위 테스트가 있다.
[ ] Repository가 테스트 DB에서 작동한다.
[ ] 실제 database.db가 테스트로 변경되지 않는다.
[ ] 리팩터링 후 테스트가 통과한다.
```

## 20.5 완료 커밋 예

```text
test: 사람 등록과 전화번호 검증 테스트 추가
```

---

# 21. 14단계: 문서와 코드 동기화

## 21.1 목표

구현 결과에 맞게 설계 문서를 갱신합니다.

## 21.2 확인할 문서

```text
README.md
docs/analysis_results.md
docs/architecture.md
docs/database_design.md
docs/ui_flow.md
docs/coding_rules.md
docs/refactoring_plan.md
```

## 21.3 갱신 내용

* 실제 패키지 구조
* 실제 클래스명
* 구현 완료 기능
* 예정 기능
* 데이터베이스 컬럼
* 메뉴 번호
* 검증 규칙
* 삭제 정책
* CSV 형식
* 테스트 실행 방법

## 21.4 문서 상태 표시

문서에 다음 구분을 명확히 표시합니다.

```text
현재 구현됨
부분 구현됨
구현 예정
검토 중
```

설계와 구현을 혼동하지 않도록 합니다.

## 21.5 완료 커밋 예

```text
docs: 현재 구현 상태에 맞게 설계 문서 갱신
```

---

# 22. Main 리팩터링 계획

## 22.1 Main의 최종 책임

`Main`은 다음 역할만 담당합니다.

```text
데이터베이스 초기화
공통 객체 생성
의존성 연결
Scanner 관리
MainMenu 실행
```

## 22.2 Main에 두지 않을 내용

```text
메뉴 출력
사람 입력
전화번호 검증
SQL 실행
CSV 파싱
사람 목록 출력
```

## 22.3 예상 객체 생성 순서

```text
DatabaseInitializer
→ Scanner
→ MenuInputReader
→ PersonInputReader
→ PersonRepository
→ PersonService
→ PersonRegistrationMenu
→ PersonMenu
→ MainMenu
→ MainMenu.run()
```

## 22.4 예상 구조

```java
public class Main {

    public static void main(String[] args) {

        DatabaseInitializer initializer =
                new DatabaseInitializer();

        initializer.initialize();

        try (Scanner scanner = new Scanner(System.in)) {

            MenuInputReader menuInputReader =
                    new MenuInputReader(scanner);

            PersonInputReader personInputReader =
                    new PersonInputReader(scanner);

            PersonRepository personRepository =
                    new PersonRepository();

            PersonService personService =
                    new PersonService(
                            personRepository
                    );

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

Main 코드가 길어지는 것은 초기 수동 의존성 주입 구조에서는 어느 정도 허용합니다.

객체가 지나치게 많아질 때 별도 조립 클래스를 검토합니다.

```text
ApplicationFactory
ApplicationContext
```

현재 단계에서는 필요하지 않습니다.

---

# 23. Menu 리팩터링 계획

## 23.1 공통 원칙

모든 메뉴는 다음 구조를 따릅니다.

```java
public void run() {
    while (true) {
        printMenu();

        int choice =
                menuInputReader.readChoice(
                        "메뉴를 선택하세요: "
                );

        if (choice == 0) {
            return;
        }

        handleChoice(choice);
    }
}
```

## 23.2 메뉴 클래스 책임

```text
메뉴 출력
사용자 선택
하위 기능 호출
결과 메시지
Enter 대기
상위 메뉴 복귀
```

## 23.3 제거할 책임

```text
SQL
DB 연결
업무 검증
중복 확인
CSV 내부 변환
```

## 23.4 중복 메뉴 출력 처리

구분선이나 공통 문구가 반복되더라도 즉시 하나의 거대한 UI 유틸리티로 만들지 않습니다.

반복이 많아지면 다음 정도를 `UiConfig`로 분리합니다.

```text
DIVIDER
MENU_PROMPT
WAIT_MESSAGE
INVALID_MENU_MESSAGE
```

메뉴 항목 자체는 각 Menu 클래스에서 관리합니다.

---

# 24. 예외 리팩터링 계획

## 24.1 초기 단계

초기에는 표준 예외를 사용할 수 있습니다.

```text
IllegalArgumentException
IllegalStateException
SQLException
IOException
```

## 24.2 사용자 오류 구분

기능이 커지면 업무 예외를 추가합니다.

```text
DuplicatePhoneException
PersonNotFoundException
InvalidPersonException
DataAccessException
CsvImportException
```

## 24.3 예외 흐름

```text
Repository
→ SQLException을 DataAccessException으로 변환

Service
→ 업무 오류 발생

UI
→ 사용자 메시지 출력
```

## 24.4 주의사항

처음부터 모든 오류마다 별도 예외 클래스를 만들지 않습니다.

같은 오류 처리가 반복되거나 UI에서 오류 종류를 구분할 필요가 생길 때 추가합니다.

---

# 25. 데이터베이스 리팩터링 계획

## 25.1 초기 스키마

```text
gender
person
```

## 25.2 CRUD 완료 전 변경 최소화

사람 CRUD가 완성되기 전에는 불필요한 테이블을 추가하지 않습니다.

현재 우선순위:

```text
gender
person
```

나중 순위:

```text
organization
project
work_site
daily_work
attendance
payroll
```

## 25.3 논리 삭제 결정 시점

사람이 다른 업무 기록과 연결되기 전에 결정합니다.

```text
현재 CRUD 학습
→ 물리 삭제 가능

공사·근무·급여 연결 전
→ 논리 삭제 전환 검토
```

## 25.4 마이그레이션 도입 시점

다음 조건이 발생하면 스키마 버전 관리를 도입합니다.

```text
실제 업무 데이터가 축적됨

기존 테이블에 컬럼 추가가 필요함

사용자에게 프로그램을 배포함

여러 DB 파일의 버전을 맞춰야 함
```

초기에는 `CREATE TABLE IF NOT EXISTS`로 시작합니다.

---

# 26. 리팩터링 중 피해야 할 작업

## 26.1 한꺼번에 전체 구조 변경

피해야 할 예:

```text
모든 패키지 이동
모든 클래스 이름 변경
Service와 Repository 추가
DB 스키마 변경
CSV 등록 구현
테스트 도입
```

이 작업을 한 번에 하면 오류 원인을 구분하기 어렵습니다.

---

## 26.2 정상 코드를 이유 없이 다시 작성

현재 잘 작동하는 `MenuInputReader`를 새로운 스타일이 더 좋아 보인다는 이유만으로 계속 다시 작성하지 않습니다.

변경 기준:

```text
실제 오류가 있는가?

중복이 발생했는가?

새로운 요구사항을 처리할 수 없는가?

책임이 명확하지 않은가?
```

---

## 26.3 기능 없는 클래스 생성

다음과 같은 빈 클래스 생성을 피합니다.

```java
public class PersonService {
}
```

실제 기능을 구현할 단계에서 클래스를 추가합니다.

---

## 26.4 지나친 범용화

사람 등록 하나를 위해 모든 도메인에서 사용할 범용 CRUD 프레임워크를 만들지 않습니다.

지양:

```text
GenericRepository<T, ID>
GenericService<T>
AbstractCrudMenu<T>
BaseValidator<T>
```

현재는 명확한 도메인별 클래스를 사용합니다.

```text
PersonRepository
PersonService
PersonMenu
```

프로젝트에서 반복 패턴이 충분히 확인된 후 공통화를 검토합니다.

---

## 26.5 리팩터링과 새 기능을 혼동

리팩터링:

```text
동작은 같고 구조만 개선
```

기능 추가:

```text
기존에 없던 동작 추가
```

예:

```text
PersonMenu의 SQL을 PersonRepository로 이동
→ 리팩터링

사람 검색 기능 추가
→ 기능 추가
```

두 작업을 가능하면 별도 커밋으로 관리합니다.

---

# 27. 리팩터링 완료 기준

사람 관리 1차 리팩터링은 다음 조건을 만족하면 완료된 것으로 봅니다.

## 구조

```text
[ ] Main은 객체 생성과 연결만 담당한다.
[ ] Menu는 화면과 흐름만 담당한다.
[ ] InputReader는 사용자 입력을 담당한다.
[ ] Service는 업무 규칙을 담당한다.
[ ] Repository는 SQL을 담당한다.
[ ] Model은 데이터만 표현한다.
```

## 기능

```text
[ ] 사람 직접 등록이 작동한다.
[ ] 사람 전체 조회가 작동한다.
[ ] 사람 상세 조회가 작동한다.
[ ] 사람 수정이 작동한다.
[ ] 사람 삭제가 작동한다.
[ ] CSV 등록이 작동한다.
```

## 안정성

```text
[ ] 잘못된 입력으로 프로그램이 종료되지 않는다.
[ ] 중복 전화번호가 저장되지 않는다.
[ ] 잘못된 성별 ID가 저장되지 않는다.
[ ] DB 자원이 안전하게 닫힌다.
[ ] 기존 데이터가 재실행 후 유지된다.
```

## UI

```text
[ ] 결과 메시지를 읽을 수 있다.
[ ] 0번의 의미가 일관된다.
[ ] 삭제 전에 확인한다.
[ ] 상위 메뉴로 정상 복귀한다.
```

## 유지보수

```text
[ ] 같은 SQL이 여러 클래스에 반복되지 않는다.
[ ] 같은 검증 코드가 불필요하게 반복되지 않는다.
[ ] 사용하지 않는 클래스와 import가 없다.
[ ] 실제 DB와 CSV가 Git에서 제외된다.
[ ] 관련 문서가 코드와 일치한다.
```

---

# 28. 리팩터링 작업표

| 순서 | 작업            | 주요 결과물                      | 완료 확인     |
| -: | ------------- | --------------------------- | --------- |
|  0 | 현재 상태 보존      | 기준 커밋                       | 기존 메뉴 정상  |
|  1 | 이름·패키지 점검     | 구조 정리                       | 컴파일 성공    |
|  2 | DB 연결         | `DatabaseConnection`        | 연결 성공     |
|  3 | DB 초기화        | `DatabaseInitializer`       | 테이블 생성    |
|  4 | 등록 Repository | `PersonRepository.insert()` | DB 저장     |
|  5 | 등록 Service    | `PersonService.register()`  | 검증·정규화    |
|  6 | 사람 입력         | `PersonInputReader`         | 모델 생성     |
|  7 | 등록 UI 연결      | 직접 등록 완성                    | 메뉴에서 등록   |
|  8 | 조회            | `findAll`, `findById`       | 목록·상세 조회  |
|  9 | 수정            | `update`                    | 정보 변경     |
| 10 | 삭제            | `deleteById`                | 확인 후 삭제   |
| 11 | CSV 등록        | `PersonCsvImporter`         | 일괄 등록     |
| 12 | 보조 책임 분리      | Validator 등                 | 중복 감소     |
| 13 | 테스트           | 단위·통합 테스트                   | 핵심 테스트 통과 |
| 14 | 문서 갱신         | docs 전체                     | 코드와 일치    |

---

# 29. 권장 커밋 순서

```text
chore: 리팩터링 전 현재 동작 상태 저장

refactor: 패키지와 클래스 이름 정리

feat: SQLite 데이터베이스 연결 클래스 추가

feat: gender 및 person 테이블 초기화 추가

feat: PersonRepository 사람 등록 구현

feat: 사람 등록 서비스 및 검증 추가

feat: 사람 정보 입력 클래스 추가

feat: 사람 직접 등록 메뉴 연결

feat: 사람 전체 조회 기능 추가

feat: 사람 상세 조회 기능 추가

feat: 사람 정보 수정 기능 추가

feat: 사람 삭제 기능 추가

feat: CSV 사람 일괄 등록 기능 추가

refactor: 사람 검증과 출력 로직 분리

test: 사람 관리 핵심 기능 테스트 추가

docs: 현재 구현에 맞게 설계 문서 갱신
```

각 커밋에서 프로그램이 실행 가능한 상태를 유지하는 것이 좋습니다.

---

# 30. 리팩터링 우선순위

## 최우선

```text
현재 상태 Git 커밋
DatabaseConnection
DatabaseInitializer
PersonRepository.insert
PersonService.register
PersonInputReader
직접 등록 UI 연결
```

이 단계가 완료되면 프로그램이 단순한 메뉴 골격에서 실제 데이터를 저장하는 프로그램으로 발전합니다.

## 다음 우선순위

```text
전체 조회
상세 조회
수정
삭제
```

사람 CRUD를 완성합니다.

## 이후 우선순위

```text
CSV 등록
Validator 분리
Normalizer 분리
Formatter 분리
테스트
스키마 마이그레이션
```

## 아직 하지 않을 작업

```text
Spring 도입
웹 서버 구현
GUI 구현
범용 CRUD 프레임워크
공사·급여 테이블 전체 설계 및 구현
복잡한 의존성 주입 프레임워크
마이크로서비스
```

현재 프로젝트 단계에서는 필요하지 않습니다.

---

# 31. 리팩터링 작업 전 확인표

```text
[ ] 현재 코드가 정상 실행되는가?
[ ] 현재 변경사항을 커밋했는가?
[ ] 실제 DB를 백업했는가?
[ ] 이번 작업의 범위가 한 문장으로 설명되는가?
[ ] 수정할 파일을 미리 정했는가?
[ ] 새로운 파일이 정말 필요한가?
[ ] 기존 클래스와 역할이 중복되지 않는가?
```

---

# 32. 리팩터링 작업 후 확인표

```text
[ ] 프로젝트가 컴파일되는가?
[ ] Main이 실행되는가?
[ ] 기존 메뉴 이동이 정상인가?
[ ] 새 기능이 정상 작동하는가?
[ ] 기존 기능이 망가지지 않았는가?
[ ] 잘못된 입력을 처리하는가?
[ ] 오류 발생 시 프로그램이 유지되는가?
[ ] 불필요한 import가 없는가?
[ ] 디버깅용 출력이 남아 있지 않은가?
[ ] 실제 DB나 CSV가 Git에 포함되지 않았는가?
[ ] 관련 문서를 수정했는가?
[ ] 하나의 의미 있는 커밋으로 저장했는가?
```

---

# 33. 리팩터링 중 문제 발생 시 대응

리팩터링 후 오류가 발생하면 한꺼번에 여러 부분을 수정하지 않습니다.

권장 순서:

```text
1. 오류 메시지 확인
2. 마지막으로 변경한 파일 확인
3. 변경 범위 안에서 원인 검색
4. 이전 커밋과 비교
5. 필요한 최소 부분만 수정
6. 다시 실행
```

원인을 찾기 어려우면 마지막 정상 커밋으로 돌아가 변경을 더 작은 단계로 나눕니다.

```text
큰 변경 실패
→ 정상 커밋 복원
→ 변경을 두세 단계로 분리
→ 각 단계 실행 확인
```

---

# 34. 장기 리팩터링 방향

사람 관리 기능이 안정되면 동일한 구조를 다른 도메인에 적용합니다.

```text
ProjectMenu
→ ProjectService
→ ProjectRepository
→ project 테이블
```

```text
WorkSiteMenu
→ WorkSiteService
→ WorkSiteRepository
→ work_site 테이블
```

```text
PayrollMenu
→ PayrollService
→ PayrollRepository
→ payroll 테이블
```

새로운 도메인을 추가할 때 사람 관리 구조를 그대로 복사하지 않습니다.

사람 관리에서 실제로 효과가 검증된 패턴만 적용합니다.

---

# 35. 최종 리팩터링 원칙

```text
현재 정상 작동하는 기능을 먼저 보존한다.

한 번에 하나의 책임만 변경한다.

작은 변경 후 반드시 실행한다.

정상 작동하는 시점마다 커밋한다.

메뉴는 사용자와 대화한다.

Service는 업무를 판단한다.

Repository는 데이터베이스를 다룬다.

Model은 데이터를 표현한다.

Main은 객체를 생성하고 연결한다.

중복은 줄이되 과도한 추상화는 피한다.

실제 필요가 생기기 전에 구조를 만들지 않는다.

기능 추가와 구조 변경을 가능한 한 분리한다.

리팩터링 후에도 사용자 동작은 유지되어야 한다.

코드를 변경하면 관련 문서도 함께 갱신한다.
```

현재 프로젝트의 가장 중요한 리팩터링 목표는 다음 한 줄로 정리할 수 있습니다.

```text
현재 완성된 CLI 메뉴 구조를 유지하면서,
사람 직접 등록 기능을
UI → Service → Repository → SQLite까지 연결한다.
```

이 흐름을 안정적으로 완성한 뒤 조회, 수정, 삭제, CSV 등록 순서로 확장합니다.