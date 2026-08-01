# Development of Namgang Landscape System Program

## 프로잭트 구조
```
src/main/java
├─ config
│  ├─ PathConfig.java
│  └─ UiConfig.java
│
├─ database
│  ├─ DatabaseConnection.java
│  └─ DatabaseInitializer.java
│
├─ model
│  ├─ Person.java
│  └─ PersonCreate.java
│
├─ normalizer
│  └─ PersonNormalizer.java
│
├─ repository
│  └─ PersonRepository.java
│
├─ service
│  └─ PersonRegistrationService.java
│
├─ validator
│  └─ PersonValidator.java
│
└─ ui
    ├─ input
    │  ├─ MenuInputReader.java
    │  └─ PersonInputReader.java
    │
    └─ menu
        ├─ MainMenu.java
        └─ PersonMenu.java
```


## Program Flowchart
```mermaid
flowchart TD
    A([프로그램 실행]) --> B[데이터베이스 초기화]
    B --> DB[(database.db)]
    B --> C{메인 메뉴}

    C -->|1. 인사 관리| D[인사 관리 메뉴]
    C -->|2. 공사 관리| E[공사 관리 메뉴]
    C -->|0. 종료| F([프로그램 종료])

    D --> D1[사람 등록]
    D --> D2[사람 조회]
    D --> D3[사람 수정]
    D --> D4[퇴사 관리]
    D -->|뒤로가기| C

    E --> E1[공사 등록]
    E --> E2[공사 조회]
    E --> E3[현장 관리]
    E -->|뒤로가기| C

    D1 -.-> DB
    D2 -.-> DB
    D3 -.-> DB
    D4 -.-> DB

    E1 -.-> DB
    E2 -.-> DB
    E3 -.-> DB
```

### 인사관리 업무 흐름도
```mermaid
flowchart TD
    A([인사 관리 메뉴 진입]) --> B{인사 관리 메뉴}

    B -->|1. 사람 등록| C[사람 등록 업무]
    B -->|2. 사람 조회| D[사람 조회 업무]
    B -->|3. 사람 수정| E[사람 수정 업무]
    B -->|4. 퇴사 처리| F[퇴사 처리 업무]
    B -->|0. 뒤로가기| G([메인 메뉴로 복귀])

    C --> B
    D --> B
    E --> B
    F --> B
```

#### 사람 등록 업무 상세 흐름도
```mermaid
flowchart TD
A([사람 등록 시작]) --> B{등록 방법 선택}

    B -->|1. 직접 입력| C[직접 입력 등록]
    B -->|2. CSV 일괄 등록| D[CSV 일괄 등록]
    B -->|0. 뒤로가기| E([인사 관리 메뉴로 복귀])

    C --> F[등록 결과 출력]
    D --> F

    F --> E
```

##### 직접 입력 등록 상세 흐름도
```mermaid
flowchart TD
    A([직접 입력 등록 시작]) --> B[사람 정보 입력]
    B --> C[입력값 정규화]
    C --> D{입력값 검증}

    D -->|실패| E[입력 오류 메시지 출력]
    E --> B

    D -->|통과| F[PersonCreate 객체 생성]
    F --> G[데이터베이스 저장 요청]
    G --> H{저장 성공 여부}

    H -->|실패| I[데이터베이스 오류 메시지 출력]
    I --> M([사람 등록 메뉴로 복귀])

    H -->|성공| K[등록된 사람 정보 출력]
    K --> L{추가 등록 여부}

    L -->|예| B
    L -->|아니오| M
```

##### CSV 일괄 등록 상세 흐름도
```mermaid
flowchart TD
    A([CSV 일괄 등록 시작]) --> B[CSV 파일 선택 또는 경로 확인]
    B --> C{파일이 존재하는가?}

    C -->|아니오| D[파일 없음 오류 출력]
    D --> Z([사람 등록 메뉴로 복귀])

    C -->|예| E[CSV 파일 열기]
    E --> F{필수 헤더가 올바른가?}

    F -->|아니오| G[헤더 오류 메시지 출력]
    G --> Z

    F -->|예| H[다음 CSV 행 읽기]
    H --> I{읽을 행이 남아 있는가?}

    I -->|아니오| Q[전체 처리 결과 요약]
    Q --> Z

    I -->|예| J[행 데이터 변환]
    J --> K[행 데이터 정규화]
    K --> L{행 데이터 검증}

    L -->|실패| M[행 번호와 오류 내용 기록]
    M --> H

    L -->|통과| N[PersonCreate 객체 생성]
    N --> O[데이터베이스 저장]
    O --> P{저장 성공 여부}

    P -->|성공| R[성공 건수 기록]
    P -->|실패| S[실패 건수와 오류 기록]

    R --> H
    S --> H
```

#### 조회 업무 흐름도
```mermaid
flowchart TD
    A([사람 조회 시작]) --> B{조회 방법 선택}

    B -->|1. 전체 조회| C[전체 사람 목록 조회]
    B -->|2. 이름 검색| D[이름 입력]
    B -->|3. 전화번호 검색| E[전화번호 입력]
    B -->|4. 재직 상태 조회| F[재직 상태 선택]
    B -->|0. 뒤로가기| Z([인사 관리 메뉴로 복귀])

    D --> G[이름 조건으로 데이터베이스 조회]
    E --> H[전화번호 정규화]
    H --> I[전화번호 조건으로 데이터베이스 조회]
    F --> J[재직 상태 조건으로 데이터베이스 조회]

    C --> K[조회 결과 출력]
    G --> K
    I --> K
    J --> K

    K --> L{조회 결과가 있는가?}
    L -->|예| M[사람 목록 출력]
    L -->|아니오| N[검색 결과 없음 출력]

    M --> B
    N --> B
```

#### 수정 업무 흐름도
```mermaid
flowchart TD
    A([사람 수정 시작]) --> B[수정 대상 검색]
    B --> C[검색 결과 출력]
    C --> D{수정 대상이 존재하는가?}

    D -->|아니오| E[대상을 찾을 수 없음 출력]
    E --> Z([인사 관리 메뉴로 복귀])

    D -->|예| F[수정 대상 선택]
    F --> G[현재 정보 출력]
    G --> H{수정할 항목 선택}

    H -->|이름| I[새 이름 입력]
    H -->|전화번호| J[새 전화번호 입력]
    H -->|주소| K[새 주소 입력]
    H -->|0. 취소| Z

    I --> L[입력값 정규화 및 검증]
    J --> L
    K --> L

    L --> M{입력값이 올바른가?}
    M -->|아니오| N[오류 메시지 출력]
    N --> H

    M -->|예| O{수정을 확정할 것인가?}
    O -->|아니오| H
    O -->|예| P[데이터베이스 수정]
    P --> Q[수정 결과 출력]
    Q --> Z
```
#### 퇴사 처리 흐름도
```mermaid
flowchart TD
    A([퇴사 처리 시작]) --> B[퇴사 대상 검색]
    B --> C[대상 정보 출력]
    C --> D{대상이 존재하는가?}

    D -->|아니오| E[대상을 찾을 수 없음 출력]
    E --> Z([인사 관리 메뉴로 복귀])

    D -->|예| F{퇴사 처리할 것인가?}
    F -->|아니오| Z

    F -->|예| G[퇴사일 입력]
    G --> H{입력값이 올바른가?}

    H -->|아니오| I[오류 메시지 출력]
    I --> G

    H -->|예| J[재직 상태를 퇴사로 변경]
    J --> K[퇴사일 저장]
    K --> L[데이터베이스 반영]
    L --> M[퇴사 처리 결과 출력]
    M --> Z
```


## 데이터베이스 ER 다이어그램

### Person
```mermaid
erDiagram

    PERSON {
        INTEGER id PK
        TEXT name
        TEXT phone
        INTEGER gender_id FK
        TEXT address
        INTEGER bank_id FK
        TEXT account_number
    }

    GENDER {
        INTEGER id PK
        TEXT name
    }
    BANK {
        INTEGER id PK
        TEXT name
    }
    
    GENDER ||--o{ PERSON : has
    BANK ||--o{ PERSON : has
```

### Project

```mermaid
erDiagram
    PROJECT {
        INTEGER id PK
        TEXT name
        TEXT project_code FK
    }

    PROJECT_CODE {
        TEXT code PK
        TEXT name
    }

    PROJECT_CODE ||--o{ PROJECT : classifies
```
