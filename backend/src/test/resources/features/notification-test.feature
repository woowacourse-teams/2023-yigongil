Feature: 알림을 발송한다.

  Scenario: 스터디의 과정에서 알림을 발송한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 토큰을 메시지 서비스에 등록한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.

    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given "noiman"가 토큰을 메시지 서비스에 등록한다.

    Then "jinwoo"가 "noiman"의 "자바1" 스터디 신청 알림을 받는다.
