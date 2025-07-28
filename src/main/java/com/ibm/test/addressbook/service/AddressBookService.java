package com.ibm.test.addressbook.service;

import com.ibm.test.addressbook.model.AddressBook;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for handling address book API operations
 */
@ApplicationScoped
public class AddressBookService implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(AddressBookService.class.getName());
    private static final String API_BASE_URL = "http://localhost:8081/api/addressbook";
    
    private final CloseableHttpClient httpClient;
    
    public AddressBookService() {
        this.httpClient = HttpClients.createDefault();
    }
    
    /**
     * Retrieve all contacts from the API
     */
    public List<AddressBook> getAllContacts() {
        List<AddressBook> contacts = new ArrayList<>();
        
        try {
            HttpGet httpGet = new HttpGet(API_BASE_URL);
            String response = httpClient.execute(httpGet, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    return new String(httpResponse.getEntity().getContent().readAllBytes());
                }
                return "[]";
            });
            
            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonArray jsonArray = jsonReader.readArray();
            
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                contacts.add(jsonToAddressBook(jsonObject));
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching contacts from API", e);
        }
        
        return contacts;
    }
    
    /**
     * Get a single contact by ID
     */
    public AddressBook getContactById(Long id) {
        try {
            HttpGet httpGet = new HttpGet(API_BASE_URL + "/" + id);
            return httpClient.execute(httpGet, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    String response = new String(httpResponse.getEntity().getContent().readAllBytes());
                    JsonReader jsonReader = Json.createReader(new StringReader(response));
                    JsonObject jsonObject = jsonReader.readObject();
                    return jsonToAddressBook(jsonObject);
                }
                return null;
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error fetching contact by ID: " + id, e);
            return null;
        }
    }
    
    /**
     * Create a new contact
     */
    public AddressBook createContact(AddressBook contact) {
        try {
            HttpPost httpPost = new HttpPost(API_BASE_URL);
            JsonObject json = addressBookToJson(contact);
            httpPost.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
            
            return httpClient.execute(httpPost, httpResponse -> {
                if (httpResponse.getCode() == 201 || httpResponse.getCode() == 200) {
                    String response = new String(httpResponse.getEntity().getContent().readAllBytes());
                    JsonReader jsonReader = Json.createReader(new StringReader(response));
                    JsonObject jsonObject = jsonReader.readObject();
                    return jsonToAddressBook(jsonObject);
                }
                return null;
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating contact", e);
            return null;
        }
    }
    
    /**
     * Update an existing contact
     */
    public AddressBook updateContact(Long id, AddressBook contact) {
        try {
            HttpPut httpPut = new HttpPut(API_BASE_URL + "/" + id);
            JsonObject json = addressBookToJson(contact);
            httpPut.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));
            
            return httpClient.execute(httpPut, httpResponse -> {
                if (httpResponse.getCode() == 200) {
                    String response = new String(httpResponse.getEntity().getContent().readAllBytes());
                    JsonReader jsonReader = Json.createReader(new StringReader(response));
                    JsonObject jsonObject = jsonReader.readObject();
                    return jsonToAddressBook(jsonObject);
                }
                return null;
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error updating contact with ID: " + id, e);
            return null;
        }
    }
    
    /**
     * Delete a contact
     */
    public boolean deleteContact(Long id) {
        try {
            HttpDelete httpDelete = new HttpDelete(API_BASE_URL + "/" + id);
            return httpClient.execute(httpDelete, httpResponse -> 
                httpResponse.getCode() == 204 || httpResponse.getCode() == 200
            );
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting contact with ID: " + id, e);
            return false;
        }
    }
    
    /**
     * Search contacts by query string
     */
    public List<AddressBook> searchContacts(String query, String category) {
        List<AddressBook> allContacts = getAllContacts();
        List<AddressBook> filteredContacts = new ArrayList<>();
        
        for (AddressBook contact : allContacts) {
            boolean matchesQuery = query == null || query.isEmpty() ||
                contact.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                contact.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                contact.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                (contact.getCompany() != null && contact.getCompany().toLowerCase().contains(query.toLowerCase()));
            
            boolean matchesCategory = category == null || category.isEmpty() || 
                category.equals(contact.getCategory());
            
            if (matchesQuery && matchesCategory) {
                filteredContacts.add(contact);
            }
        }
        
        return filteredContacts;
    }
    
    /**
     * Convert JSON object to AddressBook entity
     */
    private AddressBook jsonToAddressBook(JsonObject json) {
        AddressBook contact = new AddressBook();
        
        if (json.containsKey("id")) {
            contact.setId(json.getJsonNumber("id").longValue());
        }
        contact.setFirstName(json.getString("firstName", ""));
        contact.setLastName(json.getString("lastName", ""));
        contact.setEmail(json.getString("email", ""));
        contact.setPhone(json.getString("phone", ""));
        contact.setCompany(json.getString("company", ""));
        contact.setCategory(json.getString("category", ""));
        contact.setAddress(json.getString("address", ""));
        
        return contact;
    }
    
    /**
     * Convert AddressBook entity to JSON object
     */
    private JsonObject addressBookToJson(AddressBook contact) {
        return Json.createObjectBuilder()
                .add("firstName", contact.getFirstName() != null ? contact.getFirstName() : "")
                .add("lastName", contact.getLastName() != null ? contact.getLastName() : "")
                .add("email", contact.getEmail() != null ? contact.getEmail() : "")
                .add("phone", contact.getPhone() != null ? contact.getPhone() : "")
                .add("company", contact.getCompany() != null ? contact.getCompany() : "")
                .add("category", contact.getCategory() != null ? contact.getCategory() : "")
                .add("address", contact.getAddress() != null ? contact.getAddress() : "")
                .build();
    }
}