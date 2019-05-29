package berkanguzel.yemekim.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import berkanguzel.yemekim.R;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail, userPw;
    private Button btnLogin, btnReg;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    //private Intent HomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.loginMail); // Cast işlemi
        userPw = findViewById(R.id.loginPassword); // Cast işlemi
        btnLogin = findViewById(R.id.btnLogin); // Cast işlemi
        btnReg = findViewById(R.id.btnReg);
        loginProgress = findViewById(R.id.loginProgress); // Cast işlemi
        mAuth = FirebaseAuth.getInstance();
        //HomeActivity = new Intent(this, Home.class);

        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String pw = userPw.getText().toString();

                if (mail.isEmpty() || pw.isEmpty()) {
                    showMessage("Boş alanları doldurunuz.");
                } else {
                    signIn(mail, pw);
                }
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void signIn(String mail, String pw) {
        mAuth.signInWithEmailAndPassword(mail, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                } else {
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(),Home.class);
        startActivity(homeActivity);
        finish();
    }

    private void register() {
        Intent reg = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(reg);
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //Kullanıcı oturum açmış olduğu için HomeActivity'e yönlendirilmeli.
            updateUI();
        }
    }
}
