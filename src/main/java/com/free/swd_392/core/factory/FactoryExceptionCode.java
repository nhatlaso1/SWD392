package com.free.swd_392.core.factory;

public interface FactoryExceptionCode {

    default String notFound() {
        return "Not found";
    }

    default String conflict() {
        return "Conflict";
    }

    default String badRequest() {
        return "Bad request";
    }

    default String internalServerError() {
        return "Internal server error";
    }
}
