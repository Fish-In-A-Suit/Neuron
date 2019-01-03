package org.tord.neuroncore.registration;

import java.time.Month;

public class Birthday {
    private int year;
    private Month month;
    private int day;

    public Birthday(String month, int day, int year) {
        this.year = year;
        determineMonth(month);
        this.day = day;

        System.out.println("[Neuron.NC.registration.Birthday]: Created a new birthday instance: " + month + " " + day + " " + year);
    }

    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    /**
     * Determines which month enum to use based on the input month value.
     * @param month
     */
    private void determineMonth(String month) {
        switch(month) {
            case "January":
                this.month = Month.JANUARY;
                break;
            case "February":
                this.month = Month.FEBRUARY;
                break;
            case "March":
                this.month = Month.MARCH;
                break;
            case "April":
                this.month = Month.APRIL;
                break;
            case "May":
                this.month = Month.MAY;
                break;
            case "June":
                this.month = Month.JUNE;
                break;
            case "July":
                this.month = Month.JULY;
                break;
            case "August":
                this.month = Month.AUGUST;
                break;
            case "September":
                this.month = Month.SEPTEMBER;
                break;
            case "October":
                this.month = Month.OCTOBER;
                break;
            case "November":
                this.month = Month.NOVEMBER;
                break;
            case "December":
                this.month = Month.DECEMBER;
                break;
        }
    }

    @Override
    public String toString() {
        return "" + month.toString() + " " + day + " " + year;
    }
}
