package be.pxl.auctions.util.exception;

public class RequiredFieldException extends RuntimeException {

	public RequiredFieldException(String field) {
		super("[" + field + "] is required.");
	}
}
