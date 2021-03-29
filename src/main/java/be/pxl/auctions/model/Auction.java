package be.pxl.auctions.model;

import be.pxl.auctions.util.exception.InvalidBidException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctions")
@NamedQueries(value = {@NamedQuery(name = "findAllAuctions", query = "select a from Auction a")})
public class Auction {
    @Id
    @GeneratedValue
    private long id;
    private String description;
    private LocalDate endDate;
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    public List<Bid> bids = new ArrayList<>();

    public Auction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void addBid(Bid bid) {
        if (bid.getDate().isAfter(endDate)) {
            throw new InvalidBidException("This auction is closed.");
        }
        Bid highestBid = findHighestBid();
        if (highestBid != null && bid.getAmount() <= highestBid.getAmount()) {
            throw new InvalidBidException("Bid not allowed. Your bid should exceed €" + highestBid.getAmount());
        }
        bids.add(bid);
        bid.setAuction(this);
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Bid findHighestBid() {
        if (bids.isEmpty()) {
            return null;
        }
        Bid highestBid = bids.get(0);
        for (int i = 1; i < bids.size(); i++) {
            if (bids.get(i).getAmount() > highestBid.getAmount()) {
                highestBid = bids.get(i);
            }
        }
        return highestBid;
    }

}
