package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.*;
import cat.udl.eps.softarch.demo.domain.Match;
import cat.udl.eps.softarch.demo.domain.Round;
import cat.udl.eps.softarch.demo.domain.CompetitionTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalTime;
import java.time.Duration;


public class TestMatch {

	private Round round;
	private CompetitionTable table;
	private Match match;

	@Given("a Round exists")
	public void a_round_exists() {
		round = new Round(); // Assuming a default constructor exists
	}

	@Given("a Competition Table exists")
	public void a_competition_table_exists() {
		table = new CompetitionTable(); // Assuming a default constructor exists
	}

	@When("I create a new match starting at {string} and ending at {string}")
	public void i_create_a_new_match(String start, String end) {
		match = new Match();
		match.setStartTime(LocalTime.parse(start));
		match.setEndTime(LocalTime.parse(end));
		match.setRound(round);
		match.setCompetitionTable(table);
	}

	@Then("the match should be linked to the round and the table")
	public void the_match_should_be_linked() {
		assertNotNull(match.getRound());
		assertNotNull(match.getCompetitionTable());
		assertEquals(round, match.getRound());
		assertEquals(table, match.getCompetitionTable());
	}

	@Then("the match duration should be {string} minutes")
	public void the_match_duration_should_be(String minutes) {
		long duration = Duration.between(match.getStartTime(), match.getEndTime()).toMinutes();
		assertEquals(Long.parseLong(minutes), duration);
	}
}