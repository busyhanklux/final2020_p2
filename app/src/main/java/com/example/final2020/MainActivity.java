package com.example.final2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView t_temp,t_city,t_description,t_date;
    Button b1_decide;
    Spinner e1_target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t_temp = (TextView)findViewById(R.id.t1);
        t_city = (TextView)findViewById(R.id.t3);
        t_description = (TextView)findViewById(R.id.t4);
        t_date = (TextView)findViewById(R.id.t2);
        
        e1_target = (Spinner)findViewById(R.id.sp_city);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,R.array.tw_cities,android.R.layout.simple_spinner_dropdown_item);
        e1_target.setAdapter(adapter);

        find_weather(e1_target);
    }

    public void find_weather(View view) {

        b1_decide = (Button)findViewById(R.id.button);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ e1_target.getSelectedItem() +"&appid=a3687731d05eb6f4484ea5b83b087c09";
        Log.d("nameOfChty", String.valueOf(e1_target.getSelectedItem()));
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response){
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array        = response.getJSONArray("weather");
                    JSONObject object      = array.getJSONObject(0);
                    String temp            = String.valueOf(main_object.getDouble("temp"));
                    String description     = object.getString("description");
                    String city            = response.getString("name");

                    //t1_temp.setText(temp);
                    t_city.setText(city);//城市
                    t_description.setText(description);//天氣概況

                    Calendar calendar     = Calendar.getInstance();
                    SimpleDateFormat sdf  = new SimpleDateFormat("EEEE-MM-dd");//星期-月-日
                    String formatted_date = sdf.format(calendar.getTime());

                    t_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    //double centi    = (temp_int - 32) / 1.8000;
                    double centi    = temp_int - 273.15;//絕對溫度 轉 攝氏度
                    centi           = Math.round(centi);
                    int i           = (int)centi;
                    t_temp.setText(String.valueOf(i)+" °C");

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
}