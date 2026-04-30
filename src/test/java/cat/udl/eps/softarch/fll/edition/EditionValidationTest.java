package cat.udl.eps.softarch.fll.edition;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cat.udl.eps.softarch.fll.domain.DomainValidationException;
import cat.udl.eps.softarch.fll.domain.edition.Edition;
import cat.udl.eps.softarch.fll.domain.edition.Venue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EditionValidationTest {

	private static final Venue VALID_VENUE = Venue.create("Lleida Arena", "Lleida");

	@Test
	void validConstruction() {
		assertDoesNotThrow(() -> Edition.create(2024, VALID_VENUE, "FLL Season"));
	}

	@Nested
	class NullRequiredField {

		@Test
		void nullYearThrows() {
			assertThrows(DomainValidationException.class,
					() -> Edition.create(null, VALID_VENUE, "FLL Season"));
		}

		@Test
		void nullVenueThrows() {
			assertThrows(DomainValidationException.class,
					() -> Edition.create(2024, null, "FLL Season"));
		}
	}

	@Nested
	class EmptyDescription {

		@Test
		void blankDescriptionThrows() {
			assertThrows(DomainValidationException.class,
					() -> Edition.create(2024, VALID_VENUE, ""));
		}
	}
}
