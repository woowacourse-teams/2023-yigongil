Feature: 스터디를 진행한다

  Scenario: 스터디를 정상 시작한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1w", 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 시작한다.
    When "jinwoo"가 홈화면을 조회한다.
    Then 스터디의 남은 날짜가 null이 아니다.

  Scenario: 스터디가 날짜 변경에 따라 진행된다
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"2"회, 주기-"1d", 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 시작한다.
    Given 1일이 지난다.
    When "jinwoo"가 "자바1" 스터디를 조회한다.
    Then 스터디의 현재 라운드가 2로 변경되어 있다.

  Scenario: 스터디가 날짜 변경에 따라 진행되고 종료된다
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 예상시작일-"1"일 뒤, 총 회차-"1"회, 주기-"15d", 소개-"스터디소개1"로 스터디를 개설한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given 깃허브 아이디가 "noiman"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "noiman"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 시작한다.

    Given "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "필수"로 필수 투두를 추가한다.
    When "jinwoo"가 찾은 회차의 필수 투두를 수정 내용 "true", "내용"으로 수정한다.
    When "noiman"가 찾은 회차의 필수 투두를 체크한다.

    Given 16일이 지난다.

    When "jinwoo"가 "자바1" 스터디를 조회한다.
    Then 스터디가 종료되어 있다.
    Then 멤버들이 스터디를 성공적으로 완료하여 티어가 2로 상승했다.
