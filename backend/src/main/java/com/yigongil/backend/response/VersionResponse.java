package com.yigongil.backend.response;

import com.yigongil.backend.utils.version.Version;

public record VersionResponse(
        boolean shouldUpdate,
        String message
) {

    public static VersionResponse from(Version version) {
        return new VersionResponse(version.shouldUpdate(), version.getMessage());
    }
}
