package com.novacroft.library.back_office;

/**
 * Specification for back office librarian API
 *
 * @See com.novacroft.library.back_office.stub.LibrarianImpl
 */
public interface Librarian {
    String bookEnquiry(String bookIdentificationNumber);
}