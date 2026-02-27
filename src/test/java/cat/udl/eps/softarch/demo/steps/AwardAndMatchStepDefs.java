package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.json.JSONObject;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

import cat.udl.eps.softarch.demo.domain.Match;
import cat.udl.eps.softarch.demo.repository.MatchRepository;

public class AwardAndMatchStepDefs {
    
    private final StepDefs stepDefs;
    private final MatchRepository matchRepository;
    private String teamUri; 
    private String editionUri;
    private String matchUri;

    public AwardAndMatchStepDefs(StepDefs stepDefs, MatchRepository matchRepository) {
        this.stepDefs = stepDefs;
        this.matchRepository = matchRepository;
    }

    @Given("^The dependencies exist$")
    public void theDependenciesExist() throws Throwable {
        JSONObject editionJson = new JSONObject();
        editionJson.put("year", 2025);
        editionJson.put("venueName", "EPS Igualada");
        editionJson.put("description", "Test Edition");

        var edRes = stepDefs.mockMvc.perform(post("/editions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(editionJson.toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .with(AuthenticationStepDefs.authenticate())).andReturn().getResponse();
            
        if (edRes.getStatus() == 201) editionUri = edRes.getHeader("Location");
        else if (edRes.getStatus() == 409) editionUri = "http://localhost/editions/1";
        else throw new RuntimeException("ERROR CREANT EDITION: " + edRes.getContentAsString());

        JSONObject teamJson = new JSONObject();
        teamJson.put("name", "TeamA");
        teamJson.put("city", "Igualada");
        teamJson.put("foundationYear", 2000);
        teamJson.put("category", "Junior");

        var teamRes = stepDefs.mockMvc.perform(post("/teams")
            .contentType(MediaType.APPLICATION_JSON)
            .content(teamJson.toString())
            .characterEncoding(StandardCharsets.UTF_8)
            .with(AuthenticationStepDefs.authenticate())).andReturn().getResponse();
            
        if (teamRes.getStatus() == 201) teamUri = teamRes.getHeader("Location");
        else if (teamRes.getStatus() == 409) teamUri = "http://localhost/teams/TeamA";
        else throw new RuntimeException("ERROR CREANT TEAM: " + teamRes.getContentAsString());

        Match match = new Match();
        match = matchRepository.save(match);
        matchUri = "http://localhost/matches/" + match.getId();
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
        
        if (stepDefs.result.andReturn().getResponse().getStatus() == 400) {
            String errorMsg = stepDefs.result.andReturn().getResponse().getContentAsString();
            throw new RuntimeException("EL SERVIDOR DÓNA 400 EN CREAR MATCHRESULT. MOTIU: " + errorMsg);
        }
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