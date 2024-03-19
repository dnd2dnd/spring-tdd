package com.dnd.tdd.membership;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

	@InjectMocks
	private MembershipService membershipService;

	@Mock
	private MembershipRepository membershipRepository;


	private final Long membershipId = 1L;
	private final String userId = "userId";
	private final MembershipType membershipType = MembershipType.NAVER;
	private final Integer point = 10000;

	@Test
	void 이미_존재하는_아이디와_멤버십타입_인경우_멤버십_등록에_실패한다() {
		// given
		doReturn(Membership.builder().build())
			.when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

		// when
		final MembershipException result = assertThrows(
			MembershipException.class, () -> {
				membershipService.addMembership(new MembershipRequest("userId", MembershipType.NAVER, 10000));
				}
			);

		// then
		assertThat(result.getMembershipErrorCode()).isEqualTo(MembershipErrorCode.DUPLICATED_MEMBERSHIP_REGISTER);

	}

	private Membership membership() {
		return Membership.builder()
			.userId(userId)
			.point(point)
			.membershipType(membershipType)
			.build();
	}

	@Test
	void 멤버십_등록에_성공한다() {
		// given
		doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
		doReturn(membership()).when(membershipRepository).save(any(Membership.class));

		// when
		final MembershipAddResponse result = membershipService.addMembership(
			new MembershipRequest("userId", MembershipType.NAVER, 10000));

		// then
		assertThat(result.membershipType()).isEqualTo(MembershipType.NAVER.getCompanyName());

		// verify
		verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
		verify(membershipRepository, times(1)).save(any(Membership.class));
	}

	@Test
	void 멤버십목록을_조회한다() {
		// given
		doReturn(Arrays.asList(
			Membership.builder().build(),
			Membership.builder().build(),
			Membership.builder().build()
		)).when(membershipRepository).findAllByUserId(userId);

		// when
		final List<MembershipDetailResponse> result = membershipService.getMembershipList(userId);

		// then
		assertThat(result.size()).isEqualTo(3);
	}

	@Test
	void 멤버십상세조회실패_값이_없다() {
		// given
		given(membershipRepository.getById(anyLong()))
				.willThrow(new MembershipException(MembershipErrorCode.MEMBERSHIP_NOT_FOUND));

		// when
		final MembershipException result = assertThrows(MembershipException.class,
				() -> membershipService.getMembership(membershipId, "notOwner"));

		// then
		assertThat(result.getMembershipErrorCode()).isEqualTo(MembershipErrorCode.MEMBERSHIP_NOT_FOUND);
	}

	@Test
	void 멤버십상세조회실패_본인이아님() {
		// given
		doReturn(membership()).when(membershipRepository).getById(membershipId);

		// when
		final MembershipException result = assertThrows(MembershipException.class,
			() -> membershipService.getMembership(membershipId, "notOwner"));

		// then
		assertThat(result.getMembershipErrorCode()).isEqualTo(MembershipErrorCode.NOT_MEMBERSHIP_OWNER);
	}

	@Test
	void 멤버십상세조회에_성공한다() {
		// given
		doReturn(membership()).when(membershipRepository).getById(membershipId);

		// when
		final MembershipDetailResponse result = membershipService.getMembership(membershipId, userId);

		// then
		assertThat(result.userId()).isEqualTo(userId);
		assertThat(result.point()).isEqualTo(point);
	}

	@Nested
	class 멤버십삭제 {
		@Test
		void 본인이_아닌_경우_멤버십삭제에_실패한다() {
			// given
			final Membership membership = membership();
			doReturn(membership()).when(membershipRepository).getById(membershipId);

			// when
			final MembershipException result = assertThrows(MembershipException.class,
				() -> membershipService.removeMembership(membership.getId(), userId)
			);

			// then
			assertThat(result.getMembershipErrorCode()).isEqualTo(MembershipErrorCode.NOT_MEMBERSHIP_OWNER);
		}

		@Test
		void 멤버십삭제에_성공한다() {
			// given
			final Membership membership = membership();
			doReturn(membership()).when(membershipRepository).getById(membershipId);

			// when
			membershipService.removeMembership(membership.getId(), userId);

			// then

		}
	}

	// private Membership membership() {
	// 	return Membership.builder()
	// 		.userId(userId)
	// 		.point(point)
	// 		.membershipType(membershipType)
	// 		.build();
	// }
}
