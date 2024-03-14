package com.dnd.tdd;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dnd.tdd.membership.MembershipErrorCode;
import com.dnd.tdd.membership.MembershipException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({MembershipException.class})
	public ResponseEntity<ErrorResponse> handleRestApiException(final MembershipException exception) {
		log.warn("MembershipException occur: ", exception);
		return this.makeErrorResponseEntity(exception.getMembershipErrorCode());
	}

	private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final MembershipErrorCode errorResult) {
		return ResponseEntity.status(errorResult.getHttpStatus())
			.body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
	}

	record ErrorResponse(
		String code,
		String message) {
	}
}
