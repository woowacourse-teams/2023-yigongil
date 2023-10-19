package com.yigongil.backend.exception;

import java.io.IOException;
import org.springframework.http.HttpStatus;

public class ImageToBytesException extends HttpException {

    public ImageToBytesException(IOException e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Failed to convert image to bytes");
    }
}
