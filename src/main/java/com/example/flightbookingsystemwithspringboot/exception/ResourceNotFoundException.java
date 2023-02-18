package com.example.flightbookingsystemwithspringboot.exception;

public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = -4822254354063859643L;

    private final String resource;
    private final String reasonCode;

    public ResourceNotFoundException(final String resource, final String reasonCode, final String reasonText) {
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
