package com.ulgebra.luxscaradmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class SearchBooking extends AppCompatActivity {
    AutoCompleteTextView searchBookingNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_booking);
        searchBookingNo=(AutoCompleteTextView)findViewById(R.id.search_bookingNo);

        Button searchBookingBtn=(Button)findViewById(R.id.searchBookingBtn);
        searchBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SingleBookingDetails.class);
                String searchBookingNoTXt=searchBookingNo.getText().toString();
                intent.putExtra("booking_idd","#"+searchBookingNoTXt);
                startActivity(intent);
            }
        });
    }
}
