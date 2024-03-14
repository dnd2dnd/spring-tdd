package com.dnd.tdd.membership;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;

	@PostMapping("/api/v1/membership")
	public ResponseEntity<MembershipResponse> addMembership(
		@Valid @RequestBody MembershipRequest membershipRequest
	) {
		MembershipResponse response = membershipService.addMembership(membershipRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}
}
