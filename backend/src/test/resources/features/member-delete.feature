Feature: 회원 탈퇴 관련 기능

  Scenario: 회원 탈퇴 권한을 검증한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 회원 탈퇴한다.
    When "jinwoo"가 마이페이지를 조회한다.
    Then 권한 예외가 발생한다.

  Scenario: 스터디 상세 조회에서 회원 탈퇴 상태를 확인할 수 있다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1d", 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    Given "noiman"가 회원 탈퇴한다.
    When "jinwoo"가 "자바1" 스터디를 조회한다.
    Then "noiman"가 회원 탈퇴 상태이다.

  Scenario: 모집중인 스터디에서 마스터가 탈퇴하면 스터디가 삭제된다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "mint"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1d", 소개-"스터디소개1"로 스터디를 개설한다.
    Given 깃허브 아이디가 "mint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "mint"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 회원 탈퇴한다.

    When 모집 중인 스터디 탭을 클릭한다.

    When "mint"가 "자바1" 스터디를 조회한다.

    Then 404 코드를 반환한다.
