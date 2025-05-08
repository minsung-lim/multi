package com.auth.controller;

import com.auth.dto.LoginRequest;
import com.auth.dto.TokenResponse;
import com.auth.service.AuthService;
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

    @PostMapping("/token")
    @Operation(summary = "Get access token", description = "Obtain an access token by providing user credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token successfully generated",
            content = @Content(schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<TokenResponse> getToken(
            @Parameter(description = "User credentials", required = true)
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        log.info("Token request received - Headers: {}, Body: {}", request.getHeaderNames(), loginRequest);
        TokenResponse response = authService.authenticate(loginRequest);
        log.info("Token response - Headers: {}, Body: {}", response);
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