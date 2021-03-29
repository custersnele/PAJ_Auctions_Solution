package be.pxl.auctions.util.exception;

public class InvalidEmailException extends RuntimeException {

	public InvalidEmailException(String email) {
		super("[" + email + "] is not a valid email.");
	}
}
