package hello.library.loan.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import hello.library.book.entity.Book;
import hello.library.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId; //null 허용

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "user_id", nullable = false) // FK 컬럼명 지정
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDate loanDate;//대출일

    private LocalDate dueDate;//반납 예정일

    private LocalDate returnDate; //반납일

    public boolean isOverdue() { //loan.isOverdue()로 연체여부 확인
        // 반납일이 있고, 반납일이 예정일보다 늦었으면 연체
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        }

        // 아직 반납 안 했는데 오늘이 예정일보다 늦었으면 연체
        return LocalDate.now().isAfter(dueDate);
    }

}
