package com.dnd.tdd.membership;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MembershipRepository membershipRepository;


	private final String userId = "userId";
	private final MembershipType membershipType = MembershipType.NAVER;
	private final Integer point = 10000;

	@Test
	void 이미_존재하는_아이디와_멤버십타입_인경우_멤버십_등록에_실패한다() {
		// given
		doReturn(Membership.builder().build())
			.when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

		// when
		final MembershipException result = assertThrows(MembershipException.class, () -> memberService.addMembership(userId, membershipType, point));

		// then
		assertThat(result.getMembershipErrorCode()).isEqualTo(MembershipErrorCode.DUPLICATED_MEMBERSHIP_REGISTER);

	}

	@Test
	void 멤버십_등록에_성공한다() {
		// given
		doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
		doReturn(membership()).when(membershipRepository).save(any(Membership.class));

		// when
		final MembershipResponse result = memberService.addMembership(userId, membershipType, point);

		// then
		assertThat(result.membershipType()).isEqualTo(MembershipType.NAVER.getCompanyName());

		// verify
		verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
		verify(membershipRepository, times(1)).save(any(Membership.class));
	}

	private Membership membership() {
		return Membership.builder()
			.userId(userId)
			.point(point)
			.membershipType(membershipType)
			.build();
	}
}
