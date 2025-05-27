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
import hello.library.loan.service.LoanService;
import hello.library.user.entity.User;
import hello.library.user.repository.UserRepository;
import hello.library.user.service.UserService;

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

	@Autowired
	private LoanService loanService;

	@BeforeEach
	void clean() {
		loanRepository.deleteAll();
		userRepository.deleteAll();
		bookRepository.deleteAll();
	}

	User registerUser() {
		User user = User.builder()
				.username("홍길동")
				.email("aaa@naver.com")
			    .build();

		return userRepository.save(user);
	}

	Book registerBook() {
		Book book = Book.builder()
			.bookName("채근담")
			.isbn("11111111")
			.author("홍자성")
			.build();

		return bookRepository.save(book);
	}
	@Test
	@DisplayName("대출 성공")
	void registerLoan_success() throws Exception {
		long userId = registerUser().getUserId();
		long bookId = registerBook().getBookId();

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
	@Test
	@DisplayName("대출 실패 - 존재하지 않는 bookId")
	void registerLoan_fail1() throws Exception {
		// given
		Long userId = registerUser().getUserId();
		Long unexistBookId = 1L;

		// LoanRequest : userId + 존재하지 않는 bookId
		LoanRequest request = LoanRequest.builder()
			.userId(userId)
			.bookId(unexistBookId)
			.build();

		// when & then
		mockMvc.perform(post("/loan")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("도서를 찾을 수 없습니다."));
	}

	@Test
	@DisplayName("대출 실패 - 대출 불가능한 책(다른사람이 대출중)")
	void registerLoan_fail2() throws Exception {
		//given
		//1. 먼저 user가 대출 등록
		User user = registerUser();
		Book book = registerBook();
		Loan loan = Loan.builder()
			.user(user)
			.book(book)
			.build();
		loanRepository.save(loan);

		//2. newUser가 같은 책 대출
		User newUser = User.builder()
			.username("이이이")
			.email("bbb@naver.com")
			.build();
		userRepository.save(newUser);

		Long newUserId = newUser.getUserId();
		Long existedBookId = book.getBookId(); // user가 대출한 책 id

		LoanRequest request = LoanRequest.builder()
				.userId(newUserId)
			    .bookId(existedBookId)
				.build();

		//when & then
		mockMvc.perform(post("/loan")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.message").value("이미 대출 중인 도서입니다."));
	}
}