package cat.udl.eps.softarch.demo.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import io.cucumber.java.en.When;

public class AwardAndMatchStepDefs {
    
    private final StepDefs stepDefs;

    public AwardAndMatchStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("^I request the match results list$")
    public void iRequestTheMatchResultsList() throws Throwable {
        var request = get("/matchResults");
        request.accept(MediaType.APPLICATION_JSON);
        request.with(AuthenticationStepDefs.authenticate());
        
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I request the awards list$")
    public void iRequestTheAwardsList() throws Throwable {
        var request = get("/awards");
        request.accept(MediaType.APPLICATION_JSON);
        request.with(AuthenticationStepDefs.authenticate());
        
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create a match result with score (-?\\d+)$")
    public void iCreateAMatchResultWithScore(int score) throws Throwable {
        // AQUÍ ESTÀ EL CANVI: Hem tret el camp "match" perquè l'hem comentat a l'entitat
        String payload = "{\"score\": " + score + ", \"team\": \"/teams/1\"}";
        var request = post("/matchResults");
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload);
        request.characterEncoding(StandardCharsets.UTF_8);
        request.accept(MediaType.APPLICATION_JSON);
        request.with(AuthenticationStepDefs.authenticate());
        
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with name \"([^\"]*)\"$")
    public void iCreateAnAwardWithName(String name) throws Throwable {
        String payload = "{\"name\": \"" + name + "\", \"edition\": \"/editions/1\", \"winner\": \"/teams/1\"}";
        var request = post("/awards");
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload);
        request.characterEncoding(StandardCharsets.UTF_8);
        request.accept(MediaType.APPLICATION_JSON);
        request.with(AuthenticationStepDefs.authenticate());
        
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }

    @When("^I create an award with no name$")
    public void iCreateAnAwardWithNoName() throws Throwable {
        String payload = "{\"edition\": \"/editions/1\", \"winner\": \"/teams/1\"}";
        var request = post("/awards");
        request.contentType(MediaType.APPLICATION_JSON);
        request.content(payload);
        request.characterEncoding(StandardCharsets.UTF_8);
        request.accept(MediaType.APPLICATION_JSON);
        request.with(AuthenticationStepDefs.authenticate());
        
        stepDefs.result = stepDefs.mockMvc.perform(request);
    }
}