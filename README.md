# NGSP_JAVA

> **Namgang Landscape System Project — Java Edition**
> Java와 SQLite를 기반으로 개발 중인 터미널(CLI) 기반 회사 업무 관리 시스템

---

## 프로젝트 소개

NGSP_JAVA는 **남강조경의 업무를 체계적으로 관리하기 위해 개발 중인 Java 기반 ERP 프로젝트**입니다.

단순한 CRUD 프로그램을 만드는 것에 그치지 않고, 회사에서 발생하는 사람, 공사, 현장, 출근, 급여, 계약 등의 업무를 객체 중심으로 관리할 수 있는 장기적인 시스템을 목표로 합니다.

현재는 **사람(Person) 관리 기능**을 중심으로 기본 구조를 구축하고 있으며, 이후 공사(Project), 현장(Work Site), 출근(Attendance), 급여(Payroll), 계약(Contract) 등의 기능으로 확장할 예정입니다.

---

## 현재 버전

```text
v0.2.0
```

### v0.2.0 주요 내용

* 사람 정보 수정 기능 구현
* `PersonUpdateMenu` 구현
* `PersonUpdateService` 구현
* 사람 수정 Repository 메서드 구현
* 이름, 전화번호, 주소, 은행, 계좌번호 수정 지원
* 은행과 계좌번호 동시 수정 지원
* `Menu → Service → Repository` 계층 구조 정립
* `Main.java`에서 객체 생성과 의존성 연결

---

## 개발 목표

* 객체지향 프로그래밍 기반 설계
* 계층형 아키텍처 적용
* 클래스별 책임 분리
* 유지보수하기 쉬운 구조
* SQLite 기반 로컬 데이터베이스 사용
* 터미널 기반 사용자 인터페이스
* CSV 파일을 이용한 대량 등록 지원
* 공사 및 회사 업무 관리 기능으로 확장
* 향후 GUI 또는 Web 환경으로 확장 가능한 구조

---

## 기술 스택

* Java 21 LTS
* Maven
* SQLite
* JDBC
* Git
* GitHub
* IntelliJ IDEA

---

## 프로젝트 구조

```text
src/main/java
├── Main.java
│
├── config
│   ├── CsvConfig.java
│   ├── DatabaseConfig.java
│   ├── PathConfig.java
│   ├── PersonConfig.java
│   └── UiConfig.java
│
├── database
│   ├── DatabaseConnection.java
│   └── DatabaseInitializer.java
│
├── model
│   ├── Person.java
│   └── PersonCreate.java
│
├── normalizer
│   └── PersonNormalizer.java
│
├── repository
│   └── PersonRepository.java
│
├── service
│   ├── PersonRegistrationService.java
│   ├── PersonSearchService.java
│   └── PersonUpdateService.java
│
├── validator
│   ├── CommonValidator.java
│   └── PersonValidator.java
│
└── ui
    ├── input
    │   ├── MenuInputReader.java
    │   └── PersonInputReader.java
    │
    ├── output
    │   └── PersonOutput.java
    │
    └── menu
        ├── MainMenu.java
        │
        └── person
            ├── PersonMenu.java
            ├── PersonRegistrationMenu.java
            ├── PersonSearchMenu.java
            └── PersonUpdateMenu.java
```

---

## 계층별 역할

| 계층           | 역할                         |
| ------------ | -------------------------- |
| `Main`       | 객체 생성 및 의존성 연결             |
| `UI`         | 메뉴 출력, 사용자 입력, 결과 안내       |
| `Service`    | 업무 흐름, 정규화, 검증, 처리 결과 확인   |
| `Repository` | SQL 실행 및 데이터베이스 접근         |
| `Model`      | 프로그램에서 사용하는 객체 표현          |
| `Normalizer` | 입력값을 저장 형식에 맞게 표준화         |
| `Validator`  | 입력값이 규칙에 맞는지 검사            |
| `Config`     | 경로, 데이터베이스, 입력 규칙 등의 설정 관리 |
| `Database`   | SQLite 연결 및 테이블 초기화        |

---

## 기본 실행 구조

```text
Main
 ├── DatabaseInitializer
 ├── Input Reader 생성
 ├── Repository 생성
 ├── Service 생성
 ├── Output 생성
 ├── Menu 생성
 └── MainMenu.run()
```

기능 처리 흐름은 다음과 같습니다.

```text
사용자
  ↓
Menu
  ↓
Service
  ↓
Repository
  ↓
SQLite
```

결과는 반대 방향으로 반환됩니다.

```text
SQLite
  ↓
Repository
  ↓
Service
  ↓
Menu
  ↓
사용자
```

---

## 설계 원칙

### 1. UI는 사용자와의 상호작용만 담당한다

Menu 클래스는 다음 역할을 담당합니다.

* 메뉴 출력
* 사용자 입력
* Service 호출
* 성공 또는 오류 메시지 출력

Menu에서 입력값을 직접 정규화하거나 검증하지 않습니다.

Menu에서 Repository를 직접 호출하지 않습니다.

```text
올바른 구조

Menu
  ↓
Service
  ↓
Repository
```

```text
사용하지 않는 구조

Menu
  ↓
Repository
```

---

### 2. Service는 업무 처리를 담당한다

Service는 사용자 입력을 전달받아 다음 순서로 처리합니다.

```text
원본 입력
  ↓
Normalizer
  ↓
Validator
  ↓
Repository
  ↓
처리 결과 확인
```

예를 들어 사람 이름 수정은 다음과 같이 처리됩니다.

```text
PersonUpdateMenu
  ↓
PersonUpdateService.updateName()
  ↓
PersonNormalizer.normalizeName()
  ↓
PersonValidator.validateName()
  ↓
PersonRepository.updateName()
  ↓
수정된 행 수 확인
```

---

### 3. Repository는 데이터베이스 접근만 담당한다

Repository는 다음 작업을 담당합니다.

* `INSERT`
* `SELECT`
* `UPDATE`
* 향후 `DELETE`
* `PreparedStatement` 생성
* SQL 파라미터 설정
* 조회 결과를 객체로 변환
* 변경된 행 수 반환

Repository는 사용자에게 메시지를 출력하지 않습니다.

---

### 4. 입력은 정규화 후 검증한다

입력값은 다음 순서로 처리합니다.

```text
입력
  ↓
정규화
  ↓
검증
  ↓
저장 또는 수정
```

예:

```text
010-1234-5678
  ↓
01012345678
  ↓
전화번호 규칙 검증
  ↓
데이터베이스 저장
```

---

### 5. 작은 기능을 완성한 뒤 확장한다

기능을 한꺼번에 구현하지 않고 하나씩 완성합니다.

```text
사람 등록
  ↓
사람 조회
  ↓
사람 수정
  ↓
예외 처리 개선
  ↓
사람 삭제
  ↓
CSV 등록
  ↓
공사 관리
```

---

## 현재 구현 완료

### 데이터베이스

* SQLite 데이터베이스 자동 생성
* `gender` 테이블 생성
* `person` 테이블 생성
* 기본 성별 데이터 자동 등록
* Foreign Key 활성화
* 프로그램 실행 시 데이터베이스 초기화

---

### Person 등록

* 사람 직접 입력
* 이름 입력
* 전화번호 입력
* 성별 입력
* 주소 입력
* 은행 입력
* 계좌번호 입력
* 입력값 정규화
* 입력값 검증
* Repository 저장
* 생성된 Person ID 반환
* Service 계층 분리

---

### Person 조회

* 전체 사람 목록 조회
* 이름 검색
* Person ID 검색
* 성별 검색
* `Optional<Person>`을 이용한 단일 결과 처리
* `List<Person>`을 이용한 복수 결과 처리
* 조회 결과 테이블 출력
* 전화번호 출력 형식 적용
* 계좌번호 출력 형식 적용
* `PersonSearchMenu`
* `PersonSearchService`

---

### Person 수정

* 이름 수정
* 전화번호 수정
* 주소 수정
* 은행 수정
* 계좌번호 수정
* 은행과 계좌번호 동시 수정
* 수정된 행 수 확인
* 수정 실패 감지
* `PersonUpdateMenu`
* `PersonUpdateService`
* Repository UPDATE 메서드 구현

수정 기능의 기본 흐름은 다음과 같습니다.

```text
PersonUpdateMenu
  ↓
Person ID 입력
  ↓
변경할 값 입력
  ↓
PersonUpdateService
  ↓
정규화
  ↓
검증
  ↓
PersonRepository
  ↓
SQLite UPDATE
```

---

### UI

* Main Menu
* Person Menu
* Person Registration Menu
* Person Search Menu
* Person Update Menu
* 입력 전용 Reader 분리
* 출력 전용 클래스 분리
* Enter 입력 대기 기능
* 반복 메뉴 구조
* 잘못된 메뉴 번호 처리

---

## 현재 개발 중

### 예외 처리 개선

현재 Validator와 Service에서 발생한 예외가 Menu에서 처리되지 않으면 프로그램이 종료될 수 있습니다.

예:

```text
지원하지 않는 은행 입력
  ↓
IllegalArgumentException 발생
  ↓
처리되지 않은 예외
  ↓
프로그램 종료
```

다음 단계에서는 Menu 계층에서 예외를 처리하여 프로그램이 종료되지 않도록 개선할 예정입니다.

목표 흐름:

```text
잘못된 입력
  ↓
오류 메시지 출력
  ↓
프로그램 유지
  ↓
수정 메뉴로 복귀 또는 재입력
```

---

### 수정 대상 확인 개선

존재하지 않는 Person ID를 입력했을 때 다음과 같이 보다 명확한 메시지를 제공하도록 개선할 예정입니다.

```text
해당 ID의 사람을 찾을 수 없습니다.
```

---

### 데이터베이스 예외 처리

전화번호처럼 `UNIQUE` 제약조건이 있는 값을 중복 입력하거나 수정했을 때 발생하는 SQLite 예외를 사용자에게 이해하기 쉬운 메시지로 변환할 예정입니다.

---

## 다음 개발 계획

### Person 관리

* 예외 처리 개선
* 잘못된 입력 재입력 기능
* 사람 삭제
* 사람 상세 조회
* CSV 일괄 등록
* 검색 조건 확장
* 수정 전 기존 정보 출력
* 수정 완료 후 변경 결과 확인
* 중복 전화번호 오류 처리

---

### 코드 개선

* `PersonRepository`의 중복 객체 변환 코드 분리
* `ResultSet → Person` 변환 메서드 작성
* 수정 결과 확인 코드 중복 제거
* `updatedRows` 변수명 통일
* 코드 스타일 통일
* Person ID 타입 통일 검토
* Service와 Repository의 검증 책임 정리
* 테스트 코드 작성

---

### Project 관리

사람 관리 기능을 기반으로 공사 관리 기능을 추가할 예정입니다.

예상 메뉴:

```text
공사 관리
1. 공사 등록
2. 공사 조회
3. 공사 수정
4. 공사 삭제
0. 메인 메뉴로 돌아가기
```

예상 구조:

```text
ProjectMenu
 ├── ProjectRegistrationMenu
 ├── ProjectSearchMenu
 └── ProjectUpdateMenu
```

예상 계층:

```text
ProjectMenu
  ↓
ProjectService
  ↓
ProjectRepository
  ↓
SQLite
```

---

### 공사 종류

| 코드  | 공사 종류   |
| --- | ------- |
| P01 | 조경식재    |
| P02 | 조경시설물   |
| P03 | 풀베기 용역  |
| P04 | 전정공사    |
| P05 | 위험목 제거  |
| P06 | 기타 건설공사 |

---

### Work Site

* 현장 관리
* 작업 구역 관리
* 작업 내용 기록
* 담당자 배정
* 공사와 현장 연결

---

### Attendance

* 근로자 출근 기록
* 공사별 출근 관리
* 일일 작업 기록
* 근무일수 집계

---

### Payroll

* 급여 계산
* 일당 관리
* 수당 계산
* 지급 기록
* 공사별 인건비 집계

---

### ERP 확장

* 계약 관리
* 발주처 관리
* 협력업체 관리
* 장비 관리
* 자재 관리
* 안전관리
* 문서 관리
* 사진 및 첨부파일 관리

---

## 실행 방법

### 1. 저장소 복제

```bash
git clone https://github.com/Dydiddl/NGSP_JAVA.git
```

### 2. 프로젝트 폴더 이동

```bash
cd NGSP_JAVA
```

### 3. Maven 컴파일

```bash
mvn clean compile
```

### 4. 테스트 실행

```bash
mvn test
```

현재 자동화된 테스트 코드가 없으면 빌드 및 컴파일 상태 확인 용도로 실행합니다.

### 5. IntelliJ IDEA에서 실행

```text
src/main/java/Main.java
```

파일의 `main()` 메서드를 실행합니다.

---

## Git 버전 관리

현재 주요 버전:

```text
v0.1.0
└── 사람 관리 기본 구조 완성

v0.2.0
└── 사람 수정 기능과 계층 구조 완성
```

버전 관리 기준:

```text
MAJOR
기존 구조와 호환되지 않는 큰 변경

MINOR
새로운 기능 추가

PATCH
버그 수정 및 사용성 개선
```

예상 다음 버전:

```text
v0.2.1
└── 예외 처리 및 사용자 입력 오류 개선

v0.3.0
└── 공사 관리 기능 추가
```

---

## 프로젝트 문서

```text
docs/
├── architecture.md
├── architecture_design.md
├── database_design.md
├── ui_flow.md
├── coding_rules.md
├── refactoring_plan.md
├── regex_guide.md
└── TODO.md
```

문서 파일명은 실제 `docs` 디렉터리 구성에 따라 계속 갱신합니다.

---

## 현재 수동 테스트 항목

```text
1. 사람 정상 등록
2. 중복 전화번호 등록
3. 전체 사람 조회
4. 이름 검색
5. ID 검색
6. 성별 검색
7. 존재하는 ID의 이름 수정
8. 존재하지 않는 ID 수정
9. 올바르지 않은 전화번호 수정
10. 지원하지 않는 은행 입력
11. 은행만 수정
12. 계좌번호만 수정
13. 은행과 계좌번호 동시 수정
14. 수정 후 전체 조회로 결과 확인
15. 오류 발생 후 프로그램 유지 여부 확인
```

---

## 개발 철학

> **기능보다 구조를 먼저 만든다.**

> **작동하는 코드보다 유지보수 가능한 코드를 만든다.**

> **역할이 다른 코드는 서로 다른 클래스에 배치한다.**

> **반복을 예상해서 미리 추상화하지 않고, 실제 반복을 확인한 뒤 리팩터링한다.**

NGSP_JAVA는 단기적인 기능 구현보다 장기간 유지보수할 수 있는 회사 업무 관리 시스템을 목표로 지속적으로 발전시키고 있습니다.
