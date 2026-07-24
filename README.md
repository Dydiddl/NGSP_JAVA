# NGSP_JAVA

> **Namgang Landscape System (Java Edition)**
> Java와 SQLite를 기반으로 개발 중인 터미널(CLI) 기반 ERP 시스템

---

# 프로젝트 소개

NGSP_JAVA는 **남강조경**의 업무를 체계적으로 관리하기 위해 개발 중인 Java 기반 ERP 프로젝트입니다.

단순한 CRUD 프로그램이 아니라, 회사의 모든 업무를 **객체(Object)** 중심으로 관리할 수 있는 장기적인 시스템을 목표로 합니다.

현재는 **사람(Person) 관리 기능**을 중심으로 개발 중이며, 향후 공사(Project), 현장(Work Site), 급여(Payroll), 계약(Contract) 등으로 확장될 예정입니다.

---

# 개발 목표

* 객체지향(OOP) 기반 설계
* 계층형 아키텍처 적용
* 역할별 책임 분리
* 유지보수가 쉬운 구조
* SQLite 기반 로컬 데이터베이스
* CSV 대량 등록 지원
* CLI 기반 인터페이스
* GUI 및 Web으로 확장 가능한 구조

---

# 기술 스택

* Java
* Maven
* SQLite
* JDBC
* Git / GitHub
* IntelliJ IDEA

---

# 프로젝트 구조

```text
src
├── Main.java
│
├── config
│   ├── CsvConfig
│   ├── DatabaseConfig
│   ├── PathConfig
│   ├── PersonConfig
│   └── UiConfig
│
├── database
│   ├── DatabaseConnection
│   └── DatabaseInitializer
│
├── model
│   ├── Person
│   └── PersonCreate
│
├── normalizer
│   └── PersonNormalizer
│
├── repository
│   └── PersonRepository
│
├── service
│   ├── PersonRegistrationService
│   └── PersonLookupService
│
├── validator
│   ├── CommonValidator
│   └── PersonValidator
│
└── ui
    ├── input
    │   ├── MenuInputReader
    │   └── PersonInputReader
    │
    ├── output
    │   └── PersonOutput
    │
    └── menu
        ├── MainMenu
        ├── PersonMenu
        ├── PersonRegistrationMenu
        └── PersonLookupMenu
```

---

# 현재 구현 완료

## 데이터베이스

* SQLite 자동 생성
* Gender 테이블 생성
* Person 테이블 생성
* 기본 성별 데이터 자동 등록
* Foreign Key 활성화

---

## Person 등록

* 직접 입력
* 입력값 표준화(Normalizer)
* 입력값 검증(Validator)
* Repository 저장
* Service 계층 분리

---

## Person 조회

* 전체 목록 조회
* Service → Repository 구조
* Person 객체 반환

---

## UI

* Main Menu
* Person Menu
* Person Registration Menu
* Person Lookup Menu
* Enter 입력 대기 기능
* 입력 전용 Reader 분리

---

# 현재 개발 중

* Person 수정
* Person 삭제
* CSV 일괄 등록
* 검색 기능
* 예외 처리 개선

---

# 실행 흐름

```text
Main
│
├── DatabaseInitializer
│
└── MainMenu
      │
      └── PersonMenu
              │
              ├── PersonRegistrationMenu
              │       │
              │       ▼
              │ PersonRegistrationService
              │       │
              │       ▼
              │ PersonRepository
              │       │
              │       ▼
              │    SQLite
              │
              └── PersonLookupMenu
                      │
                      ▼
              PersonLookupService
                      │
                      ▼
              PersonRepository
                      │
                      ▼
                   SQLite
```

---

# 프로젝트 설계 원칙

## 1. 역할별 책임 분리

각 클래스는 하나의 책임만 가집니다.

| 계층         | 역할         |
| ---------- | ---------- |
| UI         | 화면 출력 및 입력 |
| Service    | 업무 로직      |
| Repository | 데이터베이스 접근  |
| Model      | 객체 표현      |
| Validator  | 데이터 검증     |
| Normalizer | 입력값 표준화    |

---

## 2. 의존성 최소화

```text
UI
 ↓
Service
 ↓
Repository
 ↓
SQLite
```

UI는 SQL을 직접 실행하지 않습니다.

---

## 3. 객체 중심 설계

데이터보다 객체를 먼저 설계합니다.

예)

* Person
* Project
* WorkSite
* Company
* Payroll

객체가 자신의 책임을 가지도록 설계합니다.

---

## 4. 작은 기능부터 완성

기능을 하나씩 완전히 동작하도록 만든 뒤 다음 기능으로 확장합니다.

```text
사람 등록
    ↓
사람 조회
    ↓
사람 수정
    ↓
사람 삭제
    ↓
CSV 등록
```

---

# 향후 개발 계획

## Person

* 수정
* 삭제
* 상세 조회
* 검색
* CSV Import

---

## Project

* 공사 등록
* 공사 종류 관리
* 공사 조회
* 공사 수정

---

## Work Site

* 현장 관리
* 작업 관리
* 담당자 배정

---

## Payroll

* 급여 계산
* 지급 관리
* 수당 계산

---

## ERP 확장

* 계약 관리
* 발주처 관리
* 협력업체 관리
* 장비 관리
* 자재 관리

---

# 프로젝트 문서

```text
docs/
├── architecture.md
├── database_design.md
├── analysis_results.md
├── changelog.md
└── coding_rules.md
```

---

# 개발 철학

> **"기능보다 구조를 먼저 만든다."**

> **"작동하는 코드보다 유지보수 가능한 코드를 만든다."**

NGSP_JAVA는 단기적인 기능 구현보다 **장기간 유지보수가 가능한 ERP 시스템**을 목표로 지속적으로 발전시키고 있습니다.
