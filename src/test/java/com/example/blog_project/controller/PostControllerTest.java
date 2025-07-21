package com.example.blog_project.controller;

import com.example.blog_project.post.Post;
import com.example.blog_project.post.PostRepository;
import com.example.blog_project.post.PostRequest;
import com.example.blog_project.user.LoginRequest;
import com.example.blog_project.user.Role;
import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setup() throws Exception
    {
        User user = new User();
        user.setUsername("user1");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setEmail("user1@gmail.com");
        user.setRole(Role.USER);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest("user1", "12345678");

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        token = response;
    }

    @Test
    @DisplayName("게시글 작성")
    void create_post() throws Exception
    {
        PostRequest postRequest = new PostRequest("제목입니다", "내용입니다.");
        mockMvc.perform(post("/posts")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 수정")
    void update_post() throws Exception {
        PostRequest postRequest = new PostRequest("제목입니다", "내용입니다.");
        mockMvc.perform(post("/posts")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)));
        User user = userRepository.findByUsername("user1").get();
        Post post = postRepository.findByUser(user).get(0);

        PostRequest updateRequest = new PostRequest("수정된 제목입니다.", "수정 내용");
        mockMvc.perform(put("/posts/" + post.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        Post updatedPost = postRepository.findById(post.getId()).get();
        assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목입니다.");
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete_post() throws Exception {
        PostRequest postRequest = new PostRequest("제목입니다", "내용입니다.");
        mockMvc.perform(post("/posts")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)));
        User user = userRepository.findByUsername("user1").get();
        Post post = postRepository.findByUser(user).get(0);

        mockMvc.perform(delete("/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }
}
