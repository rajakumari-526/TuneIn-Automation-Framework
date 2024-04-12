package com.tunein.mobile.api.retrofit.exception;

import okhttp3.Response;

import java.io.IOException;

public class RequestException extends RuntimeException {

    private String message;

    public RequestException(String message) {
        super(message);
        this.message = message;
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Response response) {
        try {
            this.message = String.format(
                    "Request error: %s \nDetails: %s", response.message(), response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
