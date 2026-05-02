package cat.udl.eps.softarch.fll.steps.match;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.fll.steps.app.AuthenticationStepDefs;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import cat.udl.eps.softarch.fll.domain.edition.Edition;
import cat.udl.eps.softarch.fll.domain.edition.Venue;
import cat.udl.eps.softarch.fll.domain.match.Round;
import cat.udl.eps.softarch.fll.repository.edition.EditionRepository;
import cat.udl.eps.softarch.fll.repository.edition.VenueRepository;
import cat.udl.eps.softarch.fll.repository.match.RoundRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class RoundSearchByEditionStepDefs {

	private final StepDefs stepDefs;
	private final EditionRepository editionRepository;
	private final VenueRepository venueRepository;
	private final RoundRepository roundRepository;

	private Long currentEditionId;

	public RoundSearchByEditionStepDefs(StepDefs stepDefs, EditionRepository editionRepository,
			VenueRepository venueRepository, RoundRepository roundRepository) {
		this.stepDefs = stepDefs;
		this.editionRepository = editionRepository;
		this.venueRepository = venueRepository;
		this.roundRepository = roundRepository;
	}

	@Given("An edition exists with year {int} and venue {string} and description {string}")
	public void anEditionExistsWithYearAndVenueAndDescription(int year, String venue, String description) {
		Venue venueEntity = venueRepository.save(Venue.create(venue, "Test City"));
		Edition edition = Edition.create(year, venueEntity, description);
		currentEditionId = editionRepository.save(edition).getId();
	}

	@Given("A round with number {int} exists for that edition")
	public void aRoundWithNumberExistsForThatEdition(int number) {
		Edition edition = editionRepository.findById(currentEditionId)
				.orElseThrow(() -> new IllegalStateException("Edition not found: " + currentEditionId));
		Round round = new Round();
		round.setNumber(number);
		round.setEdition(edition);
		roundRepository.save(round);
	}

	@When("I search rounds by the edition id")
	public void iSearchRoundsByTheEditionId() throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/rounds/search/findByEditionId")
						.param("editionId", currentEditionId.toString())
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print());
	}

	@And("^The round search response should contain (\\d+) rounds?$")
	public void theRoundSearchResponseShouldContainCount(int expectedCount) throws Exception {
		String responseBody = stepDefs.result.andReturn().getResponse().getContentAsString();
			JsonNode root = stepDefs.mapper.readTree(responseBody);
			JsonNode rounds = root.path("_embedded").path("rounds");
			int actualCount = rounds.isArray() ? rounds.size() : 0;
			assertEquals(expectedCount, actualCount);
	}

	@And("The round search response should include round with number {int}")
	public void theRoundSearchResponseShouldIncludeRoundWithNumber(int number) throws Exception {
		stepDefs.result.andExpect(jsonPath("$._embedded.rounds[*].number", hasItem(number)));
	}
}


