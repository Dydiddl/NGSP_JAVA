# NGSP_JAVA

# 프로잭트 진행 중 메모 사항

다음 commit 할 것 -> PersonNormalizer.java 작성

1. PersonCreate 모델 확인 v
2. PersonNormalizer 확인 또는 작성 v
3. PersonValidator 작성 
4. PersonRepository 저장 메서드 작성
5. PersonRegistrationService 작성
6. PersonInputReader 작성
7. PersonMenu 연결
8. 직접 입력 통합 테스트
9. CSV 등록으로 확장

> **Namgang Landscape System (Java Edition)**  
> 조경 업무를 관리하기 위한 터미널(CLI) 기반 ERP 시스템

---

# 프로젝트 소개

NGSP_JAVA는 남강조경 업무를 체계적으로 관리하기 위해 개발 중인
Java 기반 CLI 프로그램입니다.

본 프로젝트는 단순한 CRUD 프로그램이 아니라,
회사 업무를 객체(Object) 중심으로 관리할 수 있는 ERP 시스템을 목표로 합니다.

현재는 사람(Person) 관리 기능부터 개발을 시작하여
향후 공사(Project), 현장(Work Site), 계약, 급여 등의 기능으로 확장될 예정입니다.

---

# 개발 목표

- 객체지향(OOP)를 기반으로 한 설계
- 역할별 책임 분리
- 유지보수가 쉬운 구조
- SQLite 기반 로컬 데이터베이스
- CSV 대량 등록 지원
- 콘솔(CLI) 기반 인터페이스
- 향후 GUI 및 Web 확장 가능하도록 설계

---

# 프로젝트 구조

```text
src
├── Main.java
│
├── config
│   ├── CsvConfig.java
│   ├── DatabaseConfig.java
│   ├── PathConfig.java
│   └── UiConfig.java
│
├── data
│   ├── database
│   └── input
│
├── model
│   ├── Person.java
│   └── PersonCreate.java
│
└── ui
    ├── input
    │   └── MenuInputReader.java
    │
    └── menu
        ├── MainMenu.java
        ├── PersonMenu.java
        └── PersonRegistrationMenu.java
```

---

# 현재 구현 상태

현재 프로젝트는 **CLI 구조를 먼저 구축한 상태**입니다.

구현 완료

- Main 실행
- Main Menu
- Person Menu
- Person Registration Menu
- 메뉴 이동
- 입력 처리
- UI 구조

개발 예정

- SQLite 연결
- Repository
- Service
- Person 등록
- Person 조회
- Person 수정
- Person 삭제
- CSV Import

---

# 실행 흐름

```text
Main
    │
    ▼
MainMenu
    │
    ▼
PersonMenu
    │
    ▼
PersonRegistrationMenu
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
SQLite
```

---

# 개발 원칙

이 프로젝트는 다음 원칙을 중요하게 생각합니다.

## 1. 책임 분리

각 클래스는 하나의 역할만 담당합니다.

예)

- UI는 화면만 출력
- Service는 비즈니스 로직 처리
- Repository는 데이터베이스 처리
- Model은 데이터 표현

---

## 2. 의존성 최소화

UI가 SQL을 직접 실행하지 않습니다.

```
UI
 ↓
Service
 ↓
Repository
 ↓
SQLite
```

---

## 3. 객체 중심 설계

데이터베이스보다 객체를 먼저 설계합니다.

예)

- Person
- Project
- WorkSite
- Company

각 객체는 자신의 책임을 가집니다.

---

## 4. 작은 기능부터 완성

큰 기능을 한 번에 구현하지 않습니다.

예)

```
사람 등록
↓

조회

↓

수정

↓

삭제

↓

CSV 등록
```

각 기능을 완전히 동작하도록 만든 후 다음 단계로 진행합니다.

---

# 향후 개발 계획

## Person

- 직접 등록
- CSV 등록
- 조회
- 수정
- 삭제

---

## Project

- 공사 등록
- 공사 조회
- 공사 수정

---

## Work Site

- 현장 관리
- 작업 관리

---

## Daily Work

- 출근
- 작업 기록
- 작업 시간

---

## Payroll

- 급여 계산
- 지급 관리

---

# 개발 환경

- Java
- SQLite
- JDBC
- IntelliJ IDEA
- Git
- GitHub

---

# 프로젝트 문서

| 문서 | 설명 |
|------|------|
| README.md | 프로젝트 소개 |
| docs/analysis_results.md | 프로젝트 구조 분석 |
| docs/architecture.md | 아키텍처 설계 |
| docs/database_design.md | 데이터베이스 설계 |
| docs/changelog.md | 변경 이력 |

---

# 개발 철학

이 프로젝트는

> **"작동하는 코드보다 유지보수 가능한 구조를 먼저 만든다."**

라는 원칙으로 개발하고 있습니다.

기능 구현보다 구조를 먼저 완성하여,
장기간 유지보수 가능한 ERP 시스템을 목표로 합니다.