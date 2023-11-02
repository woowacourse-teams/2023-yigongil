package com.yigongil.backend.config.auth.path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class PathInterceptor implements HandlerInterceptor {

    private final PathContainer pathContainer = new PathContainer();
    private final HandlerInterceptor handlerInterceptor;

    public PathInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (pathContainer.isNotInclude(uri, method)) {
            return true;
        }

        return handlerInterceptor.preHandle(request, response, handler);
    }

    public PathInterceptor includePath(String path, HttpMethod... methods) {
        pathContainer.includePath(path, methods);

        return this;
    }

    public PathInterceptor excludePath(String path, HttpMethod... methods) {
        pathContainer.excludePath(path, methods);

        return this;
    }
}
