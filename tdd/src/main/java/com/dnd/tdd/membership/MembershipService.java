package com.dnd.tdd.membership;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

	private final MembershipRepository membershipRepository;

	public MembershipAddResponse addMembership(MembershipRequest membershipRequest) {
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

		return MembershipAddResponse.builder()
			.id(membership.getId())
			.membershipType(membershipRequest.membershipType().getCompanyName())
			.build();
	}

	public List<MembershipDetailResponse> getMembershipList(String userId) {
		return membershipRepository.findAllByUserId(userId).stream()
			.map(MembershipDetailResponse::from)
			.toList();
	}

	public MembershipDetailResponse getMembership(Long membershipId, String userId) {
		Membership membership = membershipRepository.getById(membershipId);
		if(!membership.getUserId().equals(userId)) {
			throw new MembershipException(MembershipErrorCode.NOT_MEMBERSHIP_OWNER);
		}

		return MembershipDetailResponse.from(membership);
	}

	public void removeMembership(Long membershipId, String userId) {
		Membership membership = membershipRepository.getById(membershipId);
		membership.isOwner(userId);
		membershipRepository.deleteById(membership.getId());
	}
}
