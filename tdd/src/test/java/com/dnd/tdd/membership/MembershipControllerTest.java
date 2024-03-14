package com.dnd.tdd.membership;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

	@InjectMocks
	private MembershipController membershipController;
	private MockMvc mockMvc;
	private Gson gson;

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
			.build();
	}

	@Test
	void 포인트가_음수라서_멤버십_등록에_실패한다() throws Exception {
		// given
		final String url = "/api/v1/membership";

		// when
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post(url)
				.content(gson.toJson(new MembershipRequest(MembershipType.NAVER, 10000)))
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isBadRequest());
	}

}
