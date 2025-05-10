package com.account.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "OAuth code response")
public class CodeResponse {
    @Schema(description = "Generated OAuth code", example = "abc123xyz")
    private String code;
} 