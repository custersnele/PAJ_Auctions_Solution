package be.pxl.auctions.dao;

import be.pxl.auctions.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
	User saveUser(User user);
	Optional<User> findUserByEmail(String email);
	Optional<User> findUserById(long userId);
	List<User> findAllUsers();
}
