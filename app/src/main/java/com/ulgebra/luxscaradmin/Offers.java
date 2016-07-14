package com.ulgebra.luxscaradmin;

/**
 * Created by Vijayakumar on 14/07/2016.
 */

public class Offers {
    private String offer_id;
    private String coupon_code;
    private String minPurAmt;
    private String maxDiscAmt;
    private String discPerc;
    private String discPrice;
    private String expiry_date;
    private String addedOn;

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public void setDiscPerc(String discPerc) {
        this.discPerc = discPerc;
    }

    public void setDiscPrice(String discPrice) {
        this.discPrice = discPrice;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public void setMaxDiscAmt(String maxDiscAmt) {
        this.maxDiscAmt = maxDiscAmt;
    }

    public void setMinPurAmt(String minPurAmt) {
        this.minPurAmt = minPurAmt;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getDiscPerc() {
        return discPerc;
    }

    public String getDiscPrice() {
        return discPrice;
    }

    public String getMaxDiscAmt() {
        return maxDiscAmt;
    }

    public String getMinPurAmt() {
        return minPurAmt;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

}

