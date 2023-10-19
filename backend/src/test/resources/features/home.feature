Feature: 홈뷰를 조회한다.
  Scenario: 홈뷰를 조회한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"1"주, 주당 진행 횟수-"1"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 오늘과 같은 요일에 진행되도록 하여 시작한다.
    When "jinwoo"가 홈화면을 조회한다.
    Then "자바1", null, 7, 0, true로 스터디가 반환된다.
