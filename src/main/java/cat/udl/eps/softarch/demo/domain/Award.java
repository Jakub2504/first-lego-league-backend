package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This entity represents an award given to a team in a specific edition.
 * It is a many-to-many relationship between Edition and Team.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Award extends UriEntity<String> {

    /**
     * The name of the award.
     * Must be a non-blank string.
     */
    @Id
    @NotBlank
    private String name;

    /* @ManyToOne
    @JoinColumn(name = "edition_id")
	@JsonIdentityReference(alwaysAsId = true)
    private Edition edition;

    /**
     * The team that won this award.
     * Must be a non-null value.
     * Serialized as a URI reference to avoid infinite recursion.
     */
    /*@ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Team winner;*/


    @Override
    public String getId() {
        return this.name;
    }
}