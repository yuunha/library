package hello.library.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {
	//Book
	BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK-001", "도서를 찾을 수 없습니다."),
	DUPLICATE_ISBN(HttpStatus.BAD_REQUEST, "BOOK-002", "이미 존재하는 ISBN입니다."),

	//User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "해당 유저가 존재하지 않습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 이메일입니다."),

	//DB
	DB_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "COMMON-001", "데이터 무결성 제약조건 위반");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
	}
}
