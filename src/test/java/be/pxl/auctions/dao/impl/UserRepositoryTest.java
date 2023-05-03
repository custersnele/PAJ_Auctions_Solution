package be.pxl.auctions.dao.impl;

import be.pxl.auctions.model.User;
import be.pxl.auctions.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void userCanBeSavedAndRetrievedById() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		long newUserId = userRepository.save(user).getId();
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userRepository.findById(newUserId);
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
		assertEquals(user.getLastName(), retrievedUser.get().getLastName());
		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.get().getDateOfBirth());
	}

	@Test
	public void userCanBeSavedAndRetrievedByEmail() {
		User user = new User();
		user.setFirstName("Elly");
		user.setLastName("Young");
		user.setDateOfBirth(LocalDate.of(2000, 5, 3));
		user.setEmail("elly@facebook.com");
		userRepository.save(user);
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userRepository.findUserByEmail("elly@facebook.com");
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
		assertEquals(user.getLastName(), retrievedUser.get().getLastName());
		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.get().getDateOfBirth());
	}

	@Test
	public void returnsNullWhenNoUserFoundWithGivenEmail() {
		Optional<User> retrievedUser = userRepository.findUserByEmail("bruno@facebook.com");
		assertFalse(retrievedUser.isPresent());
	}

	@Test
	public void allUsersCanBeRetrieved() {
		int initialNumberOfUsers = userRepository.findAll().size();

		User user = new User();
		user.setFirstName("Emma");
		user.setLastName("Young");
		user.setDateOfBirth(LocalDate.of(2000, 5, 3));
		user.setEmail("emma@facebook.com");
		userRepository.save(user);
		entityManager.flush();
		entityManager.clear();

		List<User> allUsers = userRepository.findAll();
		assertEquals(initialNumberOfUsers + 1, allUsers.size());
		assertEquals(1, allUsers.stream().filter(u -> u.getEmail().equals("emma@facebook.com")).count());
	}


}
