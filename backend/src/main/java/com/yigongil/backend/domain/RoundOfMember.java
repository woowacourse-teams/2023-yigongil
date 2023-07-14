package com.yigongil.backend.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class RoundOfMember extends BaseEntity {

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
}
