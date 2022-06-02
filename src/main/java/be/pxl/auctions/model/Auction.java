package be.pxl.auctions.model;

import be.pxl.auctions.util.exception.InvalidBidException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Bid> highestBid = findHighestBid();
        if (highestBid.isPresent() && bid.getAmount() <= highestBid.get().getAmount()) {
            throw new InvalidBidException("Bid not allowed. Your bid should exceed €" + highestBid.get().getAmount());
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

    public Optional<Bid> findHighestBid() {
       return bids.stream().max((b1, b2) -> Double.compare(b1.getAmount(), b2.getAmount()));
    }

	public boolean isFinished() {
		return LocalDate.now().isAfter(endDate);
	}
}
