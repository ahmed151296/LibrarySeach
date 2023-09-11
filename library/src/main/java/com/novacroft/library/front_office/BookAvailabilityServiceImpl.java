package com.novacroft.library.front_office;

import com.novacroft.library.back_office.Librarian;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class BookAvailabilityServiceImpl implements BookAvailabilityService {
    
    private Librarian librarian;
    
    public BookAvailabilityServiceImpl(Librarian librarian) {
        this.librarian = librarian;
    }

    @Override
    public Boolean isBookInLibrary(String bookIdentificationNumber) {
        if (!isValidBIN(bookIdentificationNumber)) {
            throw new IllegalArgumentException("Invalid BIN format.");
        }
        
        String jsonResult = librarian.bookEnquiry(bookIdentificationNumber);
        Map resultMap = deserialize(jsonResult);
        return (Boolean) resultMap.get("isInLibrary");
    }

    @Override
    public Date whenIsBookAvailable(String bookIdentificationNumber) {
        if (!isValidBIN(bookIdentificationNumber)) {
            throw new IllegalArgumentException("Invalid BIN format.");
        }

        String jsonResult = librarian.bookEnquiry(bookIdentificationNumber);
        Map resultMap = deserialize(jsonResult);
        Map bookMap = (Map) resultMap.get("book");
        if (bookMap == null) {
            return null; // Not available in the library
        }
        
        String availableFromString = (String) bookMap.get("availableFrom");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(availableFromString);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date from book availability.", e);
        }
    }

    private boolean isValidBIN(String bin) {
        return bin != null && bin.matches("^[A-Z]{3}-\\d{3}-\\d{3}$");
    }
    
    private Map deserialize(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }
}
