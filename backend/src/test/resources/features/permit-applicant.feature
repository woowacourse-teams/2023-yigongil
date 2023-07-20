Feature: 스터디 참여 신청을 허가한다.

  Scenario Outline: 스터디 참여 신청을 정상적으로 허가한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 "자바1", "6", "2023.03.23", "2", "1w", "스터디소개1"로 스터디를 개설한다.

    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청할 수 있다.

    When "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.
    When "yujamint"가 스터디 상세 조회에서 이름이 "자바1"인 스터디를 조회한다.
    Then "yujamint"는 "자바1" 스터디의 스터디원으로 추가되어 있다.
