package com.dnd.tdd.membership;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.dnd.tdd.membership.Membership;
import com.dnd.tdd.membership.MembershipRepository;
import com.dnd.tdd.membership.MembershipType;

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
}
