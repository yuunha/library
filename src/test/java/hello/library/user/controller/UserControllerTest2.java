package hello.library.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.library.user.entity.User;
import hello.library.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest2 {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    User registerUser(){
        // given
        User user = User.builder()
            .username("홍길동")
            .email("email@email.com")
            .build();
        return userRepository.save(user);
    }

    @Test
    @DisplayName("유저 조회 by id 성공")
    void getUser_success() throws Exception {
        // given
        User user = registerUser();
        Long userId = user.getUserId();

        mockMvc.perform(get("/user/" + userId )
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(user.getUserId()))
            .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("유저 조회 by id 실패- 존재하지 않는 Id")
    void getUser_fail() throws Exception {

        mockMvc.perform(get("/user/99" )
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

}
