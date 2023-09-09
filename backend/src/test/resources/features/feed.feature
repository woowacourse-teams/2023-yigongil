Feature: 피드 조회 기능

  Scenario: 일반 피드 조회 성공
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"2"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디를 시작한다.

    When "jinwoo"가 "자바1"스터디 피드에 "내용"의 글을 작성한다.
    Then "yujamint"가 "자바1"스터디 피드에서 "jinwoo"의 "내용" 글을 확인할 수 있다.

  Scenario: 인증 피드 조회 성공
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"2"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디를 시작한다.

    When "jinwoo"가 "자바1"스터디 피드에 "내용"의 인증 글을 작성한다.
    Then "yujamint"가 "자바1"스터디 피드에서 "jinwoo"의 "내용"의 인증 글을 확인할 수 있다.
