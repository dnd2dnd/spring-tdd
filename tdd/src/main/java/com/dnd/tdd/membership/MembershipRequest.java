package com.dnd.tdd.membership;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MembershipRequest (
	@NotNull
	String userId,
	@NotNull
	MembershipType membershipType,
	@NotNull
	@Min(0)
	Integer point
){
}
