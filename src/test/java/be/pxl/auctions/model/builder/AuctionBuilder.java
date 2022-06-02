package be.pxl.auctions.model.builder;

import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AuctionBuilder {

	public List<Bid> bids = new ArrayList<>();
	private long id;
	private String description;
	private LocalDate endDate;

	private AuctionBuilder() {}

	public static AuctionBuilder anAuction() {return new AuctionBuilder();}

	public AuctionBuilder withId(long id) {
		this.id = id;
		return this;
	}

	public AuctionBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public AuctionBuilder withEndDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public AuctionBuilder withBids(List<Bid> bids) {
		this.bids = bids;
		return this;
	}

	public Auction build() {
		Auction auction = new Auction();
		auction.setId(id);
		auction.setDescription(description);
		auction.setEndDate(endDate);
		auction.setBids(bids);
		return auction;
	}
}
