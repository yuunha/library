package hello.library.loan.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {
	@NotNull(message = "사용자 ID는 필수입니다.")
	private Long userId;

	@NotNull(message = "도서 ID는 필수입니다.")
	private Long bookId;

}
