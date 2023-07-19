package com.yigongil.backend.domain.member;

import com.yigongil.backend.exception.InvalidNicknameLengthException;
import com.yigongil.backend.exception.InvalidNicknamePatternException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Nickname {

    private static final int MINIMUM_NICKNAME_LENGTH = 2;
    private static final int MAXIMUM_NICKNAME_LENGTH = 8;
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9_]+$");

    @Column(nullable = false, unique = true, length = MAXIMUM_NICKNAME_LENGTH)
    private String nickname;

    public Nickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    public Nickname() {

    }

    private void validate(String nickname) {
        validateLength(nickname);
        validatePattern(nickname);
    }

    private void validateLength(String nickname) {
        if (MAXIMUM_NICKNAME_LENGTH < nickname.length() || nickname.length() < MINIMUM_NICKNAME_LENGTH) {
            throw new InvalidNicknameLengthException("닉네임 길이는 2~8자 사이어야합니다.", nickname);
        }
    }

    private void validatePattern(String nickname) {
        if (NICKNAME_PATTERN.matcher(nickname).matches()) {
            return;
        }
        throw new InvalidNicknamePatternException("닉네임은 영어, 숫자, 한글, 언더바만 포함해야합니다.", nickname);
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Nickname nickname1 = (Nickname) o;
        return Objects.equals(nickname, nickname1.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
