package cat.udl.eps.softarch.fll.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.fll.domain.volunteer.Referee;
import cat.udl.eps.softarch.fll.repository.match.MatchRepository;
import lombok.RequiredArgsConstructor;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class RefereeEventHandler {

	private final MatchRepository matchRepository;

	@HandleBeforeDelete
	@Transactional
	public void handleRefereeBeforeDelete(Referee referee) {
		matchRepository.clearRefereeById(referee.getId());
	}
}
