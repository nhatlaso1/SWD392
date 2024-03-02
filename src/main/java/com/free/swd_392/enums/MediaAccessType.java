package com.free.swd_392.enums;

public enum MediaAccessType {
    PUBLIC, PRIVATE;

    public String getNameLowerCase() {
        return this.name().toLowerCase();
    }
}
