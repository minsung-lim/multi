package com.account.auth.controller;

import com.account.auth.dto.CodeResponse;
import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void getCode_ShouldReturnCode() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("testUser");
        loginRequest.setPassword("testPass");

        String code = "testCode123";
        when(authService.generateCode(any(LoginRequest.class))).thenReturn(code);

        // When & Then
        mockMvc.perform(post("/oauth/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"loginId\":\"testUser\",\"password\":\"testPass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code))
                .andDo(document("get-code",
                        requestHeaders(
                                headerWithName("Content-Type").description("The content type of the request")
                        ),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("User's login ID"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("User's password")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("Generated authorization code")
                        )
                ));
    }

    @Test
    void getToken_ShouldReturnToken() throws Exception {
        // Given
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setCode("testCode123");
        tokenRequest.setClient_id("testClient");

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("testToken123");
        tokenResponse.setToken_type("Bearer");
        tokenResponse.setExpires_in(3600);
        tokenResponse.setScope("read write");
        tokenResponse.setIss("https://auth.example.com");
        tokenResponse.setAud("https://api.example.com");
        tokenResponse.setSub("testUser");
        tokenResponse.setIat(1516239022L);
        tokenResponse.setExp(1516242622L);

        when(authService.generateToken(any(TokenRequest.class))).thenReturn(tokenResponse);

        // When & Then
        mockMvc.perform(post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"testCode123\",\"client_id\":\"testClient\",\"grant_type\":\"authorization_code\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("testToken123"))
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.expires_in").value(3600))
                .andExpect(jsonPath("$.scope").value("read write"))
                .andExpect(jsonPath("$.iss").value("https://auth.example.com"))
                .andExpect(jsonPath("$.aud").value("https://api.example.com"))
                .andExpect(jsonPath("$.sub").value("testUser"))
                .andExpect(jsonPath("$.iat").value(1516239022L))
                .andExpect(jsonPath("$.exp").value(1516242622L))
                .andDo(document("get-token",
                        requestHeaders(
                                headerWithName("Content-Type").description("The content type of the request")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("Authorization code"),
                                fieldWithPath("client_id").type(JsonFieldType.STRING).description("Client ID"),
                                fieldWithPath("grant_type").type(JsonFieldType.STRING).description("Grant type (authorization_code)")
                        ),
                        responseFields(
                                fieldWithPath("access_token").type(JsonFieldType.STRING).description("JWT access token"),
                                fieldWithPath("token_type").type(JsonFieldType.STRING).description("Token type (Bearer)"),
                                fieldWithPath("expires_in").type(JsonFieldType.NUMBER).description("Token expiration time in seconds"),
                                fieldWithPath("scope").type(JsonFieldType.STRING).description("Token scope"),
                                fieldWithPath("iss").type(JsonFieldType.STRING).description("JWT issuer"),
                                fieldWithPath("aud").type(JsonFieldType.STRING).description("JWT audience"),
                                fieldWithPath("sub").type(JsonFieldType.STRING).description("JWT subject"),
                                fieldWithPath("iat").type(JsonFieldType.NUMBER).description("JWT issued at timestamp"),
                                fieldWithPath("exp").type(JsonFieldType.NUMBER).description("JWT expiration timestamp")
                        )
                ));
    }

    @Test
    void revokeToken() throws Exception {
        doNothing().when(authService).revokeToken(any(String.class));

        mockMvc.perform(post("/oauth/revoke")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andDo(document("revoke-token",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token to revoke")
                        )
                ));
    }
} 