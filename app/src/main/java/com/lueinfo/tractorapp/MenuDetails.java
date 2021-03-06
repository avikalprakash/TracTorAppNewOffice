package com.lueinfo.tractorapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuDetails extends AppCompatActivity {

    String myData,id;
    String htmlText;
    Button buynow;
    ImageView mitemimg;
    ProgressDialog pDialog;
    TextView mprodtxt,mnormalpricetxt,mdiscpricetxt,mdescriptiontxt;
    String item_id,item_name,price,special_price,description,image,calories,created_at,validupto,yy,mm,dd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

        mitemimg = findViewById(R.id.item_image);

        mprodtxt = findViewById(R.id.prod_name);
        mnormalpricetxt = findViewById(R.id.text3);
        mdiscpricetxt = findViewById(R.id.price);
        mdescriptiontxt = findViewById(R.id.description);

        id = getIntent().getStringExtra("id");
        Log.d("knlsDvjb"," "+id);

        buynow=(Button)findViewById(R.id.buynow);
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ConfirmPurchase.class));
            }
        });
//        htmlText = " %s ";
//        myData = "Roast chicken is chicken prepared as food by roasting whether in a home kitchen, over a fire, or with a professional rotisserie (rotary spit). Generally, the chicken is roasted with its own fat and juices by circulating the meat during roasting, and therefore, are usually cooked exposed to fire or heat with some type of rotary grill so that the circulation of these fats and juices is as efficient as possible.";
//        TextView description = (TextView) findViewById(R.id.description);
//        description.setText(String.format(htmlText, myData));

        populatedata();
    }

    public void populatedata() {

        pDialog = new ProgressDialog(MenuDetails.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        final String url = "http://condoassist2u.com/tractorapp/api/getItemDetails/"+id;

        final StringRequest movieReq = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rr000", response.toString());
                        hidePDialog();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean error=jsonObject.getBoolean("error");

                            if(!error)
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("message");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    item_id=jsonObject1.getString("promotion_id");
                                    item_name=jsonObject1.getString("item_name");
                                    price=jsonObject1.getString("price");
                                    special_price=jsonObject1.getString("special_price");
                                    description=jsonObject1.getString("description");
                                    image=jsonObject1.getString("image");
                                    calories=jsonObject1.getString("calories");
                                    created_at=jsonObject1.getString("created_at");

                                    String alphaOnly = description.replaceAll("&#39;","'");

                                    String alphaO = alphaOnly.replaceAll("&nbsp;"," ");

                                    String alpha = alphaO.replaceAll("&amp;","&");  //$euml;

                                    String alpha123 = alpha.replaceAll("&euml;","&");

                                    String alpha12345 = alpha123.replaceAll("&eacute;","e");

                                    mdescriptiontxt.setText(alpha12345);
                                    mprodtxt.setText(item_name);
                                    mdiscpricetxt.setText("RM "+special_price);
                                    mnormalpricetxt.setText("RM "+price);
                                    Picasso.with(getApplicationContext()).load(String.valueOf(image)).into(mitemimg);


                                }


                            }
                        } catch (JSONException e) {}


                    }

                    // notifying list adapter about data changes
                    // so that it renders the list view with updated data

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(MenuDetails.this, "You Have Some Connectivity Issue....", Toast.LENGTH_LONG).show();
                }
                hidePDialog();

            }
        });
        {
            RequestQueue requestQueue = Volley.newRequestQueue(MenuDetails.this);
            requestQueue.add(movieReq);
        }

    }
    @Override
    public void onDestroy () {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
