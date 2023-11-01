package com.yigongil.backend.domain.image;

import java.net.URI;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Profile(value = {"prod", "dev"})
@RequestMapping("/images")
@RestController
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
