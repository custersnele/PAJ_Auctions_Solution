package be.pxl.auctions.service;

import be.pxl.auctions.model.User;
import be.pxl.auctions.repository.UserRepository;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceCreateUserTest {

	private static final String EMAIL = "mark@facebook.com";
	private static final String FIRSTNAME = "mark";
	private static final String LASTNAME = "Zuckerberg";
	private static final LocalDate DOB = LocalDate.of(1983, 5,3);

	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserService userService;
	private UserCreateResource user;
	@Captor
	private ArgumentCaptor<User> userCaptor;

	@BeforeEach
	public void init() {
		user = new UserCreateResource();
		user.setFirstName(FIRSTNAME);
		user.setLastName(LASTNAME);
		user.setDateOfBirth("03/05/1983");
		user.setEmail(EMAIL);
	}


	@Test
	public void throwsInvalidDateExceptionWhenDateOfBirthInFuture() {
		user.setDateOfBirth(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		assertThrows(InvalidDateException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsDuplicateEmailExceptionWhenEmailNotUnique() {
		when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(new User()));
		assertThrows(DuplicateEmailException.class, () -> userService.createUser(user));
	}

	@Test
	public void validUserIsSavedCorrectly() throws InvalidDateException, DuplicateEmailException {
		when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenAnswer(returnsFirstArg());
		userService.createUser(user);
		verify(userRepository).save(userCaptor.capture());
		User userSaved = userCaptor.getValue();
		assertEquals(FIRSTNAME, userSaved.getFirstName());
		assertEquals(LASTNAME, userSaved.getLastName());
		assertEquals(EMAIL, userSaved.getEmail());
		assertEquals(DOB, userSaved.getDateOfBirth());
	}
}
