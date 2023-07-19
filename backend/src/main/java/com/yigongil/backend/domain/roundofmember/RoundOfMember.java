package com.yigongil.backend.domain.roundofmember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    private Boolean isDone;

    @Cascade(CascadeType.PERSIST)
    @OneToMany
    @JoinColumn(name = "round_of_member_id")
    private List<OptionalTodo> optionalTodos = new ArrayList<>();

    protected RoundOfMember() {
    }

    @Builder
    public RoundOfMember(Long id, Member member, Boolean isDone, List<OptionalTodo> optionalTodos) {
        this.id = id;
        this.member = member;
        this.isDone = isDone;
        this.optionalTodos = optionalTodos;
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
    
    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Boolean getDone() {
        return isDone;
    }

    public List<OptionalTodo> getOptionalTodos() {
        return optionalTodos;
    }
}
