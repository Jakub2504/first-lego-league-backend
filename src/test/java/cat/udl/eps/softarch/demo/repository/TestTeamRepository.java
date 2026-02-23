package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "teams", path = "teams")
public interface TestTeamRepository extends JpaRepository<Team, Long> {
}