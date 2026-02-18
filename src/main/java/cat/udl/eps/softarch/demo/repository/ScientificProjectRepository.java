package cat.udl.eps.softarch.demo.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.demo.domain.ScientificProject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ScientificProjects", description = "Repository for managing ScientificProject entities")
@RepositoryRestResource
public interface ScientificProjectRepository extends CrudRepository<ScientificProject, Long>,
		PagingAndSortingRepository<ScientificProject, Long> {

	@Operation(summary = "Find projects by minimum score",
			description = "Returns ScientificProjects with score >= specified value")
	List<ScientificProject> findByScoreGreaterThanEqual(@Param("minScore") Integer minScore);
}

