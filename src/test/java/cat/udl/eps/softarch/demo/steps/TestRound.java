package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.*;
import cat.udl.eps.softarch.demo.domain.Match;
import cat.udl.eps.softarch.demo.domain.Round;
import cat.udl.eps.softarch.demo.repository.RoundRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRound {

	@Autowired
	private RoundRepository roundRepository;

	private Long currentRoundId;
	private Round round;

	@Given("a new Round with number {int} is saved")
	public void a_new_round_is_saved(Integer number) {
		Round r = new Round();
		r.setNumber(number);
		round = roundRepository.save(r); // Persistencia real
		currentRoundId = round.getId();
	}

	@When("I add {int} matches via repository")
	public void i_add_matches_repo(Integer count) {
		Round r = roundRepository.findById(currentRoundId).orElseThrow();
		for (int i = 0; i < count; i++) {
			r.addMatch(new Match());
		}
		roundRepository.save(r); // Esto dispara el CascadeType.ALL
	}

	@Then("the database should show {int} matches for this round")
	public void check_db_matches(Integer count) {
		Round r = roundRepository.findById(currentRoundId).orElseThrow();
		assertEquals(count, r.getMatches().size());
	}

	@When("I remove a match from the saved round")
	public void i_remove_match_saved() {
		Round r = roundRepository.findById(currentRoundId).orElseThrow();
		Match m = r.getMatches().get(0);
		r.removeMatch(m);
		roundRepository.save(r);
	}

	@Then("the match should be deleted from the database")
	public void check_orphan_removal() {
		Round r = roundRepository.findById(currentRoundId).orElseThrow();
		assertEquals(0, r.getMatches().size());
	}
}
