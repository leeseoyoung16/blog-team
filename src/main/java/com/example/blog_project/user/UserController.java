package com.example.blog_project.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController
{
    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signin(@Valid @RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
