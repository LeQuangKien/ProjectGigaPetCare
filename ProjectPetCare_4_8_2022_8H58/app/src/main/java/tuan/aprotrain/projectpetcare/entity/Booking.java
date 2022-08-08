package tuan.aprotrain.projectpetcare.entity;

import java.io.Serializable;

public class Booking implements Serializable {
    private String bookingId;
    private String bookingStartDate;//them Start Date
    private String bookingEndDate;
    private String bookingAddress;
    private String notes;
    private float totalPrice;//them total price
    private String payment;
    private long petId;
    public static String TABLE_NAME = "Bookings";

    public Booking() {
    }

    public Booking(String bookingId, String bookingStartDate,
                   String bookingEndDate, String bookingAddress,
                   String notes, float totalPrice, String payment, long petId) {
        this.bookingId = bookingId;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.bookingAddress = bookingAddress;
        this.notes = notes;
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.petId = petId;
    }

    public Booking(long petId, String bookingStartDate, String bookingEndDate, String payment, String notes) {
        this.petId = petId;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.payment = payment;
        this.notes = notes;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(String bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public String getBookingAddress() {
        return bookingAddress;
    }

    public void setBookingAddress(String bookingAddress) {
        this.bookingAddress = bookingAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static void setTableName(String tableName) {
        TABLE_NAME = tableName;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(String bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }
}
