Feature: 스터디를 진행 성능 테스트
  Scenario Outline: 스터디 진행 성능 테스트
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바숫자", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 <count>개 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바숫자"스터디 <count>개에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바숫자" 스터디 <count>개 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바숫자"인 스터디 <count>개를 "WEDNESDAY"에 진행되도록 하여 시작한다.
    Given 2일이 지난다.

    Examples:
      | count |
      |  100  |
