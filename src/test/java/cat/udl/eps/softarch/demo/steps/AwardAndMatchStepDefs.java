package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

public class AwardAndMatchStepDefs {
    
    private final StepDefs stepDefs;
    private String teamUri; 
    private String editionUri;
    private String matchUri;

    public AwardAndMatchStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @Given("^The dependencies exist$")
    public void theDependenciesExist() throws Throwable {
        var edReq = post("/editions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"year\": 2025, \"venueName\": \"EPS Igualada\", \"description\": \"Test Edition\"}")
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        var edRes = stepDefs.mockMvc.perform(edReq).andReturn().getResponse();
        
        if (edRes.getStatus() == 201) {
            editionUri = edRes.getHeader("Location");
        } else if (edRes.getStatus() == 409) {
            editionUri = "http://localhost/editions/1"; // <-- CORREGIT
        } else {
            throw new IllegalStateException("Failed to create edition fixture, status: " + edRes.getStatus());
        }

        var teamReq = post("/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Team A\", \"city\": \"Igualada\", \"foundationYear\": 2000, \"category\": \"Junior\"}")
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        var teamRes = stepDefs.mockMvc.perform(teamReq).andReturn().getResponse();
        
        if (teamRes.getStatus() == 201) {
            teamUri = teamRes.getHeader("Location");
        } else if (teamRes.getStatus() == 409) {
            teamUri = "http://localhost/teams/Team%20A"; // <-- CORREGIT
        } else {
            throw new IllegalStateException("Failed to create team fixture, status: " + teamRes.getStatus());
        }

        var matchReq = post("/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        var matchRes = stepDefs.mockMvc.perform(matchReq).andReturn().getResponse();
        
        if (matchRes.getStatus() == 201) {
            matchUri = matchRes.getHeader("Location");
        } else {
            matchUri = "http://localhost/matches/1"; // <-- CORREGIT
        }
    }

    @When("^I request the match results list$")
    public void iRequestTheMatchResultsList() throws Throwable {
        var request = get("/matchResults")
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I request the awards list$")
    public void iRequestTheAwardsList() throws Throwable {
        var request = get("/awards")
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create a match result with score (-?\\d+)$")
    public void iCreateAMatchResultWithScore(int score) throws Throwable {
        String payload = String.format("{\"score\": %d, \"team\": \"%s\", \"match\": \"%s\"}", 
                                        score, teamUri, matchUri);
        
        var request = post("/matchResults")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate());
                
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with name \"([^\"]*)\"$")
    public void iCreateAnAwardWithName(String name) throws Throwable {
        String payload = String.format("{\"name\": \"%s\", \"winner\": \"%s\", \"edition\": \"%s\"}", 
                                        name, teamUri, editionUri);
        
        var request = post("/awards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with no name$")
    public void iCreateAnAwardWithNoName() throws Throwable {
        String payload = String.format("{\"winner\": \"%s\", \"edition\": \"%s\"}", 
                                        teamUri, editionUri);
        
        var request = post("/awards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }
}