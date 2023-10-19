Feature: 스터디를 탈퇴한다.

  Scenario: 스터디를 탈퇴한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"1"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.

    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.

    When "noiman" 이 "자바1" 스터디에서 탈퇴한다.
    When "jinwoo"가 "자바1" 스터디의 인증 목록을 조회한다.
    Then "noiman" 이 "자바1" 스터디에 참여하지 않는다.
