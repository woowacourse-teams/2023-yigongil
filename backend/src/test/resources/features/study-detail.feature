Feature: 스터디를 생성하고 조회한다.

  Scenario: 스터디를 정상 생성하고 조회한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바" 스터디 신청을 수락한다.
    When 스터디 상세 조회에서 이름이 "자바"인 스터디를 조회한다.
    Then 스터디 상세조회 결과가 제목-"자바", 정원-"5"로 조회된다.

  Scenario: 스터디를 정상 생성하고 조회한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바"스터디에 신청한다.
    When 스터디 상세 조회에서 이름이 "자바"인 스터디를 조회한다.
    Then 스터디 상세조회 결과가 제목-"자바", 정원-"5"로 조회된다.
