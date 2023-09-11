package com.novacroft.library.front_office;

import java.util.Date;

/**
 * Specification for front office book availability API
 */
public interface BookAvailabilityService {
    Boolean isBookInLibrary(String bookIdentificationNumber);

    Date whenIsBookAvailable(String bookIdentificationNumber);
}
