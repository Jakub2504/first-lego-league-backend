package cat.udl.eps.softarch.fll.award;

import cat.udl.eps.softarch.fll.domain.ranking.Award;
import cat.udl.eps.softarch.fll.domain.DomainValidationException;
import cat.udl.eps.softarch.fll.domain.edition.Edition;
import cat.udl.eps.softarch.fll.domain.edition.Venue;
import cat.udl.eps.softarch.fll.domain.team.Team;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AwardValidationTest {

	private final Edition validEdition = Edition.create(2024, Venue.create("Lleida Arena", "Lleida"), "FLL Season");
	private final Team validTeam = Team.create("Winners", "Barcelona", 2000, "category");

	@Test
	void validConstruction() {
		assertDoesNotThrow(() -> Award.create("Best Robot", validEdition, validTeam));
	}

	@Nested
	class EmptyName {

		@Test
		void nullNameThrows() {
			assertThrows(DomainValidationException.class,
				() -> Award.create(null, validEdition, validTeam));
		}

		@Test
		void blankNameThrows() {
			assertThrows(DomainValidationException.class,
				() -> Award.create("  ", validEdition, validTeam));
		}
	}

	@Nested
	class NullRequiredReference {

		@Test
		void nullEditionThrows() {
			assertThrows(DomainValidationException.class,
				() -> Award.create("Best Robot", null, validTeam));
		}

		@Test
		void nullWinnerThrows() {
			assertThrows(DomainValidationException.class,
				() -> Award.create("Best Robot", validEdition, null));
		}
	}
}
