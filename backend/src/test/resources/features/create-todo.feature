Feature: 투두를 생성한다.

  Scenario Outline: 투두를 정상 생성한다.

    Given "자바", "5", "2023.05.17", "2", "1w", "스터디소개"를 입력한다.
    Given post 요청을 "/v1/studies" 로 보낸다.

    Given "<필수 투두 여부>", "<라운드 아이디>", "<투두 내용>" 을 입력한다.
    When post 요청을 "<투두 생성 url>" 로 보낸다.
    Then 해당 라운드에 투두가 등록된다.

    Examples:
      | 필수 투두 여부 | 라운드 아이디 | 투두 내용      | 투두 생성 url           |
      | false    | 1       | 새로운 투두 | /v1/studies/1/todos |

