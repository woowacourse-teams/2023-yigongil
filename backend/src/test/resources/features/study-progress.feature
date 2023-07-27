Feature: 스터디를 진행한다

  Scenario: 스터디를 정상 시작한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 "자바1", "6", "2023.03.23", "2", "1w", "스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청할 수 있다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 시작한다.
    When "jinwoo"가 홈화면을 조회한다.
    Then 스터디의 남은 날짜가 null이 아니다.
