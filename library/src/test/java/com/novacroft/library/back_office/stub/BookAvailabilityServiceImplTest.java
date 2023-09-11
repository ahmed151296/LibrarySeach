package com.novacroft.library.back_office.stub;

import com.novacroft.library.back_office.Librarian;
import com.novacroft.library.front_office.BookAvailabilityServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookAvailabilityServiceImplTest {

    private BookAvailabilityServiceImpl service;
    private Librarian mockLibrarian;

    @Before
    public void setUp() {
        mockLibrarian = mock(Librarian.class);
        service = new BookAvailabilityServiceImpl(mockLibrarian);
    }

    @Test
    public void isBookInLibrary_ValidBINInLibrary_ReturnsTrue() {
        when(mockLibrarian.bookEnquiry("ABC-123-456"))
                .thenReturn("{\"BIN\":\"ABC-123-456\",\"isInLibrary\":true,\"book\":{\"title\":\"Sample Book\",\"author\":\"John\",\"availableFrom\":\"2023-08-16\"}}");

        Boolean result = service.isBookInLibrary("ABC-123-456");

        assertTrue(result);
    }

    @Test
    public void isBookInLibrary_ValidBINNotInLibrary_ReturnsFalse() {
        when(mockLibrarian.bookEnquiry("ABC-123-456"))
                .thenReturn("{\"BIN\":\"ABC-123-456\",\"isInLibrary\":false}");

        Boolean result = service.isBookInLibrary("ABC-123-456");

        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isBookInLibrary_InvalidBIN_ThrowsException() {
        service.isBookInLibrary("Invalid-BIN");
    }

    @Test
    public void whenIsBookAvailable_ValidBINInLibrary_ReturnsDate() throws Exception {
        when(mockLibrarian.bookEnquiry("ABC-123-456"))
                .thenReturn("{\"BIN\":\"ABC-123-456\",\"isInLibrary\":true,\"book\":{\"title\":\"Sample Book\",\"author\":\"John\",\"availableFrom\":\"2023-08-16\"}}");

        Date result = service.whenIsBookAvailable("ABC-123-456");

        assertNotNull(result);
        assertEquals("Wed Aug 16 00:00:00 BST 2023", result.toString());
    }

    @Test
    public void whenIsBookAvailable_ValidBINNotInLibrary_ReturnsNull() {
        when(mockLibrarian.bookEnquiry("ABC-123-456"))
                .thenReturn("{\"BIN\":\"ABC-123-456\",\"isInLibrary\":false}");

        Date result = service.whenIsBookAvailable("ABC-123-456");

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIsBookAvailable_InvalidBIN_ThrowsException() {
        service.whenIsBookAvailable("Invalid-BIN");
    }
}
