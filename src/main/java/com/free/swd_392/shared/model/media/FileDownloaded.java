package com.free.swd_392.shared.model.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDownloaded {

    // private byte[] file;
    InputStream inputStream;
    private String fileName;
    private MediaType contentType;

}
