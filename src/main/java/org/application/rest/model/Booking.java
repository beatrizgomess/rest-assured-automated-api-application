package org.application.rest.model;

public class Booking {

    private String firstname;
    private String lastname;
    private String additionalneeds;
    private float totalPrice;
    private boolean depositpaid;
    private BookingDates bookingDates;


    public Booking(String firstname, String lastname, String additionalneeds,
                   float totalPrice, boolean depositpaid,
                   BookingDates bookingDates) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.additionalneeds = additionalneeds;
        this.totalPrice = totalPrice;
        this.depositpaid = depositpaid;
        this.bookingDates = bookingDates;

    }

    public Booking(String firstName, String lastName, float v, boolean b, BookingDates bookingDates, String s) {
    }

    public Booking() {
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public BookingDates getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(BookingDates bookingDates) {
        this.bookingDates = bookingDates;
    }
}
