package be.pxl.auctions.service;

import be.pxl.auctions.repository.AuctionRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.builder.AuctionBuilder;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceGetAuctionByIdTest {

    private static final long AUCTION_ID = 5L;

    @Mock
    private AuctionRepository auctionRepository;
    @InjectMocks
    private AuctionService auctionService;
    private Auction auction;

    @BeforeEach
    void init() {
        auction = AuctionBuilder.anAuction().withId(5).build();
    }

    @Test
    public void throwsAuctionNotFoundExceptionWhenNoAuctionWithGivenIdFound() {
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class, () -> auctionService.getAuctionById(AUCTION_ID));
    }

    @Test
    public void returnsAuctionDTOWhenAuctionFoundWithGivenId() {
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.of(auction));
        AuctionDTO auctionById = auctionService.getAuctionById(AUCTION_ID);
        assertEquals(AUCTION_ID, auctionById.getId());
        assertEquals(AuctionBuilder.DESCRIPTION, auctionById.getDescription());
        assertEquals(AuctionBuilder.END_DATE, auctionById.getEndDate());
    }
}
