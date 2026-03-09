package cat.udl.eps.softarch.fll.handler;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.fll.exception.EditionLifecycleException;

@RestControllerAdvice
public class EditionLifecycleExceptionHandler {

	@ExceptionHandler(EditionLifecycleException.class)
	public ResponseEntity<Map<String, String>> handleEditionLifecycleException(EditionLifecycleException exception) {
		HttpStatus status = resolveStatus(exception.getError());
		return ResponseEntity.status(status).body(Map.of(
				"error", exception.getError(),
				"message", exception.getMessage()));
	}

	private HttpStatus resolveStatus(String error) {
		return switch (error) {
			case "EDITION_NOT_FOUND" -> HttpStatus.NOT_FOUND;
			case "INVALID_EDITION_STATE_TRANSITION" -> HttpStatus.CONFLICT;
			case "EDITION_OPERATION_NOT_ALLOWED" -> HttpStatus.UNPROCESSABLE_ENTITY;
			default -> HttpStatus.BAD_REQUEST;
		};
	}
}
