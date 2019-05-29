package bgapps.rafine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import bgapps.rafine.Fragments.CalculatorFragment;
import bgapps.rafine.Fragments.ChooserFragment;
import bgapps.rafine.Fragments.HomeFragment;
import bgapps.rafine.Fragments.MapFragment;
import bgapps.rafine.Fragments.ListFragment;
import bgapps.rafine.Fragments.SettingsFragment;
import bgapps.rafine.Fragments.ShareFragment;
import bgapps.rafine.R;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Ana Sayfa");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();
        showHome();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragment instanceof HomeFragment){
                super.onBackPressed();
            }else{
                showHome();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHome() {
        fragment = new HomeFragment();
        getSupportActionBar().setTitle("Ana Sayfa");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    Fragment fragment = null;


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            getSupportActionBar().setTitle("Ana Sayfa");
           fragment = new HomeFragment();
        } else if (id == R.id.nav_foodlist) {
            getSupportActionBar().setTitle("Yemek Listesi");
            fragment = new ListFragment();

        } else if (id == R.id.nav_chooser) {
            getSupportActionBar().setTitle("Yemeğini Seç");
            fragment = new ChooserFragment();

        } else if (id == R.id.nav_calculator) {
            getSupportActionBar().setTitle("Kalori Hesapla");
            fragment = new CalculatorFragment();

        } else if (id == R.id.nav_map) {
            getSupportActionBar().setTitle("Yemekhane Nerede");
            fragment = new MapFragment();

        } else if (id == R.id.nav_share) {
            getSupportActionBar().setTitle("Paylaş");
            fragment = new ShareFragment();

        } else if (id == R.id.nav_settings) {
            getSupportActionBar().setTitle("Ayarlar");
            fragment = new SettingsFragment();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();

        }

        if(fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container, fragment, fragment.getTag()).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUsermail = headerView.findViewById(R.id.nav_usermail);
        ImageView navUserphoto = headerView.findViewById(R.id.nav_userphoto);

        navUsermail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        // System.out.println("url: " + currentUser.getPhotoUrl().toString());

        Picasso.get()
                .load(currentUser.getPhotoUrl())
                .into(navUserphoto);


    }
}
