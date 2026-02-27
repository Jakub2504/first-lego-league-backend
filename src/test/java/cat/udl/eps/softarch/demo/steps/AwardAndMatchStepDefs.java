package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.json.JSONObject; // Ho utilitzem com fan a RegisterStepDefs
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
        JSONObject editionJson = new JSONObject();
        editionJson.put("year", 2025);
        editionJson.put("venueName", "EPS Igualada");
        editionJson.put("description", "Test Edition");

        var edReq = post("/editions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(editionJson.toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
            
        var edRes = stepDefs.mockMvc.perform(edReq).andReturn().getResponse();
        if (edRes.getStatus() == 201) editionUri = edRes.getHeader("Location");
        else editionUri = "http://localhost/editions/1";

        JSONObject teamJson = new JSONObject();
        teamJson.put("name", "Team A");
        teamJson.put("city", "Igualada");
        teamJson.put("foundationYear", 2000);
        teamJson.put("category", "Junior");

        var teamReq = post("/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content(teamJson.toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
            
        var teamRes = stepDefs.mockMvc.perform(teamReq).andReturn().getResponse();
        if (teamRes.getStatus() == 201) teamUri = teamRes.getHeader("Location");
        else teamUri = "http://localhost/teams/Team%20A";

        var matchReq = post("/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
            
        var matchRes = stepDefs.mockMvc.perform(matchReq).andReturn().getResponse();
        if (matchRes.getStatus() == 201) matchUri = matchRes.getHeader("Location");
        else matchUri = "http://localhost/matches/1";
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
        JSONObject payload = new JSONObject();
        payload.put("score", score);
        payload.put("team", teamUri);
        payload.put("match", matchUri);
        
        var request = post("/matchResults")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload.toString())
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .with(AuthenticationStepDefs.authenticate());
                
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with name \"([^\"]*)\"$")
    public void iCreateAnAwardWithName(String name) throws Throwable {
        JSONObject payload = new JSONObject();
        payload.put("name", name);
        payload.put("winner", teamUri);
        payload.put("edition", editionUri);
        
        var request = post("/awards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload.toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
            
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with no name$")
    public void iCreateAnAwardWithNoName() throws Throwable {
        JSONObject payload = new JSONObject();
        payload.put("winner", teamUri);
        payload.put("edition", editionUri);
        
        var request = post("/awards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload.toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .accept(MediaType.APPLICATION_JSON)
            .with(AuthenticationStepDefs.authenticate());
            
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }
}