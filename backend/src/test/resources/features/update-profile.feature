Feature: 프로필 정보를 업데이트한다

  Scenario Outline: 프로필 정보를 정상적으로 업데이트한다
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    When "jinwoo"가 닉네임 "<nickname>"과 간단 소개"<introduction>"으로 수정한다.
    Then 변경된 정보 닉네임 "<nickname>"과 간단 소개"<introduction>"를 확인할 수 있다.

    Examples:
      | nickname | introduction |
      | 김김진진우우   | 간단 소개입니다     |
