# NGSP_JAVA

NGSP Java는 남강조경의 실제 업무를 지원하기 위해 Java와 SQLite로 개발하는 프로그램이다. 현재는 업무상 식별하고 다시 연락할 수 있는 Person 정보를 안정적으로 등록·조회하는 기반을 구축하고 있다.

## 현재 Person 범위

Person은 `displayName`과 최소 하나의 `ContactNumber`를 필수로 가지며, 확인된 실제 이름과 이메일은 선택 정보다. 근무·고용 상태, 주소, 성별, 계좌 같은 다른 업무 정보는 현재 Person 최소 모델의 책임이 아니다.

연락처는 Person과 1:N으로 별도 저장한다. 등록은 Person과 모든 연락처를 하나의 트랜잭션으로 처리하며 조회 시 모든 연락처를 보여준다. 대표 연락처와 연락처 수정 기능은 아직 제공하지 않는다.

현재 구현된 흐름은 다음과 같다.

- 터미널 UI에서 Person 등록
- 전체 Person 및 실제 이름 검색
- SQLite에 Person과 복수 연락처 저장
- 한 Person 내부 연락처 중복 방지
- Person 간 MOBILE 중복 방지 및 LANDLINE 공유 허용

## 실행과 검증

요구 환경은 `pom.xml`의 Java release 설정을 따른다.

```bash
mvn clean test
mvn exec:java -Dexec.mainClass=Main
```

현재 SQLite DB는 개발용이다. 과거 스키마 데이터 마이그레이션은 제공하지 않으며 스키마가 바뀌면 `data/database/database.db`를 재생성한다.

## Source of Truth

- Person의 업무 의미와 불변조건: `docs/model/person.md`
- ContactNumber의 의미: `docs/model/ContactNumber.md`
- Person–연락처 DB 구조: `docs/database/person-contact-number.md`
- 실제 구현과 테스트: `src/main`, `src/test`, `pom.xml`

상세 클래스 구조와 작업 이력은 이 문서에 복제하지 않고 코드와 Git History에서 확인한다.
