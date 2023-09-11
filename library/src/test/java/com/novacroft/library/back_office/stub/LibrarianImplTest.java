package com.novacroft.library.back_office.stub;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LibrarianImplTest {
    private static final String TEST_BOOK_IDENTIFICATION_NUMBER = "test-bin";
    private static final String TEST_TITLE = "test-title";
    private static final String TEST_AUTHOR = "test-author";
    private static final String TEST_AVAILABLE_FROM = "test-available-from";

    private LibrarianImpl librarian;

    private Map<String, Object> testBookMap;
    private Map<String, Object> testEnquiryForAvailableBookMap;
    private Map<String, Object> testEnquiryForNotAvailableBookMap;

    @Before
    public void setUp() {
        this.librarian = mock(LibrarianImpl.class);

        this.testBookMap = new HashMap<String, Object>();
        this.testBookMap.put("title", TEST_TITLE);
        this.testBookMap.put("author", TEST_AUTHOR);
        this.testBookMap.put("availableFrom", TEST_AVAILABLE_FROM);

        this.testEnquiryForAvailableBookMap = new HashMap<String, Object>();
        this.testEnquiryForAvailableBookMap.put("BIN", TEST_BOOK_IDENTIFICATION_NUMBER);
        this.testEnquiryForAvailableBookMap.put("isInLibrary", true);
        this.testEnquiryForAvailableBookMap.put("book", this.testBookMap);

        this.testEnquiryForNotAvailableBookMap = new HashMap<String, Object>();
        this.testEnquiryForNotAvailableBookMap.put("BIN", TEST_BOOK_IDENTIFICATION_NUMBER);
        this.testEnquiryForNotAvailableBookMap.put("isInLibrary", false);

    }

    @Test
    public void shouldSerializeEnquiryWithBook() {
        when(this.librarian.serialize(anyMap())).thenCallRealMethod();

        String jsonResult = this.librarian.serialize(this.testEnquiryForAvailableBookMap);

        Map mapResult = deSerialize(jsonResult);
        assertEquals(TEST_BOOK_IDENTIFICATION_NUMBER, mapResult.get("BIN"));
        assertTrue((boolean) mapResult.get("isInLibrary"));
        assertEquals(TEST_TITLE, ((Map) mapResult.get("book")).get("title"));
        assertEquals(TEST_AUTHOR, ((Map) mapResult.get("book")).get("author"));
        assertEquals(TEST_AVAILABLE_FROM, ((Map) mapResult.get("book")).get("availableFrom"));
    }

    @Test
    public void shouldSerializeEnquiryWithoutBook() {
        when(this.librarian.serialize(anyMap())).thenCallRealMethod();

        String jsonResult = this.librarian.serialize(this.testEnquiryForNotAvailableBookMap);

        Map mapResult = deSerialize(jsonResult);
        assertEquals(TEST_BOOK_IDENTIFICATION_NUMBER, mapResult.get("BIN"));
        assertFalse((boolean) mapResult.get("isInLibrary"));
        assertFalse(mapResult.containsKey("book"));
    }

    @Test
    public void shouldGetBookMap() {
        when(this.librarian.getBookMap(anyString(), anyString(), anyString())).thenCallRealMethod();
        Map result = this.librarian.getBookMap(TEST_TITLE, TEST_AUTHOR, TEST_AVAILABLE_FROM);
        assertEquals(TEST_TITLE, result.get("title"));
        assertEquals(TEST_AUTHOR, result.get("author"));
        assertEquals(TEST_AVAILABLE_FROM, result.get("availableFrom"));
    }

    @Test
    public void shouldGetEnquiryMapWithoutBook() {
        when(this.librarian.getEnquiryMap(anyString(), anyBoolean())).thenCallRealMethod();
        when(this.librarian.getEnquiryMap(anyString(), anyBoolean(), anyMap())).thenCallRealMethod();
        Map result = this.librarian.getEnquiryMap(TEST_BOOK_IDENTIFICATION_NUMBER, false);
        assertEquals(TEST_BOOK_IDENTIFICATION_NUMBER, result.get("BIN"));
        assertFalse((boolean) result.get("isInLibrary"));
    }

    @Test
    public void shouldGetEnquiryMapWithBook() {
        when(this.librarian.getEnquiryMap(anyString(), anyBoolean(), anyMap())).thenCallRealMethod();
        Map result = this.librarian.getEnquiryMap(TEST_BOOK_IDENTIFICATION_NUMBER, true, this.testBookMap);
        assertEquals(TEST_BOOK_IDENTIFICATION_NUMBER, result.get("BIN"));
        assertTrue((boolean) result.get("isInLibrary"));
        assertTrue(result.containsKey("book"));
    }

    @Test
    public void shouldGetBookNotInLibraryInformation() {
        when(this.librarian.getBookNotInLibraryInformation(anyString())).thenCallRealMethod();
        when(this.librarian.getEnquiryMap(anyString(), anyBoolean())).thenReturn(this.testEnquiryForNotAvailableBookMap);
        when(this.librarian.serialize(anyMap())).thenReturn("");
        this.librarian.getBookNotInLibraryInformation(TEST_BOOK_IDENTIFICATION_NUMBER);
        verify(this.librarian).getEnquiryMap(anyString(), anyBoolean());
        verify(this.librarian).serialize(anyMap());
    }

    @Test
    public void shouldGetCleanCodeBookInformation() {
        when(this.librarian.getCleanCodeBookInformation()).thenCallRealMethod();
        when(this.librarian.getEnquiryMap(anyString(), anyBoolean(), anyMap())).thenReturn(this.testEnquiryForAvailableBookMap);
        when(this.librarian.serialize(anyMap())).thenReturn("");
        this.librarian.getCleanCodeBookInformation();
        verify(this.librarian).getEnquiryMap(anyString(), anyBoolean(), anyMap());
        verify(this.librarian).serialize(anyMap());
    }

    @Test
    public void bookEnquiryShouldGetCleanCode() {
        when(this.librarian.bookEnquiry(anyString())).thenCallRealMethod();
        when(this.librarian.getCleanCodeBookInformation()).thenReturn("");
        when(this.librarian.getBookNotInLibraryInformation(anyString())).thenReturn("");
        this.librarian.bookEnquiry(LibrarianImpl.CLEAN_CODE_BIN);
        verify(this.librarian).getCleanCodeBookInformation();
        verify(this.librarian, never()).getBookNotInLibraryInformation(anyString());
    }

    @Test
    public void bookEnquiryShouldGetNotAvailable() {
        when(this.librarian.bookEnquiry(anyString())).thenCallRealMethod();
        when(this.librarian.getCleanCodeBookInformation()).thenReturn("");
        when(this.librarian.getBookNotInLibraryInformation(anyString())).thenReturn("");
        this.librarian.bookEnquiry(TEST_BOOK_IDENTIFICATION_NUMBER);
        verify(this.librarian, never()).getCleanCodeBookInformation();
        verify(this.librarian).getBookNotInLibraryInformation(anyString());
    }

    private Map deSerialize(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, HashMap.class);
    }
}