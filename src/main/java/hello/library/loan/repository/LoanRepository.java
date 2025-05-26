package hello.library.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.library.book.entity.Book;

public interface LoanRepository extends JpaRepository<Book, Long> {

}
