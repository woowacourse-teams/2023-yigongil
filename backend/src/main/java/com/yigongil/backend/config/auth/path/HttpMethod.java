package com.yigongil.backend.config.auth.path;

public enum HttpMethod {

    PUT,
    POST,
    GET,
    PATCH,
    DELETE,
    ANY {
        @Override
        public boolean matches(String method) {
            return true;
        }
    };

    public boolean matches(String method) {
        return name().equalsIgnoreCase(method);
    }
}
