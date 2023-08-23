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

  Scenario: 스터디를 정상 진행한다.
    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "noiman"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 회원 탈퇴한다.
    When "noiman"이 "jinwoo"의 프로필을 조회한다.
    Then 404 코드를 반환한다.
