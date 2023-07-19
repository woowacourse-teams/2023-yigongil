package com.yigongil.backend.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.optionaltodo.OptionalTodoRepository;
import com.yigongil.backend.request.TodoCreateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.Optional;
import org.springframework.http.MediaType;

public class TodoSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;
    private final OptionalTodoRepository optionalTodoRepository;

    public TodoSteps(ObjectMapper objectMapper, SharedContext sharedContext,
                     OptionalTodoRepository optionalTodoRepository) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
        this.optionalTodoRepository = optionalTodoRepository;
    }

    @Given("{string}, {string}, {string} 을 입력한다.")
    public void 투두_정보를_입력한다(String isNecessary, String roundId, String content) throws JsonProcessingException {
        TodoCreateRequest request = new TodoCreateRequest(
                Boolean.parseBoolean(isNecessary),
                Long.parseLong(roundId), content
        );

        final RequestSpecification specification = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request));

        sharedContext.setRequestSpecification(specification);
    }

    @Then("해당 라운드에 투두가 등록된다.")
    public void 해당_라운드에_투두가_등록된다() {
        Optional<OptionalTodo> optionalTodo = optionalTodoRepository.findById(sharedContext.getResultId());
        assertThat(optionalTodo).isNotEmpty();
        // TODO: 2023/07/18 투두 조회 api 생성 후 수정
    }
}
