Feature: 유저의 스터디에서의 역할을 조회한다

  Background: 유저의 스터디에서의 역할
    Given 역할에 따른 코드 표
      | 스터디장 | 0 |
      | 스터디원 | 1 |
      | 스터디 지원자 | 2 |

  Scenario: 스터디원의 역할 조회
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.

    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.

    When "noiman"의 "자바1" 스터디에서의 역할을 조회한다.

    Then 역할이 "스터디원"이다.

  Scenario: 스터디장 역할 조회
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.

    When "jinwoo"의 "자바1" 스터디에서의 역할을 조회한다.

    Then 역할이 "스터디장"이다.

  Scenario: 스터디 신청자의 역할 조회
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.

    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.

    When "noiman"의 "자바1" 스터디에서의 역할을 조회한다.

    Then 역할이 "스터디 지원자"이다.

