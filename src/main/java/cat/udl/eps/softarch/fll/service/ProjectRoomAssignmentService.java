package cat.udl.eps.softarch.fll.service;

import cat.udl.eps.softarch.fll.domain.ProjectRoom;
import cat.udl.eps.softarch.fll.domain.Judge;
import cat.udl.eps.softarch.fll.api.dto.AssignJudgeRequest;
import cat.udl.eps.softarch.fll.api.dto.AssignJudgeResponse;
import cat.udl.eps.softarch.fll.exception.RoomAssignmentException;
import cat.udl.eps.softarch.fll.repository.ProjectRoomRepository;
import cat.udl.eps.softarch.fll.repository.JudgeRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectRoomAssignmentService {
    
    private final ProjectRoomRepository projectRoomRepository;
    private final JudgeRepository judgeRepository;

    public ProjectRoomAssignmentService(ProjectRoomRepository projectRoomRepository, JudgeRepository judgeRepository) {
        this.projectRoomRepository = projectRoomRepository;
        this.judgeRepository = judgeRepository;
    }

    public AssignJudgeResponse assignJudge(AssignJudgeRequest request) {
        
        ProjectRoom room = projectRoomRepository.findById(request.roomId())
                .orElseThrow(() -> new RoomAssignmentException("ROOM_NOT_FOUND", "Room not found"));
                
        Judge judge = judgeRepository.findById(Long.valueOf(request.judgeId()))
                .orElseThrow(() -> new RoomAssignmentException("JUDGE_NOT_FOUND", "Judge not found"));

        boolean isAlreadyManager = room.getManagedByJudge() != null && room.getManagedByJudge().equals(judge);
        boolean isAlreadyPanelist = room.getPanelists().contains(judge);
        
        if (isAlreadyManager || isAlreadyPanelist) {
            throw new RoomAssignmentException("JUDGE_ALREADY_ASSIGNED", "This judge is already assigned to this room");
        }

        String assignedRole;

        if (request.isManager()) {
            if (room.getManagedByJudge() != null) {
                throw new RoomAssignmentException("ROOM_ALREADY_HAS_MANAGER", "This room already has a manager assigned");
            }
            room.setManagedByJudge(judge);
            assignedRole = "MANAGER";
            
        } else {
            if (room.getPanelists().size() >= 3) {
                throw new RoomAssignmentException("MAX_PANELISTS_REACHED", "This room has reached the maximum of 3 panelists");
            }
            
            judge.setMemberOfRoom(room);
            room.getPanelists().add(judge);
            assignedRole = "PANELIST";
        }

        projectRoomRepository.save(room);
        judgeRepository.save(judge);

        return new AssignJudgeResponse(
            request.roomId(),
            request.judgeId(),
            assignedRole,
            "ASSIGNED"
        );
    }
}