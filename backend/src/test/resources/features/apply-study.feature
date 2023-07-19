Feature: 스터디 참여 신청을 한다.

  Scenario Outline: 스터디에 정상적으로 참여 신청을 한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "자바", "5", "2023.05.12", "2", "1w", "안녕"를 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    Given post 요청을 "/v1/studies" 로 보낸다.
    Given 스터디 지원자 정보를 입력한다.
    When post 요청을 "<url>" 로 보낸다.
    Then Member가 Study에 참여 신청을 한다.

    Examples:
      | url                      |  |  |  |  |  |  |
      | /v1/studies/1/applicants |  |  |  |  |  |  |
