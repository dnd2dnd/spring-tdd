package com.dnd.tdd.membership;

public record MembershipRequest (
	MembershipType membershipType,
	Integer point
){
}
