package com.account.auth.controller;

import com.account.auth.dto.CodeResponse;
import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and token management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/code")
    @Operation(summary = "Get OAuth code", description = "Obtain an OAuth code by providing user credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Code successfully generated",
            content = @Content(schema = @Schema(implementation = CodeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<CodeResponse> getCode(
            @Parameter(description = "User credentials", required = true)
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        log.info("Code request received - Headers: {}, Body: {}", request.getHeaderNames(), loginRequest);
        String code = authService.generateCode(loginRequest);
        log.info("Code generated successfully");
        return ResponseEntity.ok(new CodeResponse(code));
    }

    @PostMapping("/token")
    @Operation(summary = "Get access token", description = "Obtain an access token by providing an authorization code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token successfully generated",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid code or expired code")
    })
    public ResponseEntity<TokenResponse> getToken(
            @Parameter(description = "Token request", required = true)
            @RequestBody TokenRequest tokenRequest,
            HttpServletRequest request) {
        log.info("Token request received - Headers: {}, Body: {}", request.getHeaderNames(), tokenRequest);
        TokenResponse response = authService.generateToken(tokenRequest);
        log.info("Token generated successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke")
    @Operation(summary = "Revoke token", description = "Revoke an existing access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token successfully revoked"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    public ResponseEntity<Void> revokeToken(
            @Parameter(description = "Bearer token to revoke", required = true)
            @RequestHeader("Authorization") String token) {
        authService.revokeToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok().build();
    }
} 