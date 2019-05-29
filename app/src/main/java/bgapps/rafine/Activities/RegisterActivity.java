package bgapps.rafine.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import bgapps.rafine.R;

public class RegisterActivity extends AppCompatActivity {

    ImageView imgUserPhoto;
    static int PReqCode = 1;
    static int REQUEST_CODE = 1;
    Uri pickedImgUri;

    private EditText userName, userMail, userPw, userPw2;
    private ProgressBar loadingProgress;
    private Button regBtn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.regName); // Cast işlemi
        userMail = (EditText) findViewById(R.id.regMail); // Cast işlemi
        userPw = (EditText) findViewById(R.id.regPassword); // Cast işlemi
        userPw2 = (EditText) findViewById(R.id.regPassword2); // Cast işlemi
        loadingProgress = (ProgressBar) findViewById(R.id.progressBar); // Cast işlemi
        regBtn = (Button) findViewById(R.id.regBtn); // Cast işlemi
        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String name = userName.getText().toString();
                final String mail = userMail.getText().toString();
                final String pw = userPw.getText().toString();
                final String pw2 = userPw2.getText().toString();

                if (name.isEmpty() || mail.isEmpty() || pw.isEmpty() || pw2.isEmpty() || !pw.equals(pw2)) {
                    showMessage("Lütfen tüm alanları doğru doldurun");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else {
                    CreateUserAccount(mail, name, pw);
                }
            }
        });

        //Kullanıcı fotoğraf ekleme
        imgUserPhoto = findViewById(R.id.regUserPhoto); // Cast işlemi
        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });
    }

    private void CreateUserAccount(final String mail, final String name, String pw) {
        mAuth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showMessage("Kayıt başarılı.");
                    updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());

                } else {
                    showMessage("Kayıt işlemi başarısız." + task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser){

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("kullaniciPP");
        final StorageReference imgFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imgFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Fotoğraf Firebase'e yüklendi.
                //Fotoğrafın URL'sini alıyoruz.
                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUptade = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUptade)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            showMessage("Kayıt başarılı.");
                                            UpdateUI();
                                        }
                                        else{
                                            showMessage("Kayıt işlemi başarısız.");
                                        }
                                    }
                                });

                    }
                });
            }
        });



    }

    private void UpdateUI() {
        Intent homeActivity = new Intent(getApplicationContext(),Home.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        // Kullanıcının profil fotoğrafını belirlemesi için galeriyi açacak.
        Intent galleryIntent = (new Intent(Intent.ACTION_GET_CONTENT));
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this, "Lütfen gerekli izinleri veriniz.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            // Kullanıcı profil fotoğrafını belirledi.
            // Fotoğraf referansını URI değişkeninde tutmalıyız.
            pickedImgUri = data.getData();
            imgUserPhoto.setImageURI(pickedImgUri);
            System.out.println("url1: "+pickedImgUri.toString());
        }
    }
}
