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
    Given "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "<투두 내용>"로 선택 투두를 추가한다.
    Given "jinwoo"가 회원 탈퇴한다.

    When "mint"가 "자바1" 스터디를 조회한다.

    Then 404 코드를 반환한다.

  Scenario: 탈퇴한 마스터의 스터디가 정상적으로 삭제되는 것을 홈 화면에서 확인한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "mint"의 깃허브 아이디로 회원가입을 한다.
    Given "emil"의 깃허브 아이디로 회원가입을 한다.

    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1d", 소개-"스터디소개1"로 스터디를 개설한다.
    Given "mint"가 제목-"자바2", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1d", 소개-"스터디소개1"로 스터디를 개설한다.
    Given 깃허브 아이디가 "mint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given 깃허브 아이디가 "emil"인 멤버가 이름이 "자바2"스터디에 신청한다.
    Given "jinwoo"가 "mint"의 "자바1" 스터디 신청을 수락한다.
    Given "mint"가 "emil"의 "자바2" 스터디 신청을 수락한다.
    Given "mint"가 이름이 "자바2"인 스터디를 시작한다.

    Given "jinwoo"가 회원 탈퇴한다.

    When "mint"가 홈화면을 조회한다.

    Then 스터디가 1 개 조회된다.

  Scenario: 탈퇴한 회원의 프로필을 조회한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 회원 탈퇴한다.
    When "noiman"이 "jinwoo"의 프로필을 조회한다.
    Then 404 코드를 반환한다.
