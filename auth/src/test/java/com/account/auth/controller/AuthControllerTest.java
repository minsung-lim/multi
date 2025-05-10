package com.account.auth.controller;

import com.account.auth.dto.LoginRequest;
import com.account.auth.dto.TokenResponse;
import com.account.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
@Import({})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void getToken() throws Exception {
        // Mock auth service response
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token("test-token");
        tokenResponse.setToken_type("Bearer");
        tokenResponse.setExpires_in(3600);
        tokenResponse.setScope("read write");

        when(authService.authenticate(any(LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/oauth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"loginId\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("test-token"))
                .andDo(document("get-token",
                        requestHeaders(
                                headerWithName("Content-Type").description("The content type of the request")
                        ),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("User's login ID"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("User's password")
                        ),
                        responseHeaders(
                                headerWithName("Content-Type").description("The content type of the response")
                        ),
                        responseFields(
                                fieldWithPath("access_token").type(JsonFieldType.STRING).description("JWT access token"),
                                fieldWithPath("token_type").type(JsonFieldType.STRING).description("Token type (Bearer)"),
                                fieldWithPath("expires_in").type(JsonFieldType.NUMBER).description("Token expiration time in seconds"),
                                fieldWithPath("scope").type(JsonFieldType.STRING).description("Token scope")
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