package com.example.profile.controller;

import com.example.profile.model.User;
import com.example.profile.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getUser() throws Exception {
        User user = new User();
        user.setUserId("test123");
        user.setName("Test");
        user.setLoginId("testuser");
        user.setPhoneNumber("010-1234-5678");

        Mockito.when(userService.getUserByUserId(anyString())).thenReturn(user);

        mockMvc.perform(get("/profile/user/{userId}", "test123"))
                .andExpect(status().isOk())
                .andDo(document("get-user"));
    }

    @Test
    void createUser() throws Exception {
        User user = new User();
        user.setUserId("test123");
        user.setName("Test");
        user.setLoginId("testuser");
        user.setPhoneNumber("010-1234-5678");

        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        String json = """
            {
                "name": "Test",
                "loginId": "testuser",
                "phoneNumber": "010-1234-5678",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/profile/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(document("create-user"));
    }

    @Test
    void updateUser() throws Exception {
        User user = new User();
        user.setUserId("test123");
        user.setName("Updated");
        user.setLoginId("testuser");
        user.setPhoneNumber("010-9999-8888");

        Mockito.when(userService.updateUser(anyString(), any(User.class))).thenReturn(user);

        String json = """
            {
                "name": "Updated",
                "loginId": "testuser",
                "phoneNumber": "010-9999-8888"
            }
            """;

        mockMvc.perform(put("/profile/user/{userId}", "test123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(document("update-user"));
    }

    @Test
    void deleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/profile/user/{userId}", "test123"))
                .andExpect(status().isOk())
                .andDo(document("delete-user"));
    }
} 