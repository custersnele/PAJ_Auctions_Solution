package be.pxl.auctions.model.builder;

import be.pxl.auctions.rest.resource.BidCreateResource;

public final class BidCreateResourceBuilder {
    public static final String EMAIL = "sophie@pxl.be";
    public static final Double PRICE = 22.5;

    private String email = EMAIL;
    private Double price = PRICE;

    private BidCreateResourceBuilder() {
    }

    public static BidCreateResourceBuilder aBidCreateResource() {
        return new BidCreateResourceBuilder();
    }

    public BidCreateResourceBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BidCreateResourceBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public BidCreateResource build() {
        BidCreateResource bidCreateResource = new BidCreateResource();
        bidCreateResource.setEmail(email);
        bidCreateResource.setPrice(price);
        return bidCreateResource;
    }
}
