package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
	private UserDao userDao;
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
	public void throwsRequiredFieldExceptionWhenFirstNameNotGiven() {
		user.setFirstName("");
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsRequiredFieldExceptionWhenLastNameNotGiven() {
		user.setLastName(null);
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsRequiredFieldExceptionWhenEmailNotGiven() {
		user.setEmail("    ");
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsRequiredFieldExceptionWhenDateOfBirthNotGiven() {
		user.setDateOfBirth(null);
		assertThrows(RequiredFieldException.class, () -> userService.createUser(user));

	}

	@Test
	public void throwsInvalidEmailExceptionWhenInvalidEmailGiven() {
		user.setEmail("test");
		assertThrows(InvalidEmailException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsInvalidDateExceptionWhenDateOfBirthInFuture() {
		user.setDateOfBirth(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		assertThrows(InvalidDateException.class, () -> userService.createUser(user));
	}

	@Test
	public void throwsDuplicateEmailExceptionWhenEmailNotUnique() {
		when(userDao.findUserByEmail(EMAIL)).thenReturn(Optional.of(new User()));
		assertThrows(DuplicateEmailException.class, () -> userService.createUser(user));
	}

	@Test
	public void validUserIsSavedCorrectly() throws InvalidDateException, RequiredFieldException, InvalidEmailException, DuplicateEmailException {
		when(userDao.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
		when(userDao.saveUser(any())).thenAnswer(returnsFirstArg());
		userService.createUser(user);
		verify(userDao).saveUser(userCaptor.capture());
		User userSaved = userCaptor.getValue();
		assertEquals(FIRSTNAME, userSaved.getFirstName());
		assertEquals(LASTNAME, userSaved.getLastName());
		assertEquals(EMAIL, userSaved.getEmail());
		assertEquals(DOB, userSaved.getDateOfBirth());
	}
}
