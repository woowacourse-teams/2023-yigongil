package com.yigongil.backend.application;

import com.yigongil.backend.exception.ImageToBytesException;
import com.yigongil.backend.exception.InvalidImageExtensionException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Profile(value = {"prod", "dev"})
@Service
public class ImageResourceService {

    private final String bucketName;
    private final String cloudFrontDomainName;
    private final S3Client s3Client;
    private final String directoryName;

    public ImageResourceService(
            @Value("${aws.region}") String region,
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.cloud-front-domain}") String cloudFrontDomainName,
            @Value("${aws.s3.directory-name}") String directoryName
    ) {
        this.bucketName = bucketName;
        this.cloudFrontDomainName = cloudFrontDomainName;
        this.s3Client = S3Client.builder()
                                .region(Region.of(region))
                                .build();
        this.directoryName = directoryName;
    }

    public String uploadImage(MultipartFile image) {
        String key = createKey(ImageExtension.from(image.getOriginalFilename()));
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(key)
                                    .build(),
                    RequestBody.fromBytes(image.getBytes())
            );
            return cloudFrontDomainName + "/" + key;
        } catch (IOException e) {
            throw new ImageToBytesException(e);
        }
    }

    private String createKey(ImageExtension extension) {
        return directoryName + "/" + UUID.randomUUID() + extension.toString();
    }

    enum ImageExtension {

        JPG(".jpg"),
        JPEG(".jpeg"),
        PNG(".png");

        private final String extension;

        ImageExtension(String extension) {
            this.extension = extension;
        }

        public static ImageExtension from(String fileName) {
            return Arrays.stream(values())
                         .filter(value -> fileName.endsWith(value.extension))
                         .findAny()
                         .orElseThrow(() -> new InvalidImageExtensionException("Invalid image extension", fileName));
        }

        @Override
        public String toString() {
            return extension;
        }
    }
}
