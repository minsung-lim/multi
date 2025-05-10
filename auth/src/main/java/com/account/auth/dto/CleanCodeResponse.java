package com.account.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Response for code cleaning operation")
public class CleanCodeResponse {
    @Schema(description = "Number of deleted codes")
    private final int deleted;
} 