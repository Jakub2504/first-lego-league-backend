package cat.udl.eps.softarch.demo.repository;


import cat.udl.eps.softarch.demo.domain.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Repository for managing (Award) entities.
 */
@Tag(name = "Awards", description = "Repository for managing awards and prizes")
@RepositoryRestResource
public interface AwardRepository extends JpaRepository<Award, String> {

    /* @Operation(summary = "Find awards by edition", 
               description = "Returns all awards presented in a specific edition.")
    List<Award> findByEdition(@Param("edition") Edition edition);

    @Operation(summary = "Find awards by winner", 
               description = "Returns all awards won by a specific team.")
    List<Award> findByWinner(@Param("winner") Team winner);*/
}