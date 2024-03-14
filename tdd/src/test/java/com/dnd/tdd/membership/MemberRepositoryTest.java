package com.dnd.tdd.membership;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

	@Autowired
	MembershipRepository membershipRepository;

	@Test
	void 사용자를_등록한다() {
		// given
		Membership membership = Membership.builder()
			.userId("userId")
			.membershipType(MembershipType.NAVER)
			.point(10000)
			.build();

		// when
		membershipRepository.save(membership);
		final Membership result = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getUserId()).isEqualTo("userId");
		assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
		assertThat(result.getPoint()).isEqualTo(10000);
	}

	@Test
	void 멤버십조회_사이즈가_0인_경우() {
		// given

		// when
		List<Membership> result = membershipRepository.findAllByUserId("userId");

		// then
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	public void 멤버십조회_사이즈가_2인_경우() {
		// given
		final Membership naverMembership = Membership.builder()
			.userId("userId")
			.membershipType(MembershipType.NAVER)
			.point(10000)
			.build();

		final Membership kakaoMembership = Membership.builder()
			.userId("userId")
			.membershipType(MembershipType.KAKAO)
			.point(10000)
			.build();

		membershipRepository.save(naverMembership);
		membershipRepository.save(kakaoMembership);

		// when
		List<Membership> result = membershipRepository.findAllByUserId("userId");

		// then
		assertThat(result.size()).isEqualTo(2);
	}
}
