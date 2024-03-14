package com.dnd.tdd.membership;

import lombok.Builder;

@Builder
public record MembershipAddResponse(
	Long id,
	String membershipType
){
}
