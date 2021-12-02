package com.example.youtubepostvila.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.youtubepostvila.Fragments.HomeFragment;
import com.example.youtubepostvila.Fragments.UploadFragment;
import com.example.youtubepostvila.Fragments.UserVideoFragment;
import com.example.youtubepostvila.R;
import com.example.youtubepostvila.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //View view = binding.getRoot();
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("Video");

        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment());
        //binding.bottomNav.setSelectedItemId(R.id.homePage);
    }
//    HomeFragment firstFragment = new HomeFragment();
//    UploadFragment secondFragment = new UploadFragment();
//    UserVideoFragment thirdFragment = new UserVideoFragment();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.top_left_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settings){
//            Intent intent = new Intent(this, SettingsActivity.class);
//            startActivity(intent);
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();


        }
        else if(item.getItemId()==R.id.logOut){
            mAuth.signOut();
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Log Out Successful", Toast.LENGTH_SHORT).show();
            //TODO
            // Deleting firebase instance on logout
        }
        else if(item.getItemId()==R.id.search_video){
            Toast.makeText(this, "Yet to be Implemented", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment= null;
        switch (item.getItemId()) {
            case R.id.homePage:
                fragment = new HomeFragment();
                break;

            case R.id.upload:
                fragment=new UploadFragment();
                break;

            case R.id.myVideo:
                fragment=new UserVideoFragment();
                break;
        }
        if(fragment!=null)
            loadFragment(fragment);
         return true;
    }
    void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.relativeLayout, fragment).commit();

    }
    private void videoSearch (String searchText){
        String query=searchText.toLowerCase();
        Query firebaseQuery=databaseReference.orderByChild("search").startAt(query).endAt(query+"\uf8ff");

    }
}
