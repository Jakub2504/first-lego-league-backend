package cat.udl.eps.softarch.fll.dto;


import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchSearchItemResponse {

	@NotBlank
	private String matchId;

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

	@NotBlank
	private String tableId;

	@NotNull
	private Long roundId;

}