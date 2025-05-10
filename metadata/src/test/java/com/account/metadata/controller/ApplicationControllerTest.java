package com.account.metadata.controller;

import com.account.metadata.dto.ApplicationRequest;
import com.account.metadata.dto.ApplicationResponse;
import com.account.metadata.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicationService applicationService;

    @Test
    public void createApplication() throws Exception {
        ApplicationRequest request = new ApplicationRequest();
        request.setAppName("Test Application");
        request.setScopes(new HashSet<>(Arrays.asList("read", "write")));
        request.setSecretKey("secret123");
        request.setCipherKey("cipher123");
        request.setRedirectUri("http://localhost:8080/callback");
        request.setGrantTypes(new HashSet<>(Arrays.asList("authorization_code", "client_credentials")));

        ApplicationResponse response = new ApplicationResponse();
        response.setAppId("test-app");
        response.setAppName(request.getAppName());
        response.setScopes(request.getScopes());
        response.setSecretKey(request.getSecretKey());
        response.setCipherKey(request.getCipherKey());
        response.setRedirectUri(request.getRedirectUri());
        response.setGrantTypes(request.getGrantTypes());
        response.setCreatedDate(LocalDateTime.now());
        response.setModifiedDate(LocalDateTime.now());

        when(applicationService.createApplication(any(ApplicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/metadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentation.document("create-application",
                        requestFields(
                                fieldWithPath("appName").description("Application name")
                                    .attributes(Attributes.key("constraints").value("Required")),
                                fieldWithPath("scopes").description("Application scopes")
                                    .attributes(Attributes.key("constraints").value("At least one scope required")),
                                fieldWithPath("secretKey").description("Secret key for the application")
                                    .attributes(Attributes.key("constraints").value("Required")),
                                fieldWithPath("cipherKey").description("Cipher key for the application")
                                    .attributes(Attributes.key("constraints").value("Required")),
                                fieldWithPath("redirectUri").description("Redirect URI for OAuth2"),
                                fieldWithPath("grantTypes").description("OAuth2 grant types")
                                    .attributes(Attributes.key("constraints").value("At least one grant type required"))
                        ),
                        responseFields(
                                fieldWithPath("appId").description("Application ID"),
                                fieldWithPath("appName").description("Application name"),
                                fieldWithPath("scopes").description("Application scopes"),
                                fieldWithPath("secretKey").description("Secret key for the application"),
                                fieldWithPath("cipherKey").description("Cipher key for the application"),
                                fieldWithPath("redirectUri").description("Redirect URI for OAuth2"),
                                fieldWithPath("grantTypes").description("OAuth2 grant types"),
                                fieldWithPath("createdDate").description("Creation date"),
                                fieldWithPath("modifiedDate").description("Last modification date")
                        )
                ));
    }

    @Test
    public void getApplication() throws Exception {
        String appId = "test-app";
        ApplicationResponse response = new ApplicationResponse();
        response.setAppId(appId);
        response.setAppName("Test Application");
        response.setScopes(new HashSet<>(Arrays.asList("read", "write")));
        response.setSecretKey("secret123");
        response.setCipherKey("cipher123");
        response.setRedirectUri("http://localhost:8080/callback");
        response.setGrantTypes(new HashSet<>(Arrays.asList("authorization_code", "client_credentials")));
        response.setCreatedDate(LocalDateTime.now());
        response.setModifiedDate(LocalDateTime.now());

        when(applicationService.getApplication(appId)).thenReturn(response);

        mockMvc.perform(get("/metadata/{appId}", appId))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("get-application",
                        responseFields(
                                fieldWithPath("appId").description("Application ID"),
                                fieldWithPath("appName").description("Application name"),
                                fieldWithPath("scopes").description("Application scopes"),
                                fieldWithPath("secretKey").description("Secret key for the application"),
                                fieldWithPath("cipherKey").description("Cipher key for the application"),
                                fieldWithPath("redirectUri").description("Redirect URI for OAuth2"),
                                fieldWithPath("grantTypes").description("OAuth2 grant types"),
                                fieldWithPath("createdDate").description("Creation date"),
                                fieldWithPath("modifiedDate").description("Last modification date")
                        )
                ));
    }
} 