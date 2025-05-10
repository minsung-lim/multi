package com.account.auth.controller;

import com.account.auth.config.TestConfig;
import com.account.auth.dto.CodeResponse;
import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

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
        tokenResponse.setExpires_in(3600);
        tokenResponse.setRefresh_token(null);

        when(authService.generateToken(any(TokenRequest.class))).thenReturn(tokenResponse);

        // When & Then
        mockMvc.perform(post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"testCode123\",\"client_id\":\"testClient\",\"grant_type\":\"authorization_code\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("testToken123"))
                .andExpect(jsonPath("$.expires_in").value(3600))
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
                                fieldWithPath("refresh_token").optional().type(JsonFieldType.STRING).description("Refresh token (nullable)"),
                                fieldWithPath("expires_in").type(JsonFieldType.NUMBER).description("Token expiration time in seconds")
                        )
                ));
    }

    @Test
    void revokeToken() throws Exception {
        doNothing().when(authService).revokeToken(anyString(), anyString());

        mockMvc.perform(post("/oauth/revoke?loginId=testUser&clientId=testClient"))
                .andExpect(status().isOk())
                .andDo(document("revoke-token",
                        queryParameters(
                                parameterWithName("loginId").description("Login ID of the user"),
                                parameterWithName("clientId").optional().description("Client ID (optional)")
                        )
                ));
    }
} 