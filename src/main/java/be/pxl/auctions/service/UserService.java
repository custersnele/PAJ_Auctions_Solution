package be.pxl.auctions.service;

import be.pxl.auctions.model.User;
import be.pxl.auctions.repository.UserRepository;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	private final UserRepository userRepository;

	public UserService(UserRepository userDao) {
		this.userRepository = userDao;
	}

	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(this::mapToUserDTO).collect(Collectors.toList());
	}

	public UserDTO getUserById(long userId) {
		return userRepository.findById(userId).map(this::mapToUserDTO).orElseThrow(()  -> new UserNotFoundException("Unable to find User with id [" + userId + "]"));
	}

	public UserDTO createUser(UserCreateResource userInfo) throws DuplicateEmailException, InvalidDateException {
		Optional<User> existingUser = userRepository.findUserByEmail(userInfo.getEmail());
		if (existingUser.isPresent()) {
			throw new DuplicateEmailException(userInfo.getEmail());
		}
		User user = mapToUser(userInfo);
		if (user.getDateOfBirth().isAfter(LocalDate.now())) {
			throw new InvalidDateException("DateOfBirth cannot be in the future.");
		}
		return mapToUserDTO(userRepository.save(user));
	}

	private UserDTO mapToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setDateOfBirth(user.getDateOfBirth());
		userDTO.setAge(user.getAge());
		userDTO.setEmail(user.getEmail());
		return userDTO;
	}

	private User mapToUser(UserCreateResource userCreateResource) throws InvalidDateException {
		User user = new User();
		user.setFirstName(userCreateResource.getFirstName());
		user.setLastName(userCreateResource.getLastName());
		try {
			user.setDateOfBirth(LocalDate.parse(userCreateResource.getDateOfBirth(), DATE_FORMAT));
		} catch (DateTimeParseException e) {
			throw new InvalidDateException("[" + user.getDateOfBirth() + "] is not a valid date. Excepted format: dd/mm/yyyy");
		}
		user.setEmail(userCreateResource.getEmail());
		return user;
	}

}
