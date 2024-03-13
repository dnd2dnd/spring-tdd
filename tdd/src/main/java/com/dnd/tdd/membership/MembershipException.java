package com.dnd.tdd.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MembershipException extends RuntimeException{

	private final MembershipErrorCode membershipErrorCode;

}
