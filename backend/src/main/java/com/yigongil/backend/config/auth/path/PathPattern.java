package com.yigongil.backend.config.auth.path;

import java.util.Set;
import org.springframework.util.PathMatcher;

public class PathPattern {

    private final String path;
    private final Set<HttpMethod> httpMethods;

    public PathPattern(String path, Set<HttpMethod> httpMethods) {
        this.path = path;
        this.httpMethods = httpMethods;
    }

    public boolean matches(PathMatcher pathMatcher, String targetPath, String pathMethod) {
        return pathMatcher.match(path, targetPath) && matchesMethod(pathMethod);
    }

    private boolean matchesMethod(String pathMethod) {
        return httpMethods.stream()
                          .anyMatch(httpMethod -> httpMethod.matches(pathMethod));
    }
}
