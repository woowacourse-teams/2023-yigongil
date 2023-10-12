Feature: 투두를 관리한다.

  Scenario Outline: 필수 투두를 정상 생성한다.

    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "<투두 내용>"로 필수 투두를 추가한다.

    When "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.

    Then 필수 투두가 "<투두 내용>"임을 확인할 수 있다.

    Examples:
      | 투두 내용  |  |
      | 새로운 투두 |  |

  Scenario Outline: 필수 투두를 수정한다.

    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "<투두 내용>"로 필수 투두를 추가한다.

    When "jinwoo"가 찾은 회차의 필수 투두를 수정 내용 "<수정 투두 내용>"으로 수정한다.
    When "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.

    Then 수정된 내용 "<수정 투두 내용>" 이 필수 투두에 반영된다.

    Examples:
      | 투두 내용  | 수정 투두 내용 |  |
      | 새로운 투두 | 수정됨      |  |

  Scenario Outline: 개설자가 아니면 필수 투두를 수정할 수 없다.

    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "yujamint"의 깃허브 아이디로 회원가입을 한다.
    Given "jinwoo"가 제목-"자바1", 정원-"6"명, 최소 주차-"7"주, 주당 진행 횟수-"3"회, 소개-"스터디소개1"로 스터디를 개설한다.
    Given 깃허브 아이디가 "yujamint"인 멤버가 이름이 "자바1"스터디에 신청한다.
    Given "jinwoo"가 "yujamint"의 "자바1" 스터디 신청을 수락한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디를 "MONDAY"에 진행되도록 하여 시작한다.
    Given "jinwoo"가 이름이 "자바1"인 스터디의 1 회차를 찾는다.
    Given "jinwoo"가 찾은 회차에 "<투두 내용>"로 필수 투두를 추가한다.

    When "yujamint"가 찾은 회차의 필수 투두를 수정 내용 "<수정 투두 내용>"으로 수정한다.

    Then 권한 예외가 발생한다.

    Examples:
      | 투두 내용  | 수정 투두 내용 |  |
      | 새로운 투두 | 수정됨      |  |
