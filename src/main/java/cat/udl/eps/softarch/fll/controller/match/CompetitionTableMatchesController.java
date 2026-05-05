package cat.udl.eps.softarch.fll.controller.match;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.udl.eps.softarch.fll.controller.match.dto.CompetitionTableMatchResponse;
import cat.udl.eps.softarch.fll.repository.match.CompetitionTableRepository;
import cat.udl.eps.softarch.fll.repository.match.MatchRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/competitionTables")
@RequiredArgsConstructor
public class CompetitionTableMatchesController {

	private final CompetitionTableRepository competitionTableRepository;
	private final MatchRepository matchRepository;

	@GetMapping("/{id}/matches")
	public ResponseEntity<Object> getMatchesByTable(@PathVariable String id) {
		if (!competitionTableRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("error", "COMPETITION_TABLE_NOT_FOUND"));
		}

		List<CompetitionTableMatchResponse> matches = matchRepository.findByCompetitionTableId(id)
			.stream()
			.map(m -> new CompetitionTableMatchResponse(
				m.getId(), m.getStartTime(), m.getEndTime(), m.getState()))
			.toList();

		return ResponseEntity.ok(Map.of("_embedded", Map.of("matches", matches)));
	}
}