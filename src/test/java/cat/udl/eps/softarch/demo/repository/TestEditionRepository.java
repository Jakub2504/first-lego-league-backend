package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "editions", path = "editions")
public interface TestEditionRepository extends JpaRepository<Edition, Long> {
}