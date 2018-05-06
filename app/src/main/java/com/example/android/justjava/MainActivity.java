package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int i = 0;
    Handler barHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            View lineColor = findViewById(R.id.lineview);
            View lineColor2 = findViewById(R.id.lineview2);
            View lineColor3 = findViewById(R.id.lineview3);
            String[] colorcode = new String[6];
            colorcode[0] = "#009688";
            colorcode[1] = "#03A9F4";
            colorcode[2] = "#6200EA";
            colorcode[3] = "#D50000";
            colorcode[4] = "#1DE9B6";
            colorcode[5] = "#FFD600";
            lineColor.setBackgroundColor(Color.parseColor(colorcode[i]));
            lineColor2.setBackgroundColor(Color.parseColor(colorcode[i]));
            lineColor3.setBackgroundColor(Color.parseColor(colorcode[i]));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorBar();
    }

    int orderNum = 2;
    String priceMessage = "$0";

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int price;
        CheckBox wippedCreamCheckBox = (CheckBox) findViewById(R.id.whippedcream_checkbox);
        boolean hasWhippedCream = wippedCreamCheckBox.isChecked();

        CheckBox chocolatechipCheckBox = (CheckBox) findViewById(R.id.chocolatechip_checkbox);
        boolean haschocolateChip = chocolatechipCheckBox.isChecked();

        EditText nameTextField = (EditText) findViewById(R.id.editNameText);
        String customerName = nameTextField.getText().toString();

        Log.v("MainActivity","Has Whipped Cream:"+hasWhippedCream);
        Log.v("MainActivity","Has Chocolate Chip:"+haschocolateChip);
        Log.v("MainActivity","Customer Name:"+customerName);

        price = calculatePrice(hasWhippedCream,haschocolateChip);

        /** Locale is used to set hardcode country currency instance*/
        //Locale locale = new Locale("en", "US");
        priceMessage =  getString(R.string.namej)+customerName+
                "\n"+getString(R.string.addwhipped_cream)+hasWhippedCream+
                "\n"+getString(R.string.addchocolate_chip)+haschocolateChip+
                "\n"+getString(R.string.quantityj)+orderNum+
                /** you can set currency instance with parameter locale to set location currency hardcoded **/
                 // "\n"+getString(R.string.totalj)+(NumberFormat.getCurrencyInstance(locale).format(price))+
                /** you can set currency instance with blank parameter to get location currency from mobile country language **/
                "\n"+getString(R.string.totalj)+(NumberFormat.getCurrencyInstance().format(price))+
                "\n"+getString(R.string.thank_you);
        composeEmail("Just Java"+getString(R.string.email_subject)+customerName,priceMessage);
    }

    /**
     * Calculates the price of the order with topping price.
     */
    private int calculatePrice(boolean addcream, boolean addchip) {
        int basePrice = 5;
        if(addcream){
            basePrice = basePrice + 1;
        }
        if(addchip){
            basePrice = basePrice + 2;
        }
        return basePrice * orderNum;
    }

    /**
     * This method is called when the "+" button is clicked.
     */
    public void plus(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Can't have more than 100 cups";
        Toast toast = Toast.makeText(context, text, duration);
        if(orderNum >= 100){
            toast.show();
        }
        else{
            orderNum++;
            display(orderNum);
            priceMessage = "$0";
            displayMessage(priceMessage);
        }
    }

    /**
     * This method is called when the "-" button is clicked.
     */
    public void minus(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text = "Can't have less than 1 cup";
        Toast toast = Toast.makeText(context, text, duration);
        if (orderNum <= 1) {
            toast.show();
        } else {
            orderNum--;
            display(orderNum);
            priceMessage = "$0";
            displayMessage(priceMessage);
        }
    }

    public void clearSummary(View view){
            priceMessage = "$0";
            displayMessage(priceMessage);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView order_summary_TextView = (TextView) findViewById(R.id.order_summary_text_view);
        order_summary_TextView.setText(message);
    }

    public void composeEmail(String subject, String attachment) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void colorBar(){
        Thread barColor = new Thread(){
            public void run(){

                for(i = 0; i < 6; i++){
                    if(i==5){
                        barHandler.sendEmptyMessage(0);
                        i = 0;
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else barHandler.sendEmptyMessage(0);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        barColor.start();
    }
}