package com.yigongil.backend.domain.optionaltodo;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.exception.TodoNotDoneYetException;
import com.yigongil.backend.exception.TodoNotFoundException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;

@Entity
public class OptionalTodo extends BaseEntity {

    private static final int MAX_CONTENT_LENGTH = 20;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    @Column(nullable = false)
    private boolean isDone;

    protected OptionalTodo() {
    }

    @Builder
    public OptionalTodo(Long id, String content, boolean isDone) {
        this.id = id;
        this.content = content;
        this.isDone = isDone;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void complete() {
        if (isDone) {
            throw new TodoNotFoundException("해당 투두는 이미 완료되었습니다.", String.valueOf(id));
        }
        isDone = true;
    }

    public void reset() {
        if (isDone) {
            isDone = false;
        }
        throw new TodoNotDoneYetException("투두가 아직 완료되지 않았습니다.", String.valueOf(id));
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OptionalTodo that = (OptionalTodo) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
