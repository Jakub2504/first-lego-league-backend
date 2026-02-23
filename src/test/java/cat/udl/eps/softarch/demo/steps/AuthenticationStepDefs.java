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
        var edReq = post("/editions").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"2024\"}").with(AuthenticationStepDefs.authenticate());
        var edRes = stepDefs.mockMvc.perform(edReq).andReturn().getResponse();
        if (edRes.getStatus() == 201) editionUri = edRes.getHeader("Location");

        var teamReq = post("/teams").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Test Team\"}").with(AuthenticationStepDefs.authenticate());
        var teamRes = stepDefs.mockMvc.perform(teamReq).andReturn().getResponse();
        if (teamRes.getStatus() == 201) teamUri = teamRes.getHeader("Location");
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
        String payload = "{\"name\": \"" + name + "\", \"edition\": \"" + editionUri + "\", \"winner\": \"" + teamUri + "\"}";
        var request = post("/awards").contentType(MediaType.APPLICATION_JSON).content(payload)
                .characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with no name$")
    public void iCreateAnAwardWithNoName() throws Throwable {
        String payload = "{\"edition\": \"" + editionUri + "\", \"winner\": \"" + teamUri + "\"}";
        var request = post("/awards").contentType(MediaType.APPLICATION_JSON).content(payload)
                .characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON).with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }
}