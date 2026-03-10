package cat.udl.eps.softarch.fll.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.udl.eps.softarch.fll.controller.dto.EditionVolunteersResponse;
import cat.udl.eps.softarch.fll.service.EditionVolunteerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/editions")
@RequiredArgsConstructor
public class EditionVolunteerController {

	private final EditionVolunteerService editionVolunteerService;

	@GetMapping("/{editionId}/volunteers")
	public EditionVolunteersResponse getVolunteersGroupedByType(@PathVariable Long editionId) {
		return editionVolunteerService.getVolunteersGroupedByType(editionId);
	}
}
