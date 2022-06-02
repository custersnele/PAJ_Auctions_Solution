package be.pxl.auctions.model;

import be.pxl.auctions.model.builder.AuctionBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuctionIsFinishedTest {

	@Test
	public void returnsTrueIfAuctionFinishedYesterday() {
		Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().minusDays(1)).build();
		assertTrue(auction.isFinished());
	}

	@Test
	public void returnsFalseIfAuctionFinishesToday() {
		Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now()).build();
		assertFalse(auction.isFinished());
	}

	@Test
	public void returnsFalseIfAuctionFinishesInFuture() {
		Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().plusDays(2)).build();
		assertFalse(auction.isFinished());
	}
}
