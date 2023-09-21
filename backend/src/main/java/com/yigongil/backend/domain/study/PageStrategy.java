package com.yigongil.backend.domain.study;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public enum PageStrategy {

    ID_DESC(Constants.PAGE_SIZE, Sort.by("id").descending());

    private final int size;
    private final Sort sort;

    PageStrategy(int size, Sort sort) {
        this.size = size;
        this.sort = sort;
    }

    public static Pageable defaultPageStrategy(int page) {
        return PageRequest.of(page, ID_DESC.getSize(), ID_DESC.getSort());
    }

    public static class Constants {

        public static final int PAGE_SIZE = 30;
    }
}
