package com.siheung.seemoney.domain.auth.controller;

import com.siheung.seemoney.domain.user.entity.User;
import com.siheung.seemoney.domain.user.repository.UserRepository;
import com.siheung.seemoney.domain.auth.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String test() {
        return "auth ok";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return "로그인 성공";
    }
}