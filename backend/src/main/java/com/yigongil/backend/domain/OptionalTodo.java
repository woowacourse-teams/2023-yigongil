package com.yigongil.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class OptionalTodo extends BaseEntity {

    private static final int MAX_CONTENT_LENGTH = 20;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    @Column(nullable = false)
    private boolean isDone;

    protected OptionalTodo() {
    }
}
