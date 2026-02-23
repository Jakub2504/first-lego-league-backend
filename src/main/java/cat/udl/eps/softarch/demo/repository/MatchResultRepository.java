package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Exposed as a REST resource via Spring Data REST.
 */
@Tag(name = "Match Results", description = "Repository for managing match scores and results")
@RepositoryRestResource 
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    /*@Operation(summary = "Find results by match", 
              description = "Returns a list of results associated with a specific match.")
    List<MatchResult> findByMatch(@Param("match") Match match);

    @Operation(summary = "Find results by team", 
               description = "Returns a list of results achieved by a specific team.")
    List<MatchResult> findByTeam(@Param("team") Team team);*/
}