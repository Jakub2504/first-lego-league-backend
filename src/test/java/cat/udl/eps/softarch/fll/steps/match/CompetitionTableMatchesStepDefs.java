package cat.udl.eps.softarch.fll.steps.match;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.fll.domain.match.CompetitionTable;
import cat.udl.eps.softarch.fll.domain.match.Match;
import cat.udl.eps.softarch.fll.repository.match.CompetitionTableRepository;
import cat.udl.eps.softarch.fll.repository.match.MatchRepository;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;

public class CompetitionTableMatchesStepDefs {

	private final StepDefs stepDefs;
	private final CompetitionTableRepository competitionTableRepository;
	private final MatchRepository matchRepository;

	private String targetTableId;

	public CompetitionTableMatchesStepDefs(StepDefs stepDefs,
		CompetitionTableRepository competitionTableRepository,
		MatchRepository matchRepository) {
			this.stepDefs = stepDefs;
			this.competitionTableRepository = competitionTableRepository;
			this.matchRepository = matchRepository;
	}

	@Given("a competition table with 2 matches exists")
	public void aCompetitionTableWith2MatchesExists() {
		matchRepository.deleteAll();
		competitionTableRepository.deleteAll();

		CompetitionTable table = new CompetitionTable();
		table.setId("TABLE-TEST-1");
		competitionTableRepository.save(table);
		targetTableId = table.getId();

		createMatch(table);
		createMatch(table);
	}

	@Given("a competition table without matches exists")
	public void aCompetitionTableWithoutMatchesExists() {
		matchRepository.deleteAll();
		competitionTableRepository.deleteAll();

		CompetitionTable table = new CompetitionTable();
		table.setId("TABLE-EMPTY-1");
		competitionTableRepository.save(table);
		targetTableId = table.getId();
	}

	@When("I request matches for the target competition table")
	public void iRequestMatchesForTheTargetCompetitionTable() throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/competitionTables/{id}/matches", targetTableId)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(print());
	}

	@When("I request matches for competition table {string}")
	public void iRequestMatchesForCompetitionTable(String tableId) throws Exception {
		stepDefs.result = stepDefs.mockMvc.perform(
				get("/competitionTables/{id}/matches", tableId)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(print());
	}

	@And("the competition table matches response contains {int} matches")
	public void theCompetitionTableMatchesResponseContains(int count) throws Exception {
		stepDefs.result.andExpect(jsonPath("$._embedded.matches.length()").value(count));
	}

	@And("the competition table matches error is {string}")
	public void theCompetitionTableMatchesErrorIs(String error) throws Exception {
		stepDefs.result.andExpect(jsonPath("$.error").value(error));
	}

	private void createMatch(CompetitionTable table) {
		Match match = new Match();
		match.setCompetitionTable(table);
		matchRepository.save(match);
	}
}