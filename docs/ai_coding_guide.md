# AI Coding Guide

## Project Architecture

Main
→ UI
→ Service
→ Repository
→ SQLite

## Responsibilities

- Main: 객체 생성과 연결
- UI: 입력, 출력, 메뉴 이동
- Service: 검증, 정규화, 업무 규칙
- Repository: SQL과 DB 접근
- Model: 데이터 표현

## Mandatory Rules

- Scanner는 Main에서 하나만 생성한다.
- 모든 입력은 nextLine() 기반으로 처리한다.
- UI에서 SQL을 실행하지 않는다.
- Repository에서 화면 출력하지 않는다.
- Service에서 Scanner를 사용하지 않는다.
- PreparedStatement를 사용한다.
- DB 자원은 try-with-resources로 닫는다.
- 하위 메뉴에서 System.exit()를 사용하지 않는다.
- 기능 결과 후 Enter 입력을 기다린다.
- 실제 DB와 CSV는 Git에 올리지 않는다.

## Current Priority

현재 목표는 사람 관리 CRUD 완성이다.

작업 순서:
1. 직접 등록
2. 전체 조회
3. 상세 조회
4. 수정
5. 삭제
6. CSV 등록

## Change Rules

- 한 번에 하나의 기능만 수정한다.
- 요청받지 않은 파일은 수정하지 않는다.
- 기존 구조를 임의로 재설계하지 않는다.
- 새로운 클래스 생성 전 기존 역할과 중복되는지 확인한다.
- 코드 작성 후 수정한 파일과 이유를 설명한다.