package vn.fit.hcmus.truyenfull_restapi.constant;

import org.springframework.http.HttpStatus;


public enum StatusCode {
    NONE(0),
    SUCCESS(HttpStatus.OK.value()),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value()),
    NOT_FOUND(HttpStatus.NOT_FOUND.value()),
    PARAMETER_INVALID(1000);

    private  int value;

    StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}