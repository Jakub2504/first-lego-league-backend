package cat.udl.eps.softarch.fll.domain.edition;

import cat.udl.eps.softarch.fll.domain.DomainValidation;
import cat.udl.eps.softarch.fll.domain.UriEntity;
import cat.udl.eps.softarch.fll.domain.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Edition extends UriEntity<Long> {

	public static final int MAX_TEAMS = 18;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "edition_year")
	private Integer year;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "venue_id", nullable = false)
	private Venue venue;

	@NotBlank
	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private EditionState state = EditionState.DRAFT;

	@OneToMany(mappedBy = "edition")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Team> teams = new ArrayList<>();

	protected Edition() {
	}

	@JsonProperty("venueName")
	public String getVenueName() {
		return venue != null ? venue.getName() : null;
	}

	@JsonProperty("venueCity")
	public String getVenueCity() {
		return venue != null ? venue.getCity() : null;
	}

	public static Edition create(Integer year, Venue venue, String description) {
		DomainValidation.requireNonNull(year, "year");
		DomainValidation.requireNonNull(venue, "venue");
		DomainValidation.requireNonBlank(description, "description");

		Edition edition = new Edition();
		edition.year = year;
		edition.venue = venue;
		edition.description = description;
		return edition;
	}

	public boolean hasReachedMaxTeams() {
		return teams.size() >= MAX_TEAMS;
	}

	public boolean containsTeam(Team team) {
		return teams.contains(team);
	}
}
