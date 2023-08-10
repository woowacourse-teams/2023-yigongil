Feature: 프로필 정보를 업데이트한다

  Scenario Outline: 프로필 정보를 정상적으로 업데이트한다
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    When "jinwoo"가 닉네임 "<nickname>"과 간단 소개"<introduction>"으로 수정한다.
    Then "jinwoo"가 변경된 정보 닉네임 "<nickname>"과 간단 소개"<introduction>"를 확인할 수 있다.
    Then "jinwoo"의 온보딩 상태가 완료로 변경된다.

    Examples:
      | nickname | introduction |
      | 김김진진우우   | 간단 소개입니다     |

  Scenario Outline: 프로필 정보를 업데이트할 때 닉네임이 중복되면 실패한다
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    When "jinwoo"가 닉네임 "<nickname>"과 간단 소개"<introduction>"으로 수정한다.
    Then "<nickname>"은 중복된 닉네임인 것을 확인할 수 있다.

    Examples:
      | nickname | introduction |
      | 김김진진우우   | 간단 소개입니다     |

  Scenario: 회원 탈퇴를 검증한다.
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
    Given "jinwoo"가 회원 탈퇴한다.
    When "noiman"가 "자바1" 스터디를 조회한다.
    Then "jinwoo"가 회원 탈퇴 상태이다.

  Scenario: 스터디를 정상 진행한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 회원 탈퇴한다.
    When "noiman"이 "jinwoo"의 프로필을 조회한다.
    Then 404 코드를 반환한다.
