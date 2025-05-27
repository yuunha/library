package hello.library.loan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hello.library.book.entity.Book;
import hello.library.book.repository.BookRepository;
import hello.library.common.exception.BusinessException;
import hello.library.common.exception.ErrorCode;
import hello.library.loan.dto.LoanRequest;
import hello.library.loan.entity.Loan;
import hello.library.loan.repository.LoanRepository;
import hello.library.user.entity.User;
import hello.library.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void registerLoan(LoanRequest request) {
        //1. User, Book 객체 조회
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        //2. 반납 안된 책 존재 -> 대출 불가
        List<Loan> activeLoans = loanRepository.findByBookAndReturnDateIsNull(book);

        if (!activeLoans.isEmpty()) {
            // 하나라도 반납되지 않은 대출이 있으면 대출 불가
            throw new BusinessException(ErrorCode.Loaned_Book);
        }

        //3. 새로운 대출 생성
        Loan loan = Loan.builder()
            .user(user)
            .book(book)
            .loanDate(LocalDate.now())
            .dueDate(LocalDate.now().plusWeeks(2))
            .build();

        loanRepository.save(loan);
    }

}
