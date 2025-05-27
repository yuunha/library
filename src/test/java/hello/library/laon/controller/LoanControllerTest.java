package hello.library.laon.controller;

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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import hello.library.loan.dto.LoanRequest;
import hello.library.loan.entity.Loan;
import hello.library.loan.repository.LoanRepository;
import hello.library.user.entity.User;
import hello.library.user.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LoanControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@BeforeEach
	void clean() {
		loanRepository.deleteAll();
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}

	long registerUser() throws Exception {
		User user = User.builder()
				.username("홍길동")
				.email("aaa@naver.com")
			    .build();
		User savedUser = userRepository.save(user);
		return savedUser.getUserId();
	}

	long registerBook() throws Exception {
		Book book = Book.builder()
			.bookName("채근담")
			.isbn("11111111")
			.author("홍자성")
			.build();
		Book savedBook = bookRepository.save(book);
		return savedBook.getBookId();
	}
	@Test
	@DisplayName("대출 성공")
	void registerLoan_success() throws Exception {
		long userId = registerUser();
		long bookId = registerBook();

		LoanRequest request = LoanRequest.builder()
			.userId(userId)
			.bookId(bookId)
			.build();

		// when & then
		mockMvc.perform(post("/loan")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());
		// DB에 저장됐는지 검증
		List<Loan> loans = loanRepository.findAll();
		assertThat(loans).hasSize(1);
		assertThat(loans.get(0).getBook().getBookName()).isEqualTo("채근담");
		assertThat(loans.get(0).getBook().getAuthor()).isEqualTo("홍자성");
		assertThat(loans.get(0).getUser().getUsername()).isEqualTo("홍길동");
	}
}