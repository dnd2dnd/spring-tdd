package com.dnd.tdd.membership;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MembershipRepository membershipRepository;

	public MembershipResponse addMembership(String userId, MembershipType membershipType, Integer point) {
		Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
		if(result != null) {
			throw new MembershipException(MembershipErrorCode.DUPLICATED_MEMBERSHIP_REGISTER);
		}

		Membership membership = Membership.builder()
			.userId(userId)
			.membershipType(membershipType)
			.point(point)
			.build();
		membershipRepository.save(membership);

		return MembershipResponse.builder()
			.id(membership.getId())
			.membershipType(membershipType.getCompanyName())
			.build();
	}

}
