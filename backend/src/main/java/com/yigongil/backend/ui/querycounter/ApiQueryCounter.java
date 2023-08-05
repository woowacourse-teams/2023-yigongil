package com.yigongil.backend.ui.querycounter;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@RequestScope
@Component
public class ApiQueryCounter {

    private int count;

    public void increaseCount() {
        count++;
    }
}
