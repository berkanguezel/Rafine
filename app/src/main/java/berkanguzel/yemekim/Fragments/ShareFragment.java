package berkanguzel.yemekim.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import berkanguzel.yemekim.R;


public class ShareFragment extends Fragment {
    private Button shareBtn;


    public ShareFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        shareBtn = view.findViewById(R.id.btnShare);

        shareIT();
        return view;
    }

    private void shareIT() {
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("Rafine");
                String body = "PAYLAŞ";
                String sub = "Arkadaşlarınında hemen öğrenmesini sağla!";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, body);
                myIntent.putExtra(Intent.EXTRA_TEXT, sub);
                startActivity(Intent.createChooser(myIntent, "Uygulamayı Paylaş"));
            }
        });
    }
}