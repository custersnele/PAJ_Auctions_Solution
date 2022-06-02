package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionRepository;
import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.model.builder.AuctionBuilder;
import be.pxl.auctions.model.builder.BidBuilder;
import be.pxl.auctions.model.builder.BidCreateResourceBuilder;
import be.pxl.auctions.model.builder.UserBuilder;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import be.pxl.auctions.util.exception.InvalidBidException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidServiceCreateBidTest {
    private static final long AUCTION_ID = 5L;
    private static final String USER_EMAIL = "sophie@pxl.be";

    @Mock
    private UserDao userDao;
    @Mock
    private AuctionRepository auctionRepository;
    @InjectMocks
    private BidService bidService;
    private BidCreateResource bidCreateResource;
    @Captor
    private ArgumentCaptor<Bid> bidCaptor;

    @BeforeEach
    void init() {
        bidCreateResource = BidCreateResourceBuilder.aBidCreateResource().build();
    }

    @Test
    void throwsRequiredFieldExceptionWhenBidHasNoEmail() {
        bidCreateResource.setEmail("");
        assertThrows(RequiredFieldException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidEmailExceptionWhenBidHasInvalidEmail() {
        bidCreateResource.setEmail("jenspanis.com");
        assertThrows(InvalidEmailException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsRequiredFieldExceptionWhenBidHasNoPrice() {
        bidCreateResource.setPrice(null);
        assertThrows(RequiredFieldException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsUserNotFoundExceptionWhenThereIsNoUserWithEmail() {
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsAuctionNotFoundExceptionWhenThereIsNoAuctionWithAuctionId() {
        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidBidExceptionWhenBidIsLowerThanCurrentPrice() {
        bidCreateResource.setPrice(50.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().build();
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withAmount(100.0).build();
        auction.getBids().add(bid);

        assertThrows(InvalidBidException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidBidExceptionWhenAuctionIsClosed() {
        bidCreateResource.setPrice(150.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().minusDays(5)).build();
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withAmount(100.0).build();
        auction.getBids().add(bid);

        assertThrows(InvalidBidException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void throwsInvalidBidExceptionWhenBidderHasHighestBid() {
        bidCreateResource.setPrice(150.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().plusDays(1)).build();
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.of(auction));

        Bid bid = BidBuilder.aBid().withUser(userDao.findUserByEmail(BidCreateResourceBuilder.EMAIL).get()).withAmount(100.0).build();
        auction.getBids().add(bid);

        assertThrows(InvalidBidException.class,
                () -> bidService.createBid(bidCreateResource, AUCTION_ID));
    }

    @Test
    void mapToBidDtoSavesUserCorrectly() {
        bidCreateResource.setPrice(150.0);

        User user = UserBuilder.anUser().build();
        when(userDao.findUserByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Auction auction = AuctionBuilder.anAuction().withEndDate(LocalDate.now().plusDays(1)).build();
        when(auctionRepository.findById(AUCTION_ID)).thenReturn(Optional.of(auction));

        BidDTO createdBid = bidService.createBid(bidCreateResource, AUCTION_ID);
        assertNotNull(createdBid);

	    Bid highestBid = auction.findHighestBid().get();

	    assertEquals("mark@facebook.com", highestBid.getUser().getEmail());
        assertEquals(150, highestBid.getAmount());
    }
}
