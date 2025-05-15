package hello.library.book.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
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

import hello.library.book.dto.BookRequest;
import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import hello.library.book.service.BookService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

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
	@DisplayName("책 등록 성공")
	void registerBook_success() throws Exception {
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

		// when & then
		mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.bookName").value("모모"))
			.andExpect(jsonPath("$.author").value("미하엘 엔데"));

		// DB에 저장됐는지 검증
		List<Book> books = bookRepository.findAll();
		assertThat(books).hasSize(1);
		assertThat(books.get(0).getIsbn()).isEqualTo("9781234567890");
	}
}
