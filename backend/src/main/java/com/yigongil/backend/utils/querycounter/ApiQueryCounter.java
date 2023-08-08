package com.yigongil.backend.utils.querycounter;

import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Profile(value = {"local", "test"})
@Getter
@RequestScope
@Component
public class ApiQueryCounter {

    private int count;

    public void increaseCount() {
        count++;
    }
}
