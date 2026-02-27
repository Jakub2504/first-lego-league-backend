package cat.udl.eps.softarch.demo.steps;

import static org.junit.jupiter.api.Assertions.*;
import cat.udl.eps.softarch.demo.domain.CompetitionTable;
import cat.udl.eps.softarch.demo.domain.Referee;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestReferee {

	private Referee referee;
	private CompetitionTable table;

	@Given("a new referee")
	public void a_new_referee() {
		// Usamos el constructor Referee() que está en tu clase
		this.referee = new Referee();
	}

	@Given("a competition table exists with ID {string}")
	public void a_competition_table_exists(String id) {
		this.table = new CompetitionTable();
		this.table.setId(id);
	}

	@When("I set the referee as an expert")
	public void i_set_the_referee_as_an_expert() {
		// Probamos setExpert(boolean expert)
		this.referee.setExpert(true);
	}

	@When("I assign the referee to supervise {string}")
	public void i_assign_the_referee_to_supervise(String id) {
		// Probamos setSupervisesTable(CompetitionTable table)
		// Usamos addReferee de la mesa para mantener la bidireccionalidad
		this.table.addReferee(this.referee);
	}

	@Then("the referee should be recognized as an expert")
	public void the_referee_should_be_recognized_as_an_expert() {
		// Probamos isExpert()
		assertTrue(this.referee.isExpert());
	}

	@Then("the referee should reference {string} as their assigned table")
	public void the_referee_should_reference_table(String id) {
		// Probamos getSupervisesTable()
		assertNotNull(this.referee.getSupervisesTable());
		assertEquals(id, this.referee.getSupervisesTable().getId());
	}

	@Then("the table {string} should list the referee in its staff")
	public void the_table_should_list_referee(String id) {
		// Verificamos que la relación inversa también funciona
		assertTrue(this.table.getReferees().contains(this.referee));
	}
}
