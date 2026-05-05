package cat.udl.eps.softarch.fll.controller.match.dto;

import cat.udl.eps.softarch.fll.domain.match.MatchState;
import java.time.LocalDateTime;

public record CompetitionTableMatchResponse(
	Long id,
	LocalDateTime startTime,
	LocalDateTime endTime,
	MatchState state
) {}