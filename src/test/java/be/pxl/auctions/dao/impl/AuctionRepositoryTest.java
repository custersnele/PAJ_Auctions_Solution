package be.pxl.auctions.dao.impl;

import be.pxl.auctions.dao.AuctionRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.builder.AuctionBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class AuctionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AuctionRepository auctionRepository;

    @Test
    void auctionCanBeSavedAndRetrievedById() {
        Auction auction = AuctionBuilder.anAuction().build();
        long newAuctionId = auctionRepository.save(auction).getId();
        entityManager.flush();
        entityManager.clear();

        Optional<Auction> retrievedAuction = auctionRepository.findById(newAuctionId);
        assertTrue(retrievedAuction.isPresent());

        assertEquals(AuctionBuilder.END_DATE, retrievedAuction.get().getEndDate());
        assertEquals(AuctionBuilder.DESCRIPTION, retrievedAuction.get().getDescription());
    }

    @Test
    void findAllAuctions() {
        int currentAmountOfAuctions = auctionRepository.findAll().size();

        Auction auction = AuctionBuilder.anAuction().build();
        long newAuctionId = auctionRepository.save(auction).getId();
        entityManager.flush();
        entityManager.clear();

        List<Auction> auctionList = auctionRepository.findAll();
        assertFalse(auctionList.isEmpty());
        assertEquals(currentAmountOfAuctions + 1, auctionList.size());

        Optional<Auction> retrievedAuction = auctionRepository.findById(newAuctionId);
        assertTrue(retrievedAuction.isPresent());
	    assertThat(auctionList).contains(retrievedAuction.get());
    }
}
