package com.free.swd_392.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum FileType {

    AVATAR("avatars"),
    DOCUMENT("documents"),
    ID_CARD("id-cards"),
    LIVENESS("livenesses"),
    ICON("icons"),
    COLLOCATION("collocations"),
    PROPOSAL("proposals"),
    STATEMENT("statements"),
    SIGN("signs"),
    OTHER("others");

    private static final Map<String, FileType> ENUM_MAP;

    static {
        ENUM_MAP = new ConcurrentHashMap<>();
        for (FileType fileType : FileType.values()) {
            ENUM_MAP.put(fileType.name(), fileType);
            ENUM_MAP.put(fileType.name().toLowerCase(), fileType);
            ENUM_MAP.put(fileType.getPath(), fileType);
        }
    }

    private final String path;

    FileType(String path) {
        this.path = path;
    }

    public static FileType valueFrom(String name) {
        return ENUM_MAP.get(name);
    }

    public String getPath() {
        return this.path;
    }
}
