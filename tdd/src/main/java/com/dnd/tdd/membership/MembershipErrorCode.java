package com.dnd.tdd.membership;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorCode {
	DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
	MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not Found"),
	NOT_MEMBERSHIP_OWNER(HttpStatus.FORBIDDEN, "Not Membership Owner");

	private final HttpStatus httpStatus;
	private final String message;
}
