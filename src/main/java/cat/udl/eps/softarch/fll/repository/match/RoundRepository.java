package cat.udl.eps.softarch.fll.repository.match;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import cat.udl.eps.softarch.fll.domain.match.Round;

@RepositoryRestResource
public interface RoundRepository extends CrudRepository<Round, Long>, PagingAndSortingRepository<Round, Long> {
	List<Round> findByEditionId(@Param("editionId") Long editionId);
}
