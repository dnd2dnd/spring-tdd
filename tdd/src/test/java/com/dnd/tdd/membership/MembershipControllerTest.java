package com.dnd.tdd.membership;

import static com.dnd.tdd.membership.MembershipType.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dnd.tdd.GlobalExceptionHandler;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

	@InjectMocks
	private MembershipController membershipController;

	@Mock
	private MembershipService membershipService;
	private MockMvc mockMvc;
	private Gson gson;

	@BeforeEach
	public void init() {
		gson = new Gson();
		mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();
	}

	@Test
	void 중복으로_인한_멤버십_등록에_실패한다() throws Exception {
		// given
		final String url = "/api/v1/membership";
		doThrow(new MembershipException(MembershipErrorCode.DUPLICATED_MEMBERSHIP_REGISTER))
			.when(membershipService)
			.addMembership(new MembershipRequest("userId", NAVER, 10000));

		// when
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post(url)
				.content(gson.toJson(new MembershipRequest("userId", NAVER, 10000)))
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@MethodSource("invalidMembershipAddParameters")
	void 잘못된_값으로_인하여_멤버십_등록에_실패한다(
		final String userId,
		final MembershipType membershipType,
		final Integer point
	) throws Exception {
		// given
		final String url = "/api/v1/membership";

		// then
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post(url)
				.content(gson.toJson(new MembershipRequest(userId, membershipType, point)))
				.contentType(MediaType.APPLICATION_JSON)
		);

		// when
		resultActions.andExpect(status().isBadRequest());
	}

	@Test
	void 멤버십_등록에_성공한다() throws Exception {
		// given
		final String url = "/api/v1/membership";

		// when
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post(url)
				.content(gson.toJson(new MembershipRequest("userId", NAVER, 10000)))
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isCreated());
	}

	@Test
	void 멤버십목록_조회에_성공한다() throws Exception {
		// given
		final String url = "/api/v1/membership";
		doReturn(Arrays.asList(
			MembershipDetailResponse.builder().build(),
			MembershipDetailResponse.builder().build(),
			MembershipDetailResponse.builder().build()
		)).when(membershipService).getMembershipList("userId");

		// when
		final ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get(url)
				.param("userId", "userId")
		);

		// then
		resultActions.andExpect(status().isOk());
	}

	private static Stream<Arguments> invalidMembershipAddParameters() {
		return Stream.of(
			Arguments.of(null, NAVER, 10000),
			Arguments.of("userId", null, 10000),
			Arguments.of("userId", NAVER, -1)
		);
	}
}
