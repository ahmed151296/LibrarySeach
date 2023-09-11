package com.novacroft.library.back_office.stub;

import com.google.gson.Gson;
import com.novacroft.library.back_office.Librarian;

import java.util.HashMap;
import java.util.Map;

/**
 * Library stub
 */
public class LibrarianImpl implements Librarian {
    protected static final String CLEAN_CODE_BIN = "TYK-268-921";

    @Override
    public String bookEnquiry(String bookIdentificationNumber) {
        if (CLEAN_CODE_BIN.equals(bookIdentificationNumber)) {
            return getCleanCodeBookInformation();
        }
        return getBookNotInLibraryInformation(bookIdentificationNumber);
    }

    protected String getCleanCodeBookInformation() {
        return serialize(getEnquiryMap(CLEAN_CODE_BIN, true, getBookMap("Clean Code", "Bob Martin", "2014-12-25")));
    }

    protected String getBookNotInLibraryInformation(String bookIdentificationNumber) {
        return serialize(getEnquiryMap(bookIdentificationNumber, false));
    }

    protected Map getBookMap(String title, String author, String availableFrom) {
        Map bookMap = new HashMap<String, Object>();
        bookMap.put("title", title);
        bookMap.put("author", author);
        bookMap.put("availableFrom", availableFrom);
        return bookMap;
    }

    protected Map getEnquiryMap(String bookIdentificationNumber, boolean isAvailable) {
        return getEnquiryMap(bookIdentificationNumber, isAvailable, null);
    }

    protected Map getEnquiryMap(String bookIdentificationNumber, boolean isAvailable, Map bookMap) {
        Map enquiryMap = new HashMap<String, Object>();
        enquiryMap.put("BIN", bookIdentificationNumber);
        enquiryMap.put("isInLibrary", isAvailable);
        if (bookMap != null && !bookMap.isEmpty()) {
            enquiryMap.put("book", bookMap);
        }
        return enquiryMap;
    }

    protected String serialize(Map enquiryMap) {
        Gson gson = new Gson();
        return gson.toJson(enquiryMap);
    }
}
