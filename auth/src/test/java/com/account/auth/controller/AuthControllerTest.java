package com.account.auth.controller;

import com.account.auth.config.TestConfig;
import com.account.auth.dto.CodeResponse;
import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(TestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void getCode() throws Exception {
        // given
        LoginRequest request = new LoginRequest("user@example.com", "password123");
        when(authService.generateCode(any())).thenReturn("abc123def456");

        // when & then
        mockMvc.perform(post("/oauth/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("get-oauth-code",
                        requestHeaders(
                                headerWithName("Content-Type").description("The content type of the request")
                        ),
                        requestFields(
                                fieldWithPath("loginId").description("User's login ID (email)"),
                                fieldWithPath("password").description("User's password")
                        ),
                        responseFields(
                                fieldWithPath("code").description("Generated OAuth code")
                        )
                ));
    }

    @Test
    void getToken() throws Exception {
        // given
        TokenRequest request = new TokenRequest("abc123def456", "client123", "authorization_code");
        when(authService.generateToken(any())).thenReturn(new TokenResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                "abc123def456",
                1800L
        ));

        // when & then
        mockMvc.perform(post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("get-token",
                        requestFields(
                                fieldWithPath("code").description("Authorization code"),
                                fieldWithPath("clientId").description("Client ID"),
                                fieldWithPath("grantType").description("Grant type (must be \"authorization_code\")")
                        ),
                        responseFields(
                                fieldWithPath("access_token").description("JWT access token"),
                                fieldWithPath("refresh_token").description("Refresh token"),
                                fieldWithPath("expires_in").description("Token expiration time in seconds")
                        )
                ));
    }

    @Test
    void revokeToken() throws Exception {
        // given
        doNothing().when(authService).revokeToken(anyString(), anyString());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth/revoke")
                .queryParam("loginId", "user@example.com")
                .queryParam("clientId", "client123"))
                .andExpect(status().isOk())
                .andDo(document("revoke-token",
                        queryParameters(
                                parameterWithName("loginId").description("Login ID of the user"),
                                parameterWithName("clientId").optional().description("Client ID (optional)")
                        )
                ));
    }

    @Test
    void cleanExpiredCodes() throws Exception {
        // given
        when(authService.cleanExpiredCodes()).thenReturn(100);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/oauth/cleanCode"))
                .andExpect(status().isOk())
                .andDo(document("clean-expired-codes",
                        responseFields(
                                fieldWithPath("deleted").description("Number of deleted codes")
                        )
                ));
    }
} 