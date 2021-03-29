package be.pxl.auctions.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UserGetAgeTest {

	private static final int YEARS = 15;

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayToday() {
		User user = new User();
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS));

		assertEquals(YEARS, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
		User user = new User();
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).plusDays(1));

		assertEquals(YEARS - 1, user.getAge());
	}

	@Test
	public void returnsCorrectAgeWhenBirthdayWasYesterday() {
		User user = new User();
		user.setDateOfBirth(LocalDate.now().minusYears(YEARS).minusDays(1));

		assertEquals(YEARS, user.getAge());
	}

}
