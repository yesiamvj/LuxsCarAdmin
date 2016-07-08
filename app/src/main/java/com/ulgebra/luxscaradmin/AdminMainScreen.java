package com.ulgebra.luxscaradmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AdminMainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);
    }
    public void goToAdminBooking(View view){
        Intent intent=new Intent(getApplicationContext(),AdminBookingHistory.class);
        startActivity(intent);
    }

    public void goToAdminBrowseCars(View view){
        Intent intent=new Intent(getApplicationContext(),Cars.class);
        startActivity(intent);
    }
    public void goToAdminSearchBooking(View view){
        Intent intent=new Intent(getApplicationContext(),SearchBooking.class);
        startActivity(intent);
    }

}
