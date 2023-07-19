package com.yigongil.backend.domain.study;

import org.springframework.data.domain.Sort;

public enum PageStrategy {

    CREATED_AT_DESC(Constants.PAGE_SIZE, Sort.by("createdAt").descending());

    private final int size;
    private final Sort sort;

    PageStrategy(int size, Sort sort) {
        this.size = size;
        this.sort = sort;
    }

    public int getSize() {
        return size;
    }

    public Sort getSort() {
        return sort;
    }

    public static class Constants {
        public static final int PAGE_SIZE = 1;
    }
}
