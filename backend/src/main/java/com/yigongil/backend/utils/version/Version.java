package com.yigongil.backend.utils.version;

import com.yigongil.backend.exception.HttpException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;

public enum Version {

    V1_00_00_00("1000000", false, "최신 버전입니다."),
    ;

    private final String versionCode;
    private final boolean shouldUpdate;
    private final String message;

    Version(String versionCode, boolean shouldUpdate, String message) {
        this.versionCode = versionCode;
        this.shouldUpdate = shouldUpdate;
        this.message = message;
    }

    public static Version from(String versionCode) {
        return Arrays.stream(values())
                     .filter(version -> version.versionCode.equals(versionCode))
                     .findFirst()
                     .orElseThrow(() -> new VersionNotFoundException(versionCode));
    }

    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    public String getMessage() {
        return message;
    }

    private static class VersionNotFoundException extends HttpException {
        public VersionNotFoundException(String versionCode) {
            super(HttpStatus.BAD_REQUEST, "버전 코드에 해당하는 버전이 존재하지 않습니다.", versionCode);
        }
    }
}
