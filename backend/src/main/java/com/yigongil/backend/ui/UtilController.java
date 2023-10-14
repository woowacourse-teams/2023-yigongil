package com.yigongil.backend.ui;

import com.yigongil.backend.response.VersionResponse;
import com.yigongil.backend.utils.version.Version;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilController {

    @GetMapping("/check-version")
    public ResponseEntity<VersionResponse> checkVersion(@RequestParam String v) {
        Version version = Version.from(v);
        VersionResponse response = VersionResponse.from(version);
        return ResponseEntity.ok(response);
    }
}
