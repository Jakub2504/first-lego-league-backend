package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;

import cat.udl.eps.softarch.demo.domain.ScientificProject;
import io.cucumber.java.en.When;

public class ManageScientificProjectStepDefs {

	private final StepDefs stepDefs;

	public ManageScientificProjectStepDefs(StepDefs stepDefs) {
		this.stepDefs = stepDefs;
	}

	@When("I create a new scientific project with score {int} and comments {string}")
	public void iCreateScientificProject(Integer score, String comments) throws Exception {
		ScientificProject project = new ScientificProject();
		project.setScore(score);
		project.setComments(comments);

		stepDefs.result = stepDefs.mockMvc.perform(
				post("/scientificProjects")
						.contentType(MediaType.APPLICATION_JSON)
						.content(stepDefs.mapper.writeValueAsString(project))
						.characterEncoding(StandardCharsets.UTF_8)
						.accept(MediaType.APPLICATION_JSON)
						.with(AuthenticationStepDefs.authenticate()))
				.andDo(print());
	}
}


