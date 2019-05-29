package bgapps.rafine.Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;


import bgapps.rafine.Activities.LoginActivity;
import bgapps.rafine.R;

public class HomeFragment extends Fragment {

    private CardView cvFoodList, cvChooseFood, cvCalculator, cvMap, cvShare, cvSettings, cvLogOut;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cvFoodList = view.findViewById(R.id.cvFoodList);
        cvChooseFood = view.findViewById(R.id.cvChooseFood);
        cvCalculator = view.findViewById(R.id.cvCalculate);
        cvMap = view.findViewById(R.id.cvMap);
        cvShare = view.findViewById(R.id.cvShare);
        cvSettings = view.findViewById(R.id.cvSettings);
        cvLogOut = view.findViewById(R.id.cvLogOut);

        builtUI();

        return view;
    }

    private void builtUI() {
        cvFoodList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                ListFragment fragment = new ListFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        cvChooseFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooserFragment fragment = new ChooserFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        cvCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorFragment fragment = new CalculatorFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        cvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFragment fragment = new MapFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        cvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFragment fragment = new ShareFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment fragment = new SettingsFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        cvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent loginActivity = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });
    }
}


