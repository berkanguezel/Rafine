package berkanguzel.yemekim.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

import berkanguzel.yemekim.Adapter.RecyclerViewAdapter;
import berkanguzel.yemekim.Model.Foods;
import berkanguzel.yemekim.R;

public class CalculatorFragment extends Fragment {
    private String URLstring = "https://api.myjson.com/bins/bu5va";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<Foods> alFood;

    private RequestQueue mRequestQueue;
    private Button btnClear;
    private TextView tvTotalCal;
    private String name, cal;
    private int foodCal = 0;

    public CalculatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        tvTotalCal = view.findViewById(R.id.totalCal);
        btnClear = view.findViewById(R.id.clearCal);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodCal = 0;
                tvTotalCal.setText("Toplam Kalori: " + foodCal);
            }
        });

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        soupParse();


        return view;
    }


    private void soupParse() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URLstring, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray soupArray = response.getJSONArray("Soup");
                            JSONArray mdArray = response.getJSONArray("MainDish");
                            JSONArray sdArray = response.getJSONArray("SideDish");
                            JSONArray dArray = response.getJSONArray("Dessert");
                            alFood = new ArrayList<>();

                            for (int i = 0; i < soupArray.length(); i++) {
                                JSONObject hit = soupArray.getJSONObject(i);

                                name = hit.getString("soupName");
                                cal = hit.getString("soupCal");

                                alFood.add(new Foods(name, cal));
                            }
                            for (int i = 0; i < mdArray.length(); i++) {
                                JSONObject hit = mdArray.getJSONObject(i);

                                name = hit.getString("mdName");
                                cal = hit.getString("mpCal");

                                alFood.add(new Foods(name, cal));
                            }
                            for (int i = 0; i < sdArray.length(); i++) {
                                JSONObject hit = sdArray.getJSONObject(i);

                                name = hit.getString("sdName");
                                cal = hit.getString("sdCal");

                                alFood.add(new Foods(name, cal));
                            }
                            for (int i = 0; i < dArray.length(); i++) {
                                JSONObject hit = dArray.getJSONObject(i);

                                name = hit.getString("dName");
                                cal = hit.getString("dCal");

                                alFood.add(new Foods(name, cal));
                            }


                            mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), alFood);
                            mRecyclerView.setAdapter(mRecyclerViewAdapter);
                            mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    String sCal = alFood.get(position).getFoodCal();

                                    foodCal += Integer.parseInt(sCal);
                                    tvTotalCal.setText("Toplam Kalori: " + foodCal);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        mRequestQueue.add(request);
    }


}