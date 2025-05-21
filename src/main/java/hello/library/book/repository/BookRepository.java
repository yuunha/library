package hello.library.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.library.book.entity.Book;
import jakarta.validation.constraints.NotBlank;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByBookNameContainingIgnoreCase(String keyword); // 제목 키워드로 검색

	boolean existsByIsbn(@NotBlank(message = "ISBN은 필수입니다.") String isbn);
}