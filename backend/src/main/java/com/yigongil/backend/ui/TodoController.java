package com.yigongil.backend.ui;

import com.yigongil.backend.application.TodoService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import com.yigongil.backend.ui.doc.TodoApi;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/rounds/{roundId}")
@RestController
public class TodoController implements TodoApi {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todos")
    public ResponseEntity<Void> createNecessaryTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @RequestBody @Valid TodoCreateRequest request
    ) {
        todoService.createNecessaryTodo(member, roundId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/todos")
    public ResponseEntity<Void> updateNecessaryTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @RequestBody @Valid TodoUpdateRequest request
    ) {
        todoService.updateNecessaryTodo(member, roundId, request);
        return ResponseEntity.noContent().build();
    }
}
