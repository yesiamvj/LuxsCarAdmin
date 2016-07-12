package com.ulgebra.luxscaradmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class BookingViewType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_view_type);
    }

    public void goTodayBookings(View view){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int needay=day;
        int neemon=month+1;
        int neeyear=year;

        String neemonStr="";
        String needayStr="";

        if(neemon<10){
            neemonStr="0"+neemon;
        }
        else {
            neemonStr=neemon+"";
        }

        if(needay<10){
            needayStr="0"+needay;
        }
        else {
            needayStr=needay+"";
        }



        String neededDate=neeyear+"-"+neemonStr+"-"+needayStr;

        Intent intent=new Intent(getApplicationContext(),ThisDateHistory.class);
        intent.putExtra("neededDate",neededDate);
        startActivity(intent);

    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            int needay=day;
            int neemon=month+1;
            int neeyear=year;

            String neemonStr="";
            String needayStr="";

            if(neemon<10){
                 neemonStr="0"+neemon;
            }
            else {
                neemonStr=neemon+"";
            }

            if(needay<10){
                needayStr="0"+needay;
            }
            else {
                needayStr=needay+"";
            }



            String neededDate=neeyear+"-"+neemonStr+"-"+needayStr;

            Intent intent=new Intent(this.getActivity().getApplicationContext(),ThisDateHistory.class);
            intent.putExtra("neededDate",neededDate);
            startActivity(intent);

        }

    }
    public void goToAdminBooking(View view){
        Intent intent=new Intent(getApplicationContext(),BookingHistory.class);

        startActivity(intent);
    }
}
