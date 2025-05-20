package hello.library.common.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorDetails.of(e));
	}

	// 다른 예외 처리도 추가 가능

	// 공통 응답 생성 메서드
	private ResponseEntity<ErrorDetails> buildErrorResponse(ErrorCode code, String customMessage) {
		return ResponseEntity.status(code.getHttpStatus())
			.body(new ErrorDetails(code.getHttpStatus(), code.getErrorCode(), customMessage));
	}
}