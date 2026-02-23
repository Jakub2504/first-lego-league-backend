package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

/**
 * This entity represents an award given to a team in a specific edition.
 * It is a many-to-many relationship between Edition and Team.
 */
@Entity
@IdClass(AwardId.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class Award extends UriEntity<AwardId> {

    /**
     * The name of the award.
     * Must be a non-blank string.
     */
    @Id
    @NotBlank
    private String name;

    // COMENTAT TEMPORALMENT FINS QUE ELS COMPANYS ACABIN LA CLASSE EDITION
    // @Id
    // @ManyToOne
    // @JoinColumn(name = "edition_id")
    // @JsonIdentityReference(alwaysAsId = true)
    // private Edition edition;

    /**
     * The team that won this award.
     * Must be a non-null value.
     * Serialized as a URI reference to avoid infinite recursion.
     */
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Team winner;

    /**
     * Implementació manual i TEMPORAL del mètode getId() requerit per Persistable.
     * Retorna l'AwardId només amb el nom.
     */
    @Override
    public AwardId getId() {
        return new AwardId(this.name, null);
    }
}