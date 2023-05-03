package be.pxl.auctions.service;

import be.pxl.auctions.repository.AuctionRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.builder.AuctionBuilder;
import be.pxl.auctions.rest.resource.AuctionDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceGetAllAuctionsTest {

    private static final long AUCTION_ID = 5L;

    @Mock
    private AuctionRepository auctionRepository;
    @InjectMocks
    private AuctionService auctionService;

    @Test
    public void returnsListOfAuctionDTOS() {
        Auction auction1 = AuctionBuilder.anAuction().withId(AUCTION_ID).build();
        Auction auction2 = AuctionBuilder.anAuction().withId(AUCTION_ID + 5).build();

        when(auctionRepository.findAll()).thenReturn(Arrays.asList(auction1, auction2));
        List<AuctionDTO> actualList = auctionService.getAllAuctions();

        assertEquals(actualList.size(), 2);
	    List<Long> retrievedIds = actualList.stream().map(AuctionDTO::getId).collect(Collectors.toList());
	    Assertions.assertThat(retrievedIds).containsExactly(AUCTION_ID, AUCTION_ID + 5);
    }
}
