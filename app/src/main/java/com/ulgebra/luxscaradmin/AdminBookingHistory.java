package com.ulgebra.luxscaradmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AdminBookingHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_history);
    }
    public void goToBookingDetails(View view){
        Intent intent=new Intent(getApplicationContext(),BookingDetails.class);
        startActivity(intent);
    }
}
