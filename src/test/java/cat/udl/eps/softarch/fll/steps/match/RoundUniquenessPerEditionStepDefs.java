package cat.udl.eps.softarch.fll.steps.match;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import cat.udl.eps.softarch.fll.domain.edition.Edition;
import cat.udl.eps.softarch.fll.domain.edition.Venue;
import cat.udl.eps.softarch.fll.domain.match.Round;
import cat.udl.eps.softarch.fll.repository.edition.EditionRepository;
import cat.udl.eps.softarch.fll.repository.edition.VenueRepository;
import cat.udl.eps.softarch.fll.repository.match.RoundRepository;
import cat.udl.eps.softarch.fll.steps.app.AuthenticationStepDefs;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class RoundUniquenessPerEditionStepDefs {

	private final StepDefs stepDefs;
	private final EditionRepository editionRepository;
	private final VenueRepository venueRepository;
	private final RoundRepository roundRepository;

	private final Map<String, Long> editionIdByAlias = new HashMap<>();

	public RoundUniquenessPerEditionStepDefs(
		StepDefs stepDefs,
		EditionRepository editionRepository,
		VenueRepository venueRepository,
		RoundRepository roundRepository
	) {
		this.stepDefs = stepDefs;
		this.editionRepository = editionRepository;
		this.venueRepository = venueRepository;
		this.roundRepository = roundRepository;
	}

	@Given("An edition {string} exists with year {int} and venue {string} and description {string}")
	public void anEditionExistsWithYearAndVenueAndDescription(String alias, int year, String venue, String description) {
		Venue venueEntity = venueRepository.save(Venue.create(venue, "Test City"));
		Edition edition = Edition.create(year, venueEntity, description);
		Long editionId = editionRepository.save(edition).getId();
		editionIdByAlias.put(alias, editionId);
	}

	@Given("A round with number {int} exists for edition {string}")
	public void aRoundWithNumberExistsForEdition(int number, String editionAlias) {
		Long editionId = editionIdByAlias.get(editionAlias);
		if (editionId == null) {
			throw new IllegalStateException("Edition not found for alias: " + editionAlias);
		}
		Edition edition = editionRepository.findById(editionId)
			.orElseThrow(() -> new IllegalStateException("Edition not found: " + editionId));

		Round round = new Round();
		round.setNumber(number);
		round.setEdition(edition);
		roundRepository.save(round);
	}

	@When("I search rounds by the edition id for edition {string}")
	public void iSearchRoundsByTheEditionIdForEdition(String editionAlias) throws Exception {
		Long editionId = editionIdByAlias.get(editionAlias);
		if (editionId == null) {
			throw new IllegalStateException("Edition not found for alias: " + editionAlias);
		}

		stepDefs.result = stepDefs.mockMvc.perform(
			get("/rounds/search/findByEditionId")
				.param("editionId", editionId.toString())
				.accept(MediaType.APPLICATION_JSON)
				.with(AuthenticationStepDefs.authenticate()))
			.andDo(print());
	}
}
