package hello.library.loan.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.library.book.entity.Book;
import hello.library.loan.entity.Loan;
import hello.library.user.entity.User;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByBook(Book book);
    // 대출 중인 책만 조회 (반납 안 된 경우)
    List<Loan> findByBookAndReturnDateIsNull(Book book);

}
