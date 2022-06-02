package be.pxl.auctions.rest;

import be.pxl.auctions.rest.resource.AuctionCreateResource;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.service.AuctionService;
import be.pxl.auctions.service.BidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("auctions")
public class AuctionRest {
    private static final Logger LOGGER = LogManager.getLogger(AuctionRest.class);

    private final AuctionService auctionService;
	private final BidService bidService;

	@Autowired
	public AuctionRest(AuctionService auctionService, BidService bidService) {
		this.auctionService = auctionService;
		this.bidService = bidService;
	}

	@GetMapping
    public List<AuctionDTO> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("{auctionId}")
    public AuctionDTO getAuctionById(@PathVariable("auctionId") long auctionId) {
        return auctionService.getAuctionById(auctionId);
    }

    @PostMapping
    public AuctionDTO createAuction(@RequestBody @Valid AuctionCreateResource auctionCreateResource) {
        return auctionService.createAuction(auctionCreateResource);
    }

	@PostMapping("/{auctionId}/bids")
	public BidDTO createBid(
			@RequestBody BidCreateResource bidCreateResource,
			@PathVariable("auctionId") long auctionId
	) {
		return bidService.createBid(bidCreateResource, auctionId);
	};
}
