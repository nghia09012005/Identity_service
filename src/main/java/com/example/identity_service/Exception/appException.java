package com.example.identity_service.Exception;

public class appException extends RuntimeException{
    public appException(error er) {
        super(er.getMessage());
        this.er = er;
    }

    error er;

    public error getEr() {
        return er;
    }

    public void setEr(error er) {
        this.er = er;
    }
}
