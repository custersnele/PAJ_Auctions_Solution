package be.pxl.auctions.rest.advice;

import be.pxl.auctions.util.exception.AuctionNotFoundException;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidBidException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value
			= { UserNotFoundException.class, AuctionNotFoundException.class })
	protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(),
				new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { DuplicateEmailException.class, InvalidBidException.class, InvalidDateException.class })
	protected ResponseEntity<Object> handleNotAcceptable(RuntimeException ex, WebRequest request) {
		return handleExceptionInternal(ex, ex.getMessage(),
				new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
	}
}
