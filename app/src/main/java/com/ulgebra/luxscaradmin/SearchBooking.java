package com.ulgebra.luxscaradmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class SearchBooking extends AppCompatActivity {
    AutoCompleteTextView searchBookingNo,searchUserFiltr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_booking);
        searchBookingNo=(AutoCompleteTextView)findViewById(R.id.search_bookingNo);
        searchUserFiltr=(AutoCompleteTextView)findViewById(R.id.search_user_filtr);

        Button searchBookingBtn=(Button)findViewById(R.id.searchBookingBtn);
        Button searchUserBtn=(Button)findViewById(R.id.searchUserBtn);
        searchBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SingleBookingDetails.class);
                String searchBookingNoTXt=searchBookingNo.getText().toString();
                intent.putExtra("booking_idd","#"+searchBookingNoTXt);
                startActivity(intent);
            }
        });
        searchUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UsersSearchResult.class);
                String searchUsersFilt=searchUserFiltr.getText().toString();
                intent.putExtra("filter_q",searchUsersFilt);
                startActivity(intent);
            }
        });
    }
}
