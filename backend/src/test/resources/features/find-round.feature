Feature: 스터디를 생성한다.

  Scenario Outline: 스터디를 정상 생성한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 "자바1", "6", "2023.03.23", "2", "1w", "스터디소개1"로 스터디를 개설한다.
    When "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.
    Then 스터디 장이 "jinwoo"이고 해당 회차 인것을 확인할 수 있다.
