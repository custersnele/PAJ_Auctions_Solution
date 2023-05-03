package be.pxl.auctions.rest;

import be.pxl.auctions.model.builder.AuctionDTOBuilder;
import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.service.AuctionService;
import be.pxl.auctions.service.BidService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = AuctionRest.class)
public class AuctionRestCreateAuctionTest {

	private DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BidService bidService;
	@MockBean
	private AuctionService auctionService;

	@Test
	public void badRequestWhenNoDescription() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/auctions")
						.content("{\"description\":\" \", \"endDate\":\"2022-07-01\"}")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void newAuctionIsCreated() throws Exception {
		LocalDate endDate = LocalDate.now().plusDays(5);
		when(auctionService.createAuction(Mockito.any(AuctionCreateResource.class)))
				.thenReturn(AuctionDTOBuilder.anAuctionDTO().withId(7L).withDescription("My auction").withEndDate(endDate).build());

		mockMvc.perform(MockMvcRequestBuilders.post("/auctions")
						.content("{\"description\":\"My auction\", \"endDate\" : \"" + FORMATTER.format(endDate) + "\" }")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("My auction"));
	}
}
