package hello.library.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; //null 허용

    private String username;

    private String email;

    // Optional: 연체 상태 캐시용 필드 (비즈니스 필요시)
    //private boolean blocked;

    // 연체 여부는 다음과 같이 계산할 수도 있음
    // public boolean hasOverdueLoan() {
    //     return loans.stream().anyMatch(Loan::isOverdue);
    // }

}
