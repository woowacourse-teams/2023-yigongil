Feature: 피드 조회 기능

  Scenario: 일반 피드 조회 성공
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "jinwoo"가 닉네임 "jinwoo"과 간단 소개"소개"으로 수정한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.

    When "jinwoo"가 "자바1"스터디 피드에 "내용"의 글을 작성한다.
    Then "yujamint"가 "자바1"스터디 피드에서 "jinwoo"의 "내용" 글을 확인할 수 있다.

  Scenario: 인증을 올리면 머스트두가 체크된다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "jinwoo"가 닉네임 "jinwoo"과 간단 소개"소개"으로 수정한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given "yujamint"가 닉네임 "yujamint"과 간단 소개"소개"으로 수정한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디의 현재 주차를 통해 현재 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "<머스트두 내용>"로 머스트두를 추가한다.

    When "jinwoo"가 "자바1"스터디 피드에 "내용"의 인증 글을 작성한다.
    When "yujamint"가 "자바1"스터디 피드에 "내용"의 인증 글을 작성한다.

    When 이름이 "자바1"인 스터디의 찾은 회차 머스트두 진행률을 조회한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디의 이번 회차 인증 정보를 조회한다.
    Then "jinwoo"가 이번 회차를 완료했다.
    Then "yujamint"가 이번 회차를 완료했다.


  Scenario: 올라온 인증 개수를 검증한다.

    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given "jinwoo"가 닉네임 "jinwoo"과 간단 소개"소개"으로 수정한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디의 현재 주차를 통해 현재 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "<머스트두 내용>"로 머스트두를 추가한다.

    When "jinwoo"가 "자바1"스터디 피드에 "내용"의 인증 글을 작성한다.
    When "jinwoo"가 "자바1" 스터디의 인증 목록을 조회한다.

    Then 현재 회차의 인증이 1 개 올라왔다.

