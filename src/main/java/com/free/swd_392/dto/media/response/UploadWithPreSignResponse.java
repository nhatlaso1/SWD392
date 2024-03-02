package com.free.swd_392.dto.media.response;

import com.free.swd_392.shared.model.media.PreSignUrl;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadWithPreSignResponse extends PreSignUrl {

    private String previewUrl;

    public UploadWithPreSignResponse(String url, String previewUrl) {
        super(url);
        this.previewUrl = previewUrl;
    }
}
