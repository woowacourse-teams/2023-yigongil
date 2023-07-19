package com.yigongil.backend.domain.roundofmember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RoundOfMember extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean isDone;

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
