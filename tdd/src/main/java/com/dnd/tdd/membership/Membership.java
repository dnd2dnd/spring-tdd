package com.dnd.tdd.membership;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String userId;

	@Column
	private MembershipType membershipType;

	@Column
	private Integer point;

	@Builder
	public Membership(String userId, MembershipType membershipType, Integer point) {
		this.userId = userId;
		this.membershipType = membershipType;
		this.point = point;
	}
}
