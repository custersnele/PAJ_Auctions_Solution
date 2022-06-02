package be.pxl.auctions.model.builder;

import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AuctionDTOBuilder {
    public List<BidDTO> bids = new ArrayList<>();
    private long id;
    private String description;
    private LocalDate endDate;
    private int numberOfBids;
    private BidDTO highestBid;

    private AuctionDTOBuilder() {
    }

    public static AuctionDTOBuilder anAuctionDTO() {
        return new AuctionDTOBuilder();
    }

    public AuctionDTOBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public AuctionDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AuctionDTOBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public AuctionDTOBuilder withBids(List<BidDTO> bids) {
        this.bids = bids;
        return this;
    }

    public AuctionDTOBuilder withNumberOfBids(int numberOfBids) {
        this.numberOfBids = numberOfBids;
        return this;
    }

    public AuctionDTOBuilder withHighestBid(BidDTO highestBid) {
        this.highestBid = highestBid;
        return this;
    }

    public AuctionDTO build() {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setId(id);
        auctionDTO.setDescription(description);
        auctionDTO.setEndDate(endDate);
        auctionDTO.setBids(bids);
        auctionDTO.setNumberOfBids(numberOfBids);
        auctionDTO.setHighestBid(highestBid);
        return auctionDTO;
    }
}
