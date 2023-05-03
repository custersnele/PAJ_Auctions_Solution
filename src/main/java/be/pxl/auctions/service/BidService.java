package be.pxl.auctions.service;

import be.pxl.auctions.repository.AuctionRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.model.User;
import be.pxl.auctions.repository.UserRepository;
import be.pxl.auctions.rest.resource.BidCreateResource;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Service
public class BidService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public BidService(AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BidDTO createBid(BidCreateResource bidInfo, long auctionId) {
        User existingUser = userRepository.findUserByEmail(bidInfo.getEmail()).orElseThrow(() -> new UserNotFoundException("No user found with email:" + bidInfo.getEmail()));
        Auction existingAuction = auctionRepository.findById(auctionId).orElseThrow(() ->  new AuctionNotFoundException("No auction found with id:" + auctionId));
        Bid bid = mapToBid(bidInfo, existingUser);
		existingAuction.addBid(bid);
		return mapToBidDTO(bid);
    }

    private BidDTO mapToBidDTO(Bid saveBid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(saveBid.getId());
        bidDTO.setAmount(saveBid.getAmount());
        bidDTO.setDate(saveBid.getDate());
        bidDTO.setUser(mapUserDTO(saveBid.getUser()));
        return bidDTO;
    }

    private UserDTO mapUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setAge(user.getAge());
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        return userDTO;
    }

    private Bid mapToBid(BidCreateResource bidInfo, User user) {
        Bid bid = new Bid();
        bid.setAmount(bidInfo.getPrice());
        bid.setUser(user);
        bid.setDate(LocalDate.now());
        return bid;
    }

}
