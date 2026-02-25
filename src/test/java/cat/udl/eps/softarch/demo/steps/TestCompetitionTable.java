package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.*;
import cat.udl.eps.softarch.demo.domain.CompetitionTable;
import cat.udl.eps.softarch.demo.domain.Referee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestCompetitionTable {

	private CompetitionTable table;
	private Referee referee;
	private Exception exceptionCaptured;

	@Given("a new Competition Table with id {string}")
	public void a_new_competition_table_with_id(String id) {
		table = new CompetitionTable();
		table.setId(id);
	}

	@When("I add a referee named {string} to the table")
	public void i_add_a_referee_named(String name) {
		referee = new Referee();
		// referee.setName(name); // Descomentar si Volunteer tiene setName
		table.addReferee(referee);
	}

	@Then("the table should have {int} referee")
	public void the_table_should_have_referee(Integer count) {
		assertEquals(count, table.getReferees().size());
	}

	@Then("the referee {string} should be supervising {string}")
	public void the_referee_should_be_supervising(String refName, String tableId) {
		assertNotNull(referee.getSupervisesTable());
		assertEquals(tableId, referee.getSupervisesTable().getId());
	}

	@Given("the table already has {int} referees")
	public void the_table_already_has_referees(Integer count) {
		for (int i = 0; i < count; i++) {
			table.addReferee(new Referee());
		}
	}

	@Then("the validation should prevent adding a 4th referee")
	public void the_validation_should_prevent_adding_a_4th_referee() {
		exceptionCaptured = assertThrows(IllegalStateException.class, () -> {
			table.addReferee(new Referee());
		});

		assertNotNull(exceptionCaptured);
		assertEquals("A table can have a maximum of 3 referees", exceptionCaptured.getMessage());
		assertEquals(3, table.getReferees().size());
	}
}
