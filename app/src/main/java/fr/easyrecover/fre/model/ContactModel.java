package fr.easyrecover.fre.model;

public class ContactModel {

    String conractRawId = "";
    String contactContactId = "";
    String contactDeleted = "";
    String contactName = "";

    public ContactModel(String conractRawId, String contactContactId, String contactName, String contactDeleted) {
        this.conractRawId = conractRawId;
        this.contactContactId = contactContactId;
        this.contactName = contactName;
        this.contactDeleted = contactDeleted;
    }

    public String getConractRawId() {
        return this.conractRawId;
    }

    public void setConractRawId(String conractRawId) {
        this.conractRawId = conractRawId;
    }

    public String getContactContactId() {
        return this.contactContactId;
    }

    public void setContactContactId(String contactContactId) {
        this.contactContactId = contactContactId;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactDeleted() {
        return this.contactDeleted;
    }

    public void setContactDeleted(String contactDeleted) {
        this.contactDeleted = contactDeleted;
    }
}
