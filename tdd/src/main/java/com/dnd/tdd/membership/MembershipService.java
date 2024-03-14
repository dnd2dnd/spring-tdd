package com.dnd.tdd.membership;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

	private final MembershipRepository membershipRepository;

	public MembershipResponse addMembership(MembershipRequest membershipRequest) {
		Membership result = membershipRepository.findByUserIdAndMembershipType(membershipRequest.userId(), membershipRequest.membershipType());
		if(result != null) {
			throw new MembershipException(MembershipErrorCode.DUPLICATED_MEMBERSHIP_REGISTER);
		}

		Membership membership = Membership.builder()
			.userId(membershipRequest.userId())
			.membershipType(membershipRequest.membershipType())
			.point(membershipRequest.point())
			.build();
		membershipRepository.save(membership);

		return MembershipResponse.builder()
			.id(membership.getId())
			.membershipType(membershipRequest.membershipType().getCompanyName())
			.build();
	}

}
