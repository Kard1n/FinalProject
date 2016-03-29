package com.example.kardin.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    String currencyTime = null;
    double currencyUAHtoUSD,currencyUAHtoEUR,currencyUAHtoRUB = 0;
    double currencyUSDtoEUR,currencyUSDtoRUB = 0;
    double currencyEURtoUSD,currencyEURtoRUB = 0;
    double currencyGBPtoUAH,currencyGBPtoUSD,currencyGBPtoEUR,currencyGBPtoRUB = 0;
    int flag = 1;

    String currencyName[] = {"UAH","USD","EUR","RUB","GBP"};
    ProgressDialog progressDialog;
    TextView textViewUSD,textViewEUR,textViewRUB,textViewCurTime;
    TextView curValUAH,curValUSD,curValEUR,curValRUB,curValGBP;
    EditText curVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUSD = (TextView) findViewById(R.id.currencyUSDTextView);
        textViewEUR = (TextView) findViewById(R.id.currencyEURTextView);
        textViewRUB = (TextView) findViewById(R.id.currencyRUBTextView);
        textViewCurTime = (TextView) findViewById(R.id.curTextView);

        curValUAH = (TextView) findViewById(R.id.tvCurValUAH);
        curValUSD = (TextView) findViewById(R.id.tvCurValUSD);
        curValEUR = (TextView) findViewById(R.id.tvCurValEUR);
        curValRUB = (TextView) findViewById(R.id.tvCurValRUB);

        curValGBP = (TextView) findViewById(R.id.tvCurValGBP);

        try {
            FileInputStream fileInputStream = openFileInput("infoCurrency");
            readFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            getInfo GI = new getInfo();
            GI.execute();
        }

        curVal = (EditText) findViewById(R.id.editText);

        curVal.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double getValue;
                if(curVal.getText().length() == 0){
                    getValue = 0;
                }
                else {
                    getValue = Double.parseDouble(curVal.getText().toString());
                }
                switch (flag){
                    case 1:{
                        curValUAH.setText(Double.toString(getValue));
                        curValUSD.setText(String.format(Locale.US, "%.2f", getValue / currencyUAHtoUSD));
                        curValEUR.setText(String.format(Locale.US, "%.2f", getValue / currencyUAHtoEUR));
                        curValRUB.setText(String.format(Locale.US, "%.2f", getValue / currencyUAHtoRUB));
                        curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoUAH));
                    }break;
                    case 2:{
                        curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyUAHtoUSD));
                        curValUSD.setText(Double.toString(getValue));
                        curValEUR.setText(String.format(Locale.US, "%.2f", getValue * currencyUSDtoEUR));
                        curValRUB.setText(String.format(Locale.US, "%.2f", getValue * currencyUSDtoRUB));
                        curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoUSD));
                    }break;
                    case 3:{
                        curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyUAHtoEUR));
                        curValUSD.setText(String.format(Locale.US, "%.2f", getValue * currencyEURtoUSD));
                        curValEUR.setText(Double.toString(getValue));
                        curValRUB.setText(String.format(Locale.US, "%.2f", getValue * currencyEURtoRUB));
                        curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoEUR));
                    }break;
                    case 4:{
                        curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyUAHtoRUB));
                        curValUSD.setText(String.format(Locale.US, "%.2f", getValue / currencyUSDtoRUB));
                        curValEUR.setText(String.format(Locale.US, "%.2f", getValue / currencyEURtoRUB));
                        curValRUB.setText(Double.toString(getValue));
                        curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoRUB));
                    }break;
                    case 5:{
                        curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoUAH));
                        curValUSD.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoUSD));
                        curValEUR.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoEUR));
                        curValRUB.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoRUB));
                        curValGBP.setText(Double.toString(getValue));
                    }break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, currencyName);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double getValue;
                if(curVal.getText().length() == 0){
                    getValue = 0;
                }
                else {
                    getValue = Double.parseDouble(curVal.getText().toString());
                }
                if (position == 0){
                    curValUAH.setText(Double.toString(getValue));
                    curValUSD.setText(String.format(Locale.US, "%.2f", getValue / currencyUAHtoUSD));
                    curValEUR.setText(String.format(Locale.US, "%.2f", getValue / currencyUAHtoEUR));
                    curValRUB.setText(String.format(Locale.US, "%.2f", getValue / currencyUAHtoRUB));
                    curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoUAH));
                    flag = 1;
                }
                if (position == 1){
                    curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyUAHtoUSD));
                    curValUSD.setText(Double.toString(getValue));
                    curValEUR.setText(String.format(Locale.US, "%.2f", getValue * currencyUSDtoEUR));
                    curValRUB.setText(String.format(Locale.US, "%.2f", getValue * currencyUSDtoRUB));
                    curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoUSD));
                    flag = 2;
                }
                if (position == 2){
                    curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyUAHtoEUR));
                    curValUSD.setText(String.format(Locale.US, "%.2f", getValue * currencyEURtoUSD));
                    curValEUR.setText(Double.toString(getValue));
                    curValRUB.setText(String.format(Locale.US, "%.2f", getValue * currencyEURtoRUB));
                    curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoEUR));
                    flag = 3;
                }
                if (position == 3){
                    curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyUAHtoRUB));
                    curValUSD.setText(String.format(Locale.US, "%.2f", getValue / currencyUSDtoRUB));
                    curValEUR.setText(String.format(Locale.US, "%.2f", getValue / currencyEURtoRUB));
                    curValRUB.setText(Double.toString(getValue));
                    curValGBP.setText(String.format(Locale.US, "%.2f", getValue / currencyGBPtoRUB));
                    flag = 4;
                }
                if(position == 4){
                    curValUAH.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoUAH));
                    curValUSD.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoUSD));
                    curValEUR.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoEUR));
                    curValRUB.setText(String.format(Locale.US, "%.2f", getValue * currencyGBPtoRUB));
                    curValGBP.setText(Double.toString(getValue));
                    flag = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class getInfo extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Завантаження...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect("http://www.bank.gov.ua/control/uk/curmetal/detail/currency?period=daily").userAgent("Chrome").get();
                Elements elements = document.select("tr");
                String curTime[] = document.getElementsByClass("title_info").text().split(" ");
                currencyTime = curTime[4];
                for (Element src : elements){
                    String temp[] = src.getElementsByClass("cell_c").text().split(" ");
                    if (temp.length < 100){
                        for (int i = 0; i < temp.length; i++){
                            if(temp[i].equals("USD")){
                                currencyUAHtoUSD = Double.parseDouble(temp[i+2]);
                                currencyUAHtoUSD = currencyUAHtoUSD / 100;
                            }
                            else if(temp[i].equals("EUR")){
                                currencyUAHtoEUR = Double.parseDouble(temp[i+2]);
                                currencyUAHtoEUR = currencyUAHtoEUR / 100;
                            }
                            else if(temp[i].equals("RUB")){
                                currencyUAHtoRUB = Double.parseDouble(temp[i+2]);
                                currencyUAHtoRUB = currencyUAHtoRUB / 10;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            currencyGBPtoUAH = getCurrency("https://www.google.com/finance/converter?a=1&from=GBP&to=UAH");
            currencyGBPtoUSD = getCurrency("https://www.google.com/finance/converter?a=1&from=GBP&to=USD");
            currencyGBPtoEUR = getCurrency("https://www.google.com/finance/converter?a=1&from=GBP&to=EUR");
            currencyGBPtoRUB = getCurrency("https://www.google.com/finance/converter?a=1&from=GBP&to=RUB");
            if(currencyGBPtoUAH == 0 || currencyGBPtoUSD == 0 || currencyGBPtoEUR == 0 || currencyGBPtoRUB == 0){
                return false;
            }

            currencyUSDtoEUR = getCurrency("https://www.google.com/finance/converter?a=1&from=USD&to=EUR");
            currencyUSDtoRUB = getCurrency("https://www.google.com/finance/converter?a=1&from=USD&to=RUB");
            currencyEURtoUSD = getCurrency("https://www.google.com/finance/converter?a=1&from=EUR&to=USD");
            currencyEURtoRUB = getCurrency("https://www.google.com/finance/converter?a=1&from=EUR&to=RUB");
            if(currencyUSDtoEUR == 0 || currencyUSDtoRUB == 0 || currencyEURtoUSD == 0 || currencyEURtoRUB == 0){
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                progressDialog.dismiss();
                textViewCurTime.setText("Курс валюти станом на: " + currencyTime);
                textViewUSD.setText("USD: " + String.format(Locale.US, "%.2f", currencyUAHtoUSD));
                textViewEUR.setText("EUR: " + String.format(Locale.US, "%.2f", currencyUAHtoEUR));
                textViewRUB.setText("RUB: " + String.format(Locale.US, "%.2f", currencyUAHtoRUB));
                intoFile();
            }
            else {
                progressDialog.dismiss();
                showAlertDialog(MainActivity.this);
            }
        }
    }

    public void intoFile(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("infoCurrency",MODE_PRIVATE);
            fileOutputStream.write((Double.toString(currencyUAHtoUSD) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyUAHtoEUR) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyUAHtoRUB) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyUSDtoEUR) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyUSDtoRUB) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyEURtoUSD) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyEURtoRUB) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyGBPtoUAH) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyGBPtoUSD) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyGBPtoEUR) + " ").getBytes());
            fileOutputStream.write((Double.toString(currencyGBPtoRUB) + " ").getBytes());
            fileOutputStream.write((currencyTime + " ").getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(){
        try {
            FileInputStream fileInputStream = openFileInput("infoCurrency");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine())!=null){
                stringBuffer.append(lines);
            };

            String lastinfo[] = stringBuffer.toString().split(" ");
            currencyUAHtoUSD = Double.parseDouble(lastinfo[0]);
            currencyUAHtoEUR = Double.parseDouble(lastinfo[1]);
            currencyUAHtoRUB = Double.parseDouble(lastinfo[2]);
            currencyUSDtoEUR = Double.parseDouble(lastinfo[3]);
            currencyUSDtoRUB = Double.parseDouble(lastinfo[4]);
            currencyEURtoUSD = Double.parseDouble(lastinfo[5]);
            currencyEURtoRUB = Double.parseDouble(lastinfo[6]);
            currencyGBPtoUAH = Double.parseDouble(lastinfo[7]);
            currencyGBPtoUSD = Double.parseDouble(lastinfo[8]);
            currencyGBPtoEUR = Double.parseDouble(lastinfo[9]);
            currencyGBPtoRUB = Double.parseDouble(lastinfo[10]);
            currencyTime = lastinfo[11];

            textViewCurTime.setText("Курс валюти станом на: " + currencyTime);
            textViewUSD.setText("USD: " + String.format(Locale.US, "%.2f", currencyUAHtoUSD));
            textViewEUR.setText("EUR: " + String.format(Locale.US, "%.2f", currencyUAHtoEUR));
            textViewRUB.setText("RUB: " + String.format(Locale.US, "%.2f", currencyUAHtoRUB));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getCurrency(String url)  {
        double value = 0;
        try {
            Document doc = Jsoup.connect(url).userAgent("Chrome").get();
            Elements el = doc.select(".bld");
            String src[] = el.text().split(" ");
            value = Double.parseDouble(src[0]);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void showAlertDialog(Context context){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Помилка");
        alertDialog.setMessage("Неможливо завантажити інформацію, перезавантажити?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Так", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getInfo GI = new getInfo();
                GI.execute();
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Ні", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            getInfo GI = new getInfo();
            GI.execute();
        }
        return super.onOptionsItemSelected(item);
    }
}
