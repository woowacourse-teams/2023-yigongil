package com.yigongil.backend.domain.roundofmember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.exception.NotTodoOwnerException;
import com.yigongil.backend.exception.TooManyOptionalTodosException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class RoundOfMember extends BaseEntity {

    private static final int MAXIMUM_TODO_SIZE = 4;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private boolean isDone;

    @Cascade(CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "round_of_member_id")
    private List<OptionalTodo> optionalTodos = new ArrayList<>();

    protected RoundOfMember() {
    }

    @Builder
    public RoundOfMember(Long id, Member member, boolean isDone, List<OptionalTodo> optionalTodos) {
        this.id = id;
        this.member = member;
        this.isDone = isDone;
        this.optionalTodos = optionalTodos == null ? new ArrayList<>() : optionalTodos;
    }

    public OptionalTodo createOptionalTodo(String content) {
        if (optionalTodos.size() >= MAXIMUM_TODO_SIZE) {
            throw new TooManyOptionalTodosException("선택 투두는 한 번에 4개까지 설정 가능합니다.", content);
        }
        OptionalTodo optionalTodo = OptionalTodo.builder()
                                                .content(content)
                                                .build();
        optionalTodos.add(optionalTodo);
        return optionalTodo;
    }

    public void completeRound() {
        updateNecessaryTodoIsDone(true);
    }

    public void updateNecessaryTodoIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void updateOptionalTodoIsDone(Long todoId, boolean isDone) {
        OptionalTodo todo = findOptionalTodoById(todoId);
        todo.updateIsDone(isDone);
    }

    public void updateOptionalTodoContent(Long todoId, String content) {
        OptionalTodo todo = findOptionalTodoById(todoId);
        todo.updateContent(content);
    }

    public void removeOptionalTodoById(Long todoId) {
        OptionalTodo todo = findOptionalTodoById(todoId);
        optionalTodos.remove(todo);
    }

    private OptionalTodo findOptionalTodoById(Long todoId) {
        return optionalTodos.stream()
                            .filter(optionalTodo -> optionalTodo.isSameId(todoId))
                            .findFirst()
                            .orElseThrow(() -> new NotTodoOwnerException("투두 작성자가 아닙니다.", String.valueOf(member.getId())));
    }

    public boolean isMemberEquals(Member member) {
        return this.member.equals(member);
    }

    public void updateMemberTier() {
        member.upgradeTier();
    }
}
