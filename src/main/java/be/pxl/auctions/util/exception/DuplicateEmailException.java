package be.pxl.auctions.util.exception;

public class DuplicateEmailException extends RuntimeException {

	public DuplicateEmailException(String email) {
		super("Email [" + email + "] is already in use.");
	}
}
