package hello.library.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {
	BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK-001", "도서를 찾을 수 없습니다."),
	DUPLICATE_ISBN(HttpStatus.BAD_REQUEST, "BOOK-002", "이미 존재하는 ISBN입니다.");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
	}
}
