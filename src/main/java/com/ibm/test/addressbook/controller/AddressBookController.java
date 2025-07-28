package com.ibm.test.addressbook.controller;

import com.ibm.test.addressbook.model.AddressBook;
import com.ibm.test.addressbook.service.AddressBookService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JSF Managed Bean for Address Book operations
 */
@Named("addressBookController")
@ViewScoped
public class AddressBookController implements Serializable {
    
    @Inject
    private AddressBookService addressBookService;
    
    private AddressBook currentContact;
    private List<AddressBook> contacts;
    private List<AddressBook> filteredContacts;
    private String searchQuery;
    private String selectedCategory;
    private boolean editMode;
    
    private final List<String> categories = Arrays.asList(
        "personal", "business", "family", "friend"
    );
    
    @PostConstruct
    public void init() {
        currentContact = new AddressBook();
        loadContacts();
        filteredContacts = new ArrayList<>(contacts);
        editMode = false;
    }
    
    /**
     * Load all contacts from the service
     */
    public void loadContacts() {
        try {
            contacts = addressBookService.getAllContacts();
            filteredContacts = new ArrayList<>(contacts);
        } catch (Exception e) {
            contacts = new ArrayList<>();
            filteredContacts = new ArrayList<>();
            addErrorMessage("Error loading contacts: " + e.getMessage());
        }
    }
    
    /**
     * Add a new contact
     */
    public void addContact() {
        try {
            AddressBook createdContact = addressBookService.createContact(currentContact);
            if (createdContact != null) {
                addSuccessMessage("Contact added successfully!");
                clearForm();
                loadContacts();
                performSearch(); // Refresh the filtered list
            } else {
                addErrorMessage("Failed to add contact. Please try again.");
            }
        } catch (Exception e) {
            addErrorMessage("Error adding contact: " + e.getMessage());
        }
    }
    
    /**
     * Update an existing contact
     */
    public void updateContact() {
        try {
            AddressBook updatedContact = addressBookService.updateContact(currentContact.getId(), currentContact);
            if (updatedContact != null) {
                addSuccessMessage("Contact updated successfully!");
                clearForm();
                loadContacts();
                performSearch(); // Refresh the filtered list
            } else {
                addErrorMessage("Failed to update contact. Please try again.");
            }
        } catch (Exception e) {
            addErrorMessage("Error updating contact: " + e.getMessage());
        }
    }
    
    /**
     * Delete a contact
     */
    public void deleteContact(AddressBook contact) {
        try {
            boolean deleted = addressBookService.deleteContact(contact.getId());
            if (deleted) {
                addSuccessMessage("Contact deleted successfully!");
                loadContacts();
                performSearch(); // Refresh the filtered list
            } else {
                addErrorMessage("Failed to delete contact. Please try again.");
            }
        } catch (Exception e) {
            addErrorMessage("Error deleting contact: " + e.getMessage());
        }
    }
    
    /**
     * Edit a contact - populate form with contact data
     */
    public void editContact(AddressBook contact) {
        currentContact = new AddressBook();
        currentContact.setId(contact.getId());
        currentContact.setFirstName(contact.getFirstName());
        currentContact.setLastName(contact.getLastName());
        currentContact.setEmail(contact.getEmail());
        currentContact.setPhone(contact.getPhone());
        currentContact.setCompany(contact.getCompany());
        currentContact.setCategory(contact.getCategory());
        currentContact.setAddress(contact.getAddress());
        editMode = true;
    }
    
    /**
     * Cancel edit mode
     */
    public void cancelEdit() {
        clearForm();
    }
    
    /**
     * Clear the form
     */
    public void clearForm() {
        currentContact = new AddressBook();
        editMode = false;
    }
    
    /**
     * Submit the form (add or update)
     */
    public void submitForm() {
        if (editMode) {
            updateContact();
        } else {
            addContact();
        }
    }
    
    /**
     * Perform search based on query and category
     */
    public void performSearch() {
        try {
            filteredContacts = addressBookService.searchContacts(searchQuery, selectedCategory);
        } catch (Exception e) {
            addErrorMessage("Error searching contacts: " + e.getMessage());
            filteredContacts = new ArrayList<>(contacts);
        }
    }
    
    /**
     * Clear search filters
     */
    public void clearSearch() {
        searchQuery = "";
        selectedCategory = "";
        filteredContacts = new ArrayList<>(contacts);
    }
    
    /**
     * Get the submit button text based on mode
     */
    public String getSubmitButtonText() {
        return editMode ? "Update Contact" : "Add Contact";
    }
    
    /**
     * Get the form title based on mode
     */
    public String getFormTitle() {
        return editMode ? "Edit Contact" : "Add New Contact";
    }
    
    /**
     * Get total contacts count
     */
    public int getTotalContacts() {
        return filteredContacts != null ? filteredContacts.size() : 0;
    }
    
    /**
     * Check if contacts list is empty
     */
    public boolean isContactsEmpty() {
        return filteredContacts == null || filteredContacts.isEmpty();
    }
    
    // Helper methods for messaging
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and Setters
    public AddressBook getCurrentContact() {
        return currentContact;
    }
    
    public void setCurrentContact(AddressBook currentContact) {
        this.currentContact = currentContact;
    }
    
    public List<AddressBook> getContacts() {
        return contacts;
    }
    
    public void setContacts(List<AddressBook> contacts) {
        this.contacts = contacts;
    }
    
    public List<AddressBook> getFilteredContacts() {
        return filteredContacts;
    }
    
    public void setFilteredContacts(List<AddressBook> filteredContacts) {
        this.filteredContacts = filteredContacts;
    }
    
    public String getSearchQuery() {
        return searchQuery;
    }
    
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
    
    public String getSelectedCategory() {
        return selectedCategory;
    }
    
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
    
    public boolean isEditMode() {
        return editMode;
    }
    
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
    
    public List<String> getCategories() {
        return categories;
    }
}