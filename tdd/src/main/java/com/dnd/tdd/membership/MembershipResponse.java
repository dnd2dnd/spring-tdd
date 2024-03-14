package com.dnd.tdd.membership;

import lombok.Builder;

@Builder
public record MembershipResponse (
	Long id,
	String membershipType
){
}
