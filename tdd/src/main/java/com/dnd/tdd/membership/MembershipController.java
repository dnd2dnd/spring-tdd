package com.dnd.tdd.membership;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MembershipController {

	private final MembershipService membershipService;

	@PostMapping("/api/v1/membership")
	public ResponseEntity<MembershipAddResponse> addMembership(
		@Valid @RequestBody MembershipRequest membershipRequest
	) {
		MembershipAddResponse response = membershipService.addMembership(membershipRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}

	@GetMapping("/api/v1/membership")
	public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
		@RequestParam(name = "userId") String userId
	) {
		List<MembershipDetailResponse> responses = membershipService.getMembershipList(userId);
		return ResponseEntity.ok().body(responses);
	}

	@GetMapping("/api/v1/membership/{id}")
	public ResponseEntity<MembershipDetailResponse> getMembership(
		@PathVariable(name = "id") Long id,
		@RequestParam(name = "userId") String userId
	) {
		MembershipDetailResponse responses = membershipService.getMembership(id, userId);
		return ResponseEntity.ok().body(responses);
	}
}
