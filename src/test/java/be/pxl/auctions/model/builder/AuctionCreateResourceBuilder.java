package be.pxl.auctions.model.builder;

import be.pxl.auctions.rest.resource.AuctionCreateResource;

import java.time.LocalDate;

public final class AuctionCreateResourceBuilder {
    public static final String DESCRIPTION = "My Auction";
    public static final LocalDate ENDDATE = LocalDate.now().plusDays(4);

    private String description = DESCRIPTION;
    private LocalDate endDate = ENDDATE;

    private AuctionCreateResourceBuilder() {
    }

    public static AuctionCreateResourceBuilder anAuctionCreateResource() {
        return new AuctionCreateResourceBuilder();
    }

    public AuctionCreateResourceBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AuctionCreateResourceBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public AuctionCreateResource build() {
        AuctionCreateResource auctionCreateResource = new AuctionCreateResource();
        auctionCreateResource.setDescription(description);
        auctionCreateResource.setEndDate(endDate);
        return auctionCreateResource;
    }
}
