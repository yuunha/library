package hello.library.book.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
		// 에러 메시지 커스텀 가능
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body("해당 책이 존재하지 않습니다.");
	}

	// 다른 예외 처리도 추가 가능
}