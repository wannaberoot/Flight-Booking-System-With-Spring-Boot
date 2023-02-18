package com.example.flightbookingsystemwithspringboot.exception;

public class ClientException extends Exception {

    private static final long serialVersionUID = -8277709196836634389L;

    private final String resource;
    private final String reasonCode;

    public ClientException(final String resource, final String reasonCode, final String reasonText) {
        super(reasonText);
        this.resource = resource;
        this.reasonCode = reasonCode;
    }

    public String getResource() {
        return resource;
    }

    public String getReasonCode() {
        return reasonCode;
    }
}
