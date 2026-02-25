package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.*;
import cat.udl.eps.softarch.demo.domain.Match;
import cat.udl.eps.softarch.demo.domain.Round;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestRound {

	private Round round;
	private Match lastRemovedMatch;

	@Given("a new Round with number {int}")
	public void a_new_round_with_number(Integer number) {
		round = new Round();
		round.setNumber(number);
	}

	@When("I add {int} new matches to this round")
	public void i_add_new_matches(Integer count) {
		for (int i = 0; i < count; i++) {
			round.addMatch(new Match());
		}
	}

	@Then("the round should contain {int} matches")
	public void the_round_should_contain_matches(Integer count) {
		assertEquals(count, round.getMatches().size());
	}

	@Then("each match should reference the round with number {int}")
	public void each_match_should_reference_the_round(Integer number) {
		for (Match m : round.getMatches()) {
			assertNotNull(m.getRound());
			assertEquals(number, m.getRound().getNumber());
		}
	}

	@Given("the round has {int} matches")
	public void the_round_has_matches(Integer count) {
		for (int i = 0; i < count; i++) {
			round.addMatch(new Match());
		}
	}

	@When("I remove one match from the round")
	public void i_remove_one_match() {
		lastRemovedMatch = round.getMatches().get(0);
		round.removeMatch(lastRemovedMatch);
	}

	@Then("the round should contain {int} match")
	public void the_round_should_contain_single_match(Integer count) {
		assertEquals(count, round.getMatches().size());
	}

	@Then("the removed match should no longer reference the round")
	public void the_removed_match_no_longer_references() {
		assertNull(lastRemovedMatch.getRound());
	}
}
