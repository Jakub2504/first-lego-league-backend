package cat.udl.eps.softarch.fll.steps.coach;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import cat.udl.eps.softarch.fll.steps.app.StepDefs;
import org.springframework.http.MediaType;
import io.cucumber.java.en.*;

public class CoachStepDefs {
    private final StepDefs stepDefs;
    private String lastCoachUri;
	

    public CoachStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("^I create a new coach with name \"([^\"]*)\", email \"([^\"]*)\" and phone \"([^\"]*)\"$")
    public void iCreateANewCoach(String name, String email, String phone) throws Throwable {
        Map<String, String> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("emailAddress", email);
        payload.put("phoneNumber", phone);

        stepDefs.result = stepDefs.mockMvc.perform(
                post("/coaches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(payload))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN"))) 
                .andDo(print());
        
        lastCoachUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
    }

    @And("^It has been created a coach with name \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void itHasBeenCreatedACoach(String name, String email) throws Throwable {
        if (lastCoachUri == null) {
            lastCoachUri = "/coaches/search/findByEmailAddress?email=" + email;
        }

        stepDefs.result = stepDefs.mockMvc.perform(
                get(lastCoachUri)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.emailAddress", is(email)));
    }

    @Given("^There is a coach with name \"([^\"]*)\" and email \"([^\"]*)\"$")
    public void thereIsACoach(String name, String email) throws Throwable {
        iCreateANewCoach(name, email, "123456789");
    }

    @When("^I retrieve the coach with email \"([^\"]*)\"$")
    public void iRetrieveCoach(String email) throws Throwable {
        String url = (lastCoachUri != null) ? lastCoachUri : "/coaches/search/findByEmailAddress?email=" + email;
        stepDefs.result = stepDefs.mockMvc.perform(get(url)
                .with(user("admin").roles("ADMIN")));
    }

    @Then("^The coach name is \"([^\"]*)\"$")
    public void verifyCoachName(String name) throws Throwable {
        stepDefs.result.andExpect(jsonPath("$.name", is(name)));
    }

    @When("^I update the coach \"([^\"]*)\" with new phone \"([^\"]*)\"$")
    public void iUpdateCoach(String email, String newPhone) throws Throwable {
        Map<String, String> payload = new HashMap<>();
        payload.put("phoneNumber", newPhone);

        stepDefs.result = stepDefs.mockMvc.perform(patch(lastCoachUri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stepDefs.mapper.writeValueAsString(payload))
                .with(user("admin").roles("ADMIN")));
    }

    @Then("^The coach \"([^\"]*)\" has phone \"([^\"]*)\"$")
    public void verifyCoachPhone(String email, String phone) throws Throwable {
        iRetrieveCoach(email);
        stepDefs.result.andExpect(jsonPath("$.phoneNumber", is(phone)));
    }

    @When("^I delete the coach with email \"([^\"]*)\"$")
    public void iDeleteCoach(String email) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(delete(lastCoachUri)
                .with(user("admin").roles("ADMIN")));
    }
}