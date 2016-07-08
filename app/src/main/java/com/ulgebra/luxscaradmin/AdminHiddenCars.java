package com.ulgebra.luxscaradmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AdminHiddenCars extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hidden_cars);
    }
    public void goToSingleCarDetail(View view){
        Intent intent=new Intent(getApplicationContext(),SingleCarDetail.class);
        startActivity(intent);
    }
}
