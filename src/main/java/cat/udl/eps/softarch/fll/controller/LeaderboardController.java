package cat.udl.eps.softarch.fll.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cat.udl.eps.softarch.fll.controller.dto.LeaderboardPageResponse;
import cat.udl.eps.softarch.fll.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/leaderboards")
@Tag(name = "Leaderboards", description = "Endpoints for retrieving competition leaderboards")
@Validated
public class LeaderboardController {

	private final LeaderboardService leaderboardService;

	public LeaderboardController(LeaderboardService leaderboardService) {
		this.leaderboardService = leaderboardService;
	}

	@GetMapping("/editions/{editionId}")
	@Operation(summary = "Get leaderboard for a specific edition",
			description = "Sorting is fixed to totalScore DESC, matchesPlayed DESC, teamName ASC.")
	public LeaderboardPageResponse getEditionLeaderboard(
			@Parameter(description = "Edition identifier") @PathVariable Long editionId,
			@Parameter(description = "Page index (0-based)") @RequestParam(defaultValue = "0") @Min(0) int page,
			@Parameter(description = "Page size") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
			@Parameter(description = "Optional query parameter for compatibility. Ignored because sorting is fixed.")
			@RequestParam(required = false) String sort) {
		return leaderboardService.getEditionLeaderboard(editionId, page, size);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, String> errors = ex.getConstraintViolations().stream()
				.collect(Collectors.toMap(
						violation -> {
							String propertyPath = violation.getPropertyPath().toString();
							int lastDot = propertyPath.lastIndexOf('.');
							return lastDot >= 0 ? propertyPath.substring(lastDot + 1) : propertyPath;
						},
						violation -> violation.getMessage(),
						(first, second) -> first,
						LinkedHashMap::new));

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("message", "Validation failed");
		body.put("errors", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
}
