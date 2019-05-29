package berkanguzel.yemekim.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import berkanguzel.yemekim.Activities.SuccessActivity;
import berkanguzel.yemekim.Model.Foods;
import berkanguzel.yemekim.R;

public class ChooserFragment extends Fragment {
    private String URLstring = "https://api.myjson.com/bins/bu5va";
    private Spinner spSoup, spMain, spSide, spDessert;
    private Button btnConfirm;
    TextView tvSoup, tvMain, tvSide, tvDessert;
    String textSoup, textMain, textSide, textDessert, strDate;
    int getSoupCount, setSoupCount, getMainCount, setMainCount,
            getSideCount, getDessertCount, setSideCount, setDessertCount, nowDate, lastDate, spDate, getSpDate;

    private ArrayList<Foods> soupArrayList;
    private ArrayList<Foods> mdArrayList;
    private ArrayList<Foods> sdArrayList;
    private ArrayList<Foods> dArrayList;
    private ArrayList<String> sNames = new ArrayList<String>();
    private ArrayList<String> mdNames = new ArrayList<String>();
    private ArrayList<String> sdNames = new ArrayList<String>();
    private ArrayList<String> dNames = new ArrayList<String>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    public ChooserFragment() {        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooser, container, false);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        strDate = sdf.format(calendar.getTime());
        nowDate = Integer.parseInt(strDate);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        getSpDate = sharedPreferences.getInt("lastTime", nowDate);

        spSoup = view.findViewById(R.id.soupSpinner);
        spSoup.setPrompt("Çorba Seçiniz.");
        spMain = view.findViewById(R.id.maindishSpinner);
        spMain.setPrompt("Ana Yemek Seçiniz.");
        spSide = view.findViewById(R.id.sidedishSpinner);
        spSide.setPrompt("Yardımcı Yemek Seçiniz.");
        spDessert = view.findViewById(R.id.dessertSpinner);
        spDessert.setPrompt("Tatlı&Meyve&Salata Seçiniz.");
        tvSoup = view.findViewById(R.id.tvSoup);
        tvMain = view.findViewById(R.id.tvMain);
        tvSide = view.findViewById(R.id.tvSide);
        tvDessert = view.findViewById(R.id.tvDessert);

        soupParse();
        mdParse();
        sdParse();
        dParse();

        btnConfirm = view.findViewById(R.id.confirmButton);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textSoup = spSoup.getSelectedItem().toString();
                textMain = spMain.getSelectedItem().toString();
                textSide = spSide.getSelectedItem().toString();
                textDessert = spDessert.getSelectedItem().toString();
                getData();
                if (getSpDate == nowDate) {
                    showDialog();
                } else {
                    Toast.makeText(getActivity(), "Aylık seçim hakkınızı doldurdunuz.", Toast.LENGTH_LONG).show();
                    btnConfirm.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getActivity(), SuccessActivity.class);
                    startActivity(intent);
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        return view;
    }




    private String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    private void soupParse() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = fixEncoding(response);
                        Log.d("qweResponse", ">>" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if (obj.optString("status").equals("true"))
                            {
                                soupArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("Soup");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    Foods foodModel = new Foods();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    foodModel.setFoodName(dataobj.getString("soupName"));
                                    soupArrayList.add(foodModel);
                                }
                                for (int i = 0; i < soupArrayList.size(); i++) {
                                    sNames.add(soupArrayList.get(i).getFoodName().toString())
                                    ;
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, sNames);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dialog);
                                spSoup.setAdapter(spinnerArrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("qweJSON", ">>", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void mdParse() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = fixEncoding(response);
                        Log.d("qweResponse", ">>" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if (obj.optString("status").equals("true"))
                            {
                                mdArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("MainDish");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    Foods foodModel = new Foods();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    foodModel.setFoodName(dataobj.getString("mdName"));
                                    mdArrayList.add(foodModel);
                                }
                                for (int i = 0; i < mdArrayList.size(); i++) {
                                    mdNames.add(mdArrayList.get(i).getFoodName().toString());
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, mdNames);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dialog);
                                spMain.setAdapter(spinnerArrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("qweJSON", ">>", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void sdParse() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = fixEncoding(response);
                        Log.d("qweResponse", ">>" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if (obj.optString("status").equals("true"))
                            {
                                sdArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("SideDish");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    Foods foodModel = new Foods();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    foodModel.setFoodName(dataobj.getString("sdName"));
                                    sdArrayList.add(foodModel);
                                }
                                for (int i = 0; i < sdArrayList.size(); i++) {
                                    sdNames.add(sdArrayList.get(i).getFoodName().toString());
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, sdNames);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dialog);
                                spSide.setAdapter(spinnerArrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("qweJSON", ">>", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void dParse() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = fixEncoding(response);
                        Log.d("qweResponse", ">>" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if (obj.optString("status").equals("true"))
                            {
                                dArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("Dessert");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    Foods foodModel = new Foods();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    foodModel.setFoodName(dataobj.getString("dName"));
                                    dArrayList.add(foodModel);
                                }
                                for (int i = 0; i < dArrayList.size(); i++) {
                                    dNames.add(dArrayList.get(i).getFoodName().toString());
                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, dNames);
                                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dialog);
                                spDessert.setAdapter(spinnerArrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("qweJSON", ">>", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void showDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Yemek Seçiminizi Onaylıyor Musunuz?")
                .setMessage(textSoup + "\n" + textMain + "\n" + textSide + "\n" + textDessert)
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        setSoupCount = getSoupCount + 1;
                        setMainCount = getMainCount + 1;
                        setSideCount = getSideCount + 1;
                        setDessertCount = getDessertCount + 1;

                        myRef.child("Foods").child("Soup").child(textSoup).child("count").setValue(setSoupCount);
                        myRef.child("Foods").child("MainDish").child(textMain).child("count").setValue(setMainCount);
                        myRef.child("Foods").child("SideDish").child(textSide).child("count").setValue(setSideCount);
                        myRef.child("Foods").child("Dessert").child(textDessert).child("count").setValue(setDessertCount);
                        Toast.makeText(getActivity(), "Başarılı", Toast.LENGTH_SHORT).show();

                        Calendar calendar1 = Calendar.getInstance();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                        String strDate1 = sdf1.format(calendar1.getTime());
                        lastDate = Integer.parseInt(strDate1);
                        spDate = lastDate + 1;

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("lastTime", spDate);
                        editor.commit();


                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newRef = database.getReference();
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getSoupCount = dataSnapshot.child("Foods").child("Soup").child(textSoup).child("count").getValue(Integer.class);
                getMainCount = dataSnapshot.child("Foods").child("MainDish").child(textMain).child("count").getValue(Integer.class);
                getSideCount = dataSnapshot.child("Foods").child("SideDish").child(textSide).child("count").getValue(Integer.class);
                getDessertCount = dataSnapshot.child("Foods").child("Dessert").child(textDessert).child("count").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}











