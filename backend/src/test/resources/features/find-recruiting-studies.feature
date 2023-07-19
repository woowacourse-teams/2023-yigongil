Feature: 현재 모집 중인 스터디 목록을 조회한다

  Scenario: 모집 중인 스터디 목록을 조회한다
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "자바1", "5", "2023.05.12", "2", "1w", "안녕1"를 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    Given post 요청을 "/v1/studies" 로 보낸다.
    Given "자바2", "7", "2023.05.25", "3", "3w", "안녕2"를 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    Given post 요청을 "/v1/studies" 로 보낸다.
    When 모집 중인 스터디 "0" 페이지를 요청한다.
    Then 모집 중인 스터디를 확인할 수 있다.
