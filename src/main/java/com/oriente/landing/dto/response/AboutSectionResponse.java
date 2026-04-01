package com.oriente.landing.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutSectionResponse {

    private Long    id;
    private String  heading;
    private String  body;
    private String  imageUrl;
    private Boolean active;
    private String stat1Value;
    private String stat1Label;
    private String stat2Value;
    private String stat2Label;
    private String stat3Value;
    private String stat3Label;

    // ── Diferenciales editables ───────────────────────────
    private String diff1Title;
    private String diff1Desc;
    private String diff2Title;
    private String diff2Desc;
    private String diff3Title;
    private String diff3Desc;
    private String diff4Title;
    private String diff4Desc;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}