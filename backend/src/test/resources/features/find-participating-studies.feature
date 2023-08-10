Feature: 내가 포함된 스터디를 조회한다.

  Scenario: 내가 개설자인 스터디를 조회한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.
    When "jinwoo"의 모든 스터디를 조회한다.
    Then 역할이 개설자인 스터디 1개 참여자인 스터디 0개 지원자인 스터디 0개가 표시된다.
    Then 모집 중인 스터디 1개 진행 중인 스터디 0개 종료된 스터디 0개가 표시된다.

  Scenario: 내가 참여자인 스터디를 조회한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    When "noiman"의 모든 스터디를 조회한다.
    Then 역할이 개설자인 스터디 0개 참여자인 스터디 1개 지원자인 스터디 0개가 표시된다.
    Then 모집 중인 스터디 1개 진행 중인 스터디 0개 종료된 스터디 0개가 표시된다.

