Feature: 프로필 정보를 업데이트한다

  Scenario Outline: 프로필 정보를 정상적으로 업데이트한다
    Given 닉네임 "<nickname>"과 간단 소개"<introduction>"을 정상적으로 입력한다
    When "<url>"로 patch 요청을 보낸다
    Then 변경된 정보를 확인할 수 있다

    Examples:
      | nickname | introduction | url |
      | 김김진진우우   | 간단 소개입니다 | /v1/members |
