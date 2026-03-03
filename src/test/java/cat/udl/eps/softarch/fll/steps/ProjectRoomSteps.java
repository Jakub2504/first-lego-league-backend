package cat.udl.eps.softarch.fll.steps;

import cat.udl.eps.softarch.fll.domain.Judge;
import cat.udl.eps.softarch.fll.domain.ProjectRoom;
import cat.udl.eps.softarch.fll.repository.JudgeRepository;
import cat.udl.eps.softarch.fll.repository.ProjectRoomRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectRoomSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRoomRepository roomRepository;

    @Autowired
    private JudgeRepository judgeRepository;

    private ResultActions response;

    @Given("a project room {string} exists")
    public void a_project_room_exists(String roomId) {
        ProjectRoom room = new ProjectRoom();
        room.setRoomNumber(roomId);
        room.setPanelists(new ArrayList<>());
        roomRepository.save(room);
    }

    @Given("a judge {string} exists")
    public void a_judge_exists(String judgeId) {
        Judge judge = new Judge();
        judge.setId(Long.valueOf(judgeId));
        judge.setName("Test Judge " + judgeId);
        judge.setEmailAddress("judge" + judgeId + "@test.com");
        judge.setPhoneNumber("123456789");
        judgeRepository.save(judge);
    }

    @Given("the room {string} already has a manager")
    public void the_room_already_has_a_manager(String roomId) {
        ProjectRoom room = roomRepository.findById(roomId).orElseThrow();
        Judge manager = new Judge();
        manager.setId(9991L);
        manager.setName("Manager");
        manager.setEmailAddress("manager@test.com");
        manager.setPhoneNumber("000");
        judgeRepository.save(manager);
        
        room.setManagedByJudge(manager);
        roomRepository.save(room);
    }

    @Given("the room {string} already has {int} panelists")
    public void the_room_already_has_panelists(String roomId, int count) {
        ProjectRoom room = roomRepository.findById(roomId).orElseThrow();
        
        for (int i = 0; i < count; i++) {
            Judge panelist = new Judge();
            panelist.setId(9000L + i);
            panelist.setName("Panelist " + i);
            panelist.setEmailAddress("panelist" + i + "@test.com");
            panelist.setPhoneNumber("111");
            judgeRepository.save(panelist);
            
            room.getPanelists().add(panelist);
        }
        roomRepository.save(room);
    }

    @Given("judge {string} is already assigned to room {string}")
    public void judge_is_already_assigned_to_room(String judgeId, String roomId) {
        ProjectRoom room = roomRepository.findById(roomId).orElseThrow();
        Judge judge = judgeRepository.findById(Long.valueOf(judgeId)).orElseThrow();
        
        room.getPanelists().add(judge);
        roomRepository.save(room);
    }

    @When("I request to assign judge {string} to room {string} with isManager {word}")
    public void i_request_to_assign_judge_to_room_with_is_manager(String judgeId, String roomId, String isManagerStr) throws Exception {
        boolean isManager = Boolean.parseBoolean(isManagerStr);
        String jsonPayload = String.format(
                "{\"roomId\": \"%s\", \"judgeId\": \"%s\", \"isManager\": %b}",
                roomId, judgeId, isManager
        );

        response = mockMvc.perform(post("/api/project-rooms/assign-judge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload));
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) throws Exception {
        if (expectedStatus == 200) {
            response.andExpect(status().isOk());
        } else if (expectedStatus == 400) {
            response.andExpect(status().isBadRequest());
        }
    }

    @Then("the response role should be {string}")
    public void the_response_role_should_be(String expectedRole) throws Exception {
        response.andExpect(jsonPath("$.role").value(expectedRole));
        response.andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Then("the response error should be {string}")
    public void the_response_error_should_be(String expectedError) throws Exception {
        response.andExpect(jsonPath("$.error").value(expectedError));
    }
}