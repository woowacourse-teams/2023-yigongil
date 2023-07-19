Feature: 투두를 관리한다.

  Scenario Outline: 투두를 정상 생성한다.

    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "자바", "5", "2023.05.17", "2", "1w", "스터디소개"를 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    Given post 요청을 "/v1/studies" 로 보낸다.

    Given "<필수 투두 여부>", "<라운드 아이디>", "<투두 내용>" 을 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    When post 요청을 "<투두 생성 url>" 로 보낸다.
    Then 해당 라운드에 투두가 등록된다.

    Examples:
      | 필수 투두 여부 | 라운드 아이디 | 투두 내용  | 투두 생성 url           |
      | false    | 1       | 새로운 투두 | /v1/studies/1/todos |

  Scenario Outline: 투두를 수정한다.

    Given "jinwoo"의 깃허브 아이디로 회원가입을 한다.
    Given "자바", "5", "2023.05.17", "2", "1w", "스터디소개"를 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    Given post 요청을 "/v1/studies" 로 보낸다.

    Given "<필수 투두 여부>", "<라운드 아이디>", "<투두 내용>" 을 입력한다.
    Given 토큰을 인증 헤더에 추가한다.
    Given post 요청을 생성된 id를 참고해 "<투두 생성 url>" 로 보낸다.
    Given 해당 라운드에 투두가 등록된다.
    Given "<필수 투두 여부>" 투두의 수정 내용 "<수정 투두 필수 여부>", "<수정 투두 내용>" 을 입력한다.
    Given 토큰을 인증 헤더에 추가한다.

    When patch 요청을 생성된 id를 참고해 "<투두 수정 url>" 로 보낸다.

    Then 수정된 내용 "<수정 투두 필수 여부>", "<수정 투두 내용>" 이 투두에 반영된다.

    Examples:
      | 필수 투두 여부 | 라운드 아이디 | 투두 내용  | 투두 생성 url           | 투두 수정 url           | 수정 투두 필수 여부 | 수정 투두 내용 |
      | false    | 1       | 새로운 투두 | /v1/studies/?/todos | /v1/studies/?/todos/ | true        | 수정됨      |
