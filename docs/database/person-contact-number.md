# Person–ContactNumber 저장 구조

## 목적

Person과 연락처를 1:N 관계로 저장한다. Person의 식별 정보는 `person`, 연락처는 `person_contact_number`에 저장하며 Person 테이블에 단일 전화번호 열을 두지 않는다.

## 테이블

`person_contact_number`는 다음 값을 저장한다.

- `id`: 연락처 행의 별도 기본키
- `person_id`: 연락처가 속한 Person의 외래키
- `number`: `ContactNumber`가 정규화한 숫자 형식의 번호
- `type`: 같은 `ContactNumber`가 번호에서 파생한 `ContactType`

연락처는 Person 없이 저장할 수 없다. 외래키 삭제 동작은 별도로 지정하지 않아 참조 중인 Person 삭제를 허용하지 않는다. 현재 Person 삭제 기능은 제공하지 않는다.

`number`와 `type`의 의미 일관성은 애플리케이션이 하나의 `ContactNumber`에서 두 값을 함께 저장함으로써 보장한다. DB는 지원하는 enum 이름만 `type`으로 허용하며 번호에서 type을 다시 계산하지 않는다.

## 생성과 조회

Person 등록은 Person 행과 모든 연락처 행을 하나의 트랜잭션에서 저장한다. 연락처 저장이 하나라도 실패하면 Person 행도 롤백하므로 연락처 없는 Person이 등록되지 않는다.

Person 조회 시 소속된 모든 연락처를 함께 복원한다. 대표 연락처는 두지 않는다. 연락처 행의 `id`는 조회 순서를 안정적으로 유지하는 데 사용하지만 현재 업무상 우선순위를 뜻하지 않는다.

현재 연락처 삭제·수정 기능은 제공하지 않는다. 향후 연락처 변경 기능을 추가할 때도 Person이 최소 하나의 연락처를 유지해야 한다는 도메인 불변조건을 깨뜨릴 수 없다.

## 중복 제약

한 Person 내부의 동일 정규화 번호는 다음 유일 제약으로 방지한다.

```text
UNIQUE(person_id, number)
```

Person 간 정책은 연락처 종류별로 다르다.

| type | Person 간 동일 번호 | DB 반영 |
| --- | --- | --- |
| `MOBILE` | 허용하지 않음 | `type = 'MOBILE'`인 행에 `number` 부분 유일 인덱스 적용 |
| `LANDLINE` | 허용 | 전역 유일 제약 없음 |
| `INTERNET_PHONE` | 미확정 | 전역 유일 제약 없음 |
| `PERSONAL_NUMBER_SERVICE` | 미확정 | 전역 유일 제약 없음 |

미확정 두 종류에 제약이 없다는 사실은 중복 허용을 업무 규칙으로 확정한 것이 아니다. 현재는 저장을 막지 않으며, 실제 업무 사례를 확인한 뒤 정책과 기존 데이터 처리 방법을 함께 결정한다.

## 개발 DB

현재 SQLite DB는 개발용이며 기존 단일 전화번호 스키마의 데이터 마이그레이션을 제공하지 않는다. 스키마 전환 시 DB 파일을 재생성한다.

## 아직 결정하지 않은 사항

- `INTERNET_PHONE`, `PERSONAL_NUMBER_SERVICE`의 Person 간 중복 정책
- 연락처 수정·삭제와 최소 한 개 연락처 유지 흐름
- 연락처의 업무상 표시 순서 또는 우선순위
- 전화번호 담당자 승계와 이력 관리
