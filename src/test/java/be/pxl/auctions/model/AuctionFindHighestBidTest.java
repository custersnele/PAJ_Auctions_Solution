package be.pxl.auctions.model;

import be.pxl.auctions.model.builder.AuctionBuilder;
import be.pxl.auctions.model.builder.BidBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuctionFindHighestBidTest {

	private static final int HIGH_BID_AMOUNT = 1000;
	private final Bid lowBid = BidBuilder.aBid().withAmount(1).build();
	private final Bid highBid = BidBuilder.aBid().withAmount(HIGH_BID_AMOUNT).build();

	@Test
	public void returnsEmptyOptionalWhenThereAreNoBids() {
		Auction auction = AuctionBuilder.anAuction().build();
		assertTrue(auction.findHighestBid().isEmpty());
	}

	@Test
	public void returnsBidWithHighestAmount() {
		Auction auction = AuctionBuilder.anAuction().withBids(Arrays.asList(lowBid, highBid)).build();
		Optional<Bid> highestBid = auction.findHighestBid();
		assertTrue(highestBid.isPresent());
		assertEquals(1000, highestBid.get().getAmount());
	}

}
