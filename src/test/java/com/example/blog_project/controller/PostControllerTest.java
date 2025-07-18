package com.example.blog_project.controller;

import com.example.blog_project.user.Role;
import com.example.blog_project.user.User;
import com.example.blog_project.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setEmail("user1@gmail.com");
        user.setRole(Role.USER);
        userRepository.save(user);
    }
}
