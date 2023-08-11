package com.yigongil.backend.ui;

import com.yigongil.backend.application.TodoService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/rounds/{roundId}")
@RestController
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/todos/necessary")
    public ResponseEntity<Void> createNecessaryTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @RequestBody @Valid TodoCreateRequest request
    ) {
        todoService.createNecessaryTodo(member, roundId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todos/optional")
    public ResponseEntity<Void> createOptionalTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @RequestBody @Valid TodoCreateRequest request
    ) {
        Long todoId = todoService.createOptionalTodo(member, roundId, request);
        return ResponseEntity.created(URI.create("/v1/rounds/" + roundId + "/todos/optional/" + todoId)).build();
    }

    @PatchMapping("/todos/necessary")
    public ResponseEntity<Void> updateNecessaryTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @RequestBody @Valid TodoUpdateRequest request
    ) {
        todoService.updateNecessaryTodo(member, roundId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/todos/optional/{todoId}")
    public ResponseEntity<Void> updateOptionalTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @PathVariable Long todoId,
            @RequestBody @Valid TodoUpdateRequest request
    ) {
        todoService.updateOptionalTodo(member, roundId, todoId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todos/optional/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @Authorization Member member,
            @PathVariable Long roundId,
            @PathVariable Long todoId
    ) {
        todoService.deleteOptionalTodo(member, roundId, todoId);
        return ResponseEntity.noContent().build();
    }
}
