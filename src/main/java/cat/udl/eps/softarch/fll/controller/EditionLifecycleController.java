package cat.udl.eps.softarch.fll.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import cat.udl.eps.softarch.fll.domain.EditionState;
import cat.udl.eps.softarch.fll.exception.EditionLifecycleException;
import cat.udl.eps.softarch.fll.service.EditionLifecycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Edition Lifecycle", description = "Custom endpoint to transition edition lifecycle state")
public class EditionLifecycleController {

	private final EditionLifecycleService editionLifecycleService;

	public EditionLifecycleController(EditionLifecycleService editionLifecycleService) {
		this.editionLifecycleService = editionLifecycleService;
	}

	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/editions/{editionId}/state")
	@Operation(summary = "Transition edition state")
	public ChangeEditionStateResponse changeState(@PathVariable Long editionId, @RequestBody ChangeEditionStateRequest request) {
		if (request == null || request.state() == null) {
			throw new EditionLifecycleException("INVALID_EDITION_STATE_REQUEST", "State is required");
		}
		EditionLifecycleService.TransitionResult transition = editionLifecycleService.changeState(editionId, request.state());
		return new ChangeEditionStateResponse(
				transition.editionId(),
				transition.previousState(),
				transition.newState(),
				"UPDATED");
	}

	public record ChangeEditionStateRequest(EditionState state) {
	}

	public record ChangeEditionStateResponse(Long editionId, EditionState previousState, EditionState newState, String status) {
	}
}
