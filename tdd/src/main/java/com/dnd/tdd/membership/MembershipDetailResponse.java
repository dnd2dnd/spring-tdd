package com.dnd.tdd.membership;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record MembershipDetailResponse(
	Long id,
	String userId,
	MembershipType membershipType,
	Integer point,
	LocalDateTime createAt,
	LocalDateTime updateAt
) {
	public static MembershipDetailResponse from(Membership membership) {
		return MembershipDetailResponse.builder()
			.id(membership.getId())
			.userId(membership.getUserId())
			.membershipType(membership.getMembershipType())
			.point(membership.getPoint())
			.createAt(membership.getCreateAt())
			.updateAt(membership.getUpdateAt())
			.build();
	}
}
