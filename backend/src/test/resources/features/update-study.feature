Feature: 스터디의 정보를 수정한다.

  Scenario: 스터디 정보를 정상적으로 수정한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바", 정원-"6"명, 예상시작일-"5"일 뒤, 총 회차-"5"회, 주기-"1w", 소개-"수정 전 스터디 소개"로 스터디를 개설한다.
    Given "jinwoo"가 "자바" 스터디의 정보를 제목-"자바스크립트", 정원-"8"명, 예상시작일-"3"일 뒤, 총 회차-"6"회, 주기-"5d", 소개-"수정 후 스터디 소개"로 수정한다.
    When "jinwoo"가 스터디 상세 조회에서 이름이 "자바스크립트"인 스터디를 조회한다.
    Then 스터디 상세조회 결과가 제목-"자바스크립트", 정원-"8"로 조회된다.
