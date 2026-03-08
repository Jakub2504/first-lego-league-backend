package cat.udl.eps.softarch.fll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cat.udl.eps.softarch.fll.domain.Edition;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import cat.udl.eps.softarch.fll.domain.Team;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DomainValidationExceptionHandler {

	@ExceptionHandler(DomainValidationException.class)
	public ResponseEntity<DomainValidationErrorResponse> handleDomainValidationException(DomainValidationException ex) {
		DomainValidationErrorResponse response = new DomainValidationErrorResponse(ex.getCode(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<DomainValidationErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
		if (isInvalidScientificProjectTeamReference(exception)) {
			DomainValidationErrorResponse response = new DomainValidationErrorResponse(
					"TEAM_NOT_FOUND", "The referenced team does not exist");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (isInvalidScientificProjectEditionReference(exception)) {
			DomainValidationErrorResponse response = new DomainValidationErrorResponse(
					"EDITION_NOT_FOUND", "The referenced edition does not exist");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		throw exception;
	}

	private boolean isInvalidScientificProjectTeamReference(HttpMessageNotReadableException exception) {
		Throwable current = exception;
		while (current != null) {
			if (current instanceof InvalidFormatException invalidFormatException) {
				if (invalidFormatException.getTargetType() != null
						&& Team.class.isAssignableFrom(invalidFormatException.getTargetType())) {
					return true;
				}
				boolean teamPathFound = invalidFormatException.getPath().stream()
						.anyMatch(ref -> "team".equals(ref.getFieldName()));
				if (teamPathFound) {
					return true;
				}
			}
			String message = current.getMessage();
			if (message != null && message.contains("ScientificProject") && message.contains("team")) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}

	private boolean isInvalidScientificProjectEditionReference(HttpMessageNotReadableException exception) {
		Throwable current = exception;
		while (current != null) {
			if (current instanceof InvalidFormatException invalidFormatException) {
				if (invalidFormatException.getTargetType() != null
						&& Edition.class.isAssignableFrom(invalidFormatException.getTargetType())) {
					return true;
				}
				boolean editionPathFound = invalidFormatException.getPath().stream()
						.anyMatch(ref -> "edition".equals(ref.getFieldName()));
				if (editionPathFound) {
					return true;
				}
			}
			String message = current.getMessage();
			if (message != null && message.contains("ScientificProject") && message.contains("edition")) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}
}
