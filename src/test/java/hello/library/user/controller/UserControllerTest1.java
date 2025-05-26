package hello.library.user.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import hello.library.user.dto.UserRequest;
import hello.library.user.entity.User;
import hello.library.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest1 {
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
    @DisplayName("유저 등록 성공")
    void registerUser_success() throws Exception {
        // given
        UserRequest request = UserRequest.builder()
            .username("홍길동")
            .email("email@email.com")
            .build();

        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        // DB에 저장됐는지 검증
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUsername()).isEqualTo("홍길동");
        assertThat(users.get(0).getEmail()).isEqualTo("email@email.com");
    }

    @Test
    @DisplayName("유저 등록 실패 - 필수 필드 누락")
    void registerBook_fail1() throws Exception {
        UserRequest request1 = UserRequest.builder()
            .email("aaa@naver.com")
            .build();

        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.username").value("이름은 필수입니다."));

        UserRequest request2 = UserRequest.builder()
            .username("김유신")
            .build();

        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.email").value("이메일은 필수입니다."));
    }

    @Test
    @DisplayName("유저 등록 실패 - 이미 존재하는 이메일")
    void registerBook_fail2() throws Exception {
        registerUser();

        UserRequest request = UserRequest.builder()
            .username("홍길동")
            .email("email@email.com")
            .build();

        // when & then
        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());

    }
    @Test
    @DisplayName("유저 삭제 by id 성공")
    void deleteBook_success() throws Exception {
        //given
        User user = registerUser(); //유저 저장
        Long userId = user.getUserId();
        // when & then
        mockMvc.perform(delete("/user/"+userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("유저 삭제 by id 실패- 존재하지 않는 id")
    void deleteBook_fail() throws Exception {

        // when & then
        mockMvc.perform(delete("/user/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
