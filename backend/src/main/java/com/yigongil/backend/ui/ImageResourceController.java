package com.yigongil.backend.ui;

import com.yigongil.backend.application.ImageResourceService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/v1/images")
public class ImageResourceController {

    private final ImageResourceService imageResourceService;

    public ImageResourceController(ImageResourceService imageResourceService) {
        this.imageResourceService = imageResourceService;
    }

    @PostMapping
    public ResponseEntity<Void> uploadImage(@RequestPart MultipartFile image) {
        return ResponseEntity.created(URI.create(imageResourceService.uploadImage(image)))
                             .build();
    }
}
