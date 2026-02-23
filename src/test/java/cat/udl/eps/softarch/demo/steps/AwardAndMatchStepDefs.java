package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

public class AwardAndMatchStepDefs {
    
    private final StepDefs stepDefs;
    private String teamUri = "/teams/1";
    private String editionUri = "/editions/1";

    public AwardAndMatchStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @Given("^The dependencies exist$")
    public void theDependenciesExist() throws Throwable {
        
    }

    @When("^I request the match results list$")
    public void iRequestTheMatchResultsList() throws Throwable {
        var request = get("/matchResults").accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I request the awards list$")
    public void iRequestTheAwardsList() throws Throwable {
        var request = get("/awards").accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create a match result with score (-?\\d+)$")
    public void iCreateAMatchResultWithScore(int score) throws Throwable {
        String payload = "{\"score\": " + score + ", \"team\": \"" + teamUri + "\"}";
        var request = post("/matchResults").contentType(MediaType.APPLICATION_JSON).content(payload)
                .characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

	@When("^I create an award with name \"([^\"]*)\"$")
    public void iCreateAnAwardWithName(String name) throws Throwable {
        String payload = "{\"name\": \"" + name + "\", \"winner\": \"" + teamUri + "\"}";
        var request = post("/awards").contentType(MediaType.APPLICATION_JSON).content(payload)
                .characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

	@When("^I create an award with no name$")
    public void iCreateAnAwardWithNoName() throws Throwable {
        String payload = "{\"winner\": \"" + teamUri + "\"}";
        var request = post("/awards").contentType(MediaType.APPLICATION_JSON).content(payload)
                .characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }
}