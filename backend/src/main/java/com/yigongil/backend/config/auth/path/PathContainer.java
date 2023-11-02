package com.yigongil.backend.config.auth.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class PathContainer {

    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<PathPattern> includedPaths = new ArrayList<>();
    private final List<PathPattern> excludedPaths = new ArrayList<>();

    public PathContainer excludePath(String path, HttpMethod... httpMethods) {
        excludedPaths.add(new PathPattern(path, Set.of(httpMethods)));

        return this;
    }

    public PathContainer includePath(String path, HttpMethod... httpMethods) {
        includedPaths.add(new PathPattern(path, Set.of(httpMethods)));

        return this;
    }

    public boolean isNotInclude(String path, String method) {
        boolean isExcludedPath = excludedPaths.stream()
                                              .anyMatch(pathPattern -> pathPattern.matches(pathMatcher, path, method));

        boolean isNotIncludePath = includedPaths.stream()
                                                .noneMatch(pathPattern -> pathPattern.matches(pathMatcher, path, method));

        return isExcludedPath || isNotIncludePath;
    }
}
