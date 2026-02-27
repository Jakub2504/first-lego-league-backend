package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * This entity represents an award given to a team in a specific edition.
 * It is a many-to-many relationship between Edition and Team.
 */
@Entity
@Data
@IdClass(AwardId.class)
@EqualsAndHashCode(callSuper = true)
public class Award extends UriEntity<AwardId> {

	/**
	 * The name of the award.
	 * Must be a non-blank string.
	 */
	@Id
	@NotBlank
	private String name;

	@Id
	@ManyToOne
	@JoinColumn(name = "edition_id")
	@JsonIdentityReference(alwaysAsId = true)
	private Edition edition;


	/**
	 * The team that won this award.
	 * Must be a non-null value.
	 * Serialized as a URI reference to avoid infinite recursion.
	 */

	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	private Team winner;

	@Override
	public AwardId getId() {
		return new AwardId(this.name, this.edition != null ? this.edition.getId() : null);
	}
}