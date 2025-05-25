package hello.library.book.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

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

import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
import hello.library.book.mapper.BookMapper;
import hello.library.book.repository.BookRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void clean() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("책 조회 성공")
    void getBookById_success() throws Exception {
        // given
        BookRequest request = BookRequest.builder()
            .bookName("모모")
            .author("미하엘 엔데")
            .publisher("비룡소")
            .publicationDate(LocalDate.of(2000, 1, 1))
            .isbn("9781234567890")
            .category("소설")
            .pages(300)
            .build();
        //Book 객체 저장
        Book savedBook = bookRepository.save(BookMapper.toEntity(request));

        Long bookId= savedBook.getBookId();
        // when & then
        mockMvc.perform(get("/book/"+bookId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bookId").value(savedBook.getBookId()))
            .andExpect(jsonPath("$.bookName").value("모모"))
            .andExpect(jsonPath("$.author").value("미하엘 엔데"))
            .andExpect(jsonPath("$.publisher").value("비룡소"))
            .andExpect(jsonPath("$.publicationDate").value("2000-01-01"))
            .andExpect(jsonPath("$.isbn").value("9781234567890"))
            .andExpect(jsonPath("$.category").value("소설"))
            .andExpect(jsonPath("$.pages").value(300));
    }
    @Test
    @DisplayName("책 조회 실패 - 존재하지 않는 bookId -> 404 반환")
    void getBookById_fail() throws Exception {
        // given: DB에 없는 id 사용 (예: 99999)
        Long bookId = 1L;
        // when & then
        mockMvc.perform(get("/book/" + bookId )
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("BOOK-001"));

    }
}
