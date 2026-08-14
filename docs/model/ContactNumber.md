# ContactNumber

## 목적

`ContactNumber`는 Person에게 다시 연락할 수 있는 대한민국 전화번호를 표현하는 값 객체다. Person은 최소 하나 이상의 ContactNumber를 가지며 여러 연락처를 가질 수 있다.

## 책임

ContactNumber는 입력 표현을 숫자만 남긴 번호로 정규화하고, 지원하는 전화번호 구조인지 검증하며, 번호 체계에서 종류를 파생한다. 번호 종류를 사용자에게 별도로 입력받지 않는다.

현재 지원 종류는 다음과 같다.

- `MOBILE`
- `LANDLINE`
- `INTERNET_PHONE`
- `PERSONAL_NUMBER_SERVICE`

개인용·업무용 같은 사용 목적, 대표 연락처, 활성 여부와 이력은 ContactNumber의 현재 책임이 아니다.

## 동일성

동일성은 정규화된 번호를 기준으로 한다. 따라서 하이픈이나 공백 등 입력 표현이 달라도 정규화 결과가 같으면 같은 ContactNumber다.

이 동일성은 한 Person 내부 중복을 막는 데 사용한다. 여러 Person 사이의 중복 정책은 저장된 전체 Person을 확인해야 하므로 ContactNumber 자체가 책임지지 않는다.

## Person 간 중복 정책

- `MOBILE`은 개인 휴대전화로 취급하여 서로 다른 Person이 동시에 같은 번호를 가질 수 없다.
- `LANDLINE`, `INTERNET_PHONE`, `PERSONAL_NUMBER_SERVICE`는 여러 Person이 공유할 수 있는 업무 연락처로 취급한다.

이 정책은 현실의 모든 전화번호 사용 사례를 일반화한 규칙이 아니라 현재 NGSP의 업무 기준이다. 실제 업무에서 담당자 승계나 다른 사용 사례로 문제가 확인되면 재검토한다.

저장 구조와 DB 제약은 `docs/database/person-contact-number.md`에서 설명한다.
