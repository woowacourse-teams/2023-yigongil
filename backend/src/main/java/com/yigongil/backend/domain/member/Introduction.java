package com.yigongil.backend.domain.member;

import com.yigongil.backend.exception.InvalidIntroductionLengthException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class Introduction {

    private static final int MAXIMUM_INTRODUCTION_LENGTH = 200;

    @Column(nullable = false, length = MAXIMUM_INTRODUCTION_LENGTH)
    private String introduction;

    public Introduction(String introduction) {
        validateLength(introduction);
        this.introduction = introduction;
    }

    protected Introduction() {

    }

    private void validateLength(String introduction) {
        if (MAXIMUM_INTRODUCTION_LENGTH < introduction.length() || introduction.isBlank()) {
            throw new InvalidIntroductionLengthException("간단 소개는 1~200자 사이어야합니다.", introduction);
        }
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Introduction that = (Introduction) o;
        return Objects.equals(introduction, that.introduction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(introduction);
    }
}
