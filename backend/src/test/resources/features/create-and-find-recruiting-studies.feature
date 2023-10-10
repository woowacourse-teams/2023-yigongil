Feature: 스터디를 만들면 모집 중인 스터디로 생성된다.

  Scenario: 스터디를 정상 생성한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "jinwoo"가 제목-"자바2", 정원-"8"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개2"로 스터디를 개설한다.
    When 모집 중인 스터디 탭을 클릭한다.
    Then 모집 중인 스터디를 2개 중 2개를 확인할 수 있다.

  Scenario: 스터디를 검색한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "jinwoo"가 제목-"2자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개2"로 스터디를 개설한다.
    Given "jinwoo"가 제목-"자3바", 정원-"8"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개3"로 스터디를 개설한다.
    When "자바"를 검색한다.
    Then 결과가 모두 "자바"를 포함하고 2 개가 조회된다.

  Scenario: 스터디 검색 결과가 없다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "jinwoo"가 제목-"2자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개2"로 스터디를 개설한다.
    Given "jinwoo"가 제목-"자3바", 정원-"8"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개3"로 스터디를 개설한다.
    When "아무개"를 검색한다.
    Then 결과가 모두 "아무개"를 포함하고 0 개가 조회된다.
