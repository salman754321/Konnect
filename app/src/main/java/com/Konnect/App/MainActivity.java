package com.Konnect.App;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.Konnect.App.Fragment.HomeFragment;
import com.Konnect.App.Fragment.NotificationFragment;
import com.Konnect.App.Fragment.ProfileFragment;
import com.Konnect.App.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Fragment selectdFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        bottomNavigationView=findViewById( R.id.bottom_navigation );
        bottomNavigationView.setOnNavigationItemSelectedListener( navigatioItemSelectedListner );

        Bundle intent=getIntent().getExtras();
        if(intent!=null){
            String publisherid=intent.getString( "publisherid" );

            SharedPreferences.Editor editor=getSharedPreferences( "PREPS",MODE_PRIVATE ).edit();
            editor.putString( "profileid",publisherid );
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new ProfileFragment() ).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,new HomeFragment() ).commit();
        }


    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigatioItemSelectedListner=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.nav_home:
                    selectdFragment=new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectdFragment=new SearchFragment();
                    break;

                case R.id.nav_add:
                    selectdFragment=null;
                    startActivity( new Intent( MainActivity.this,PostActivity.class ) );
                    break;
                case R.id.nav_favourite:
                    selectdFragment=new NotificationFragment();
                    break;
                case R.id.nav_profile:
                    SharedPreferences.Editor editor=getSharedPreferences( "PRESPS",MODE_PRIVATE ).edit();
                    editor.putString( "ProfileId", FirebaseAuth.getInstance().getCurrentUser().getUid() );
                    editor.apply();
                    selectdFragment=new ProfileFragment();
                    break;
            }
            if(selectdFragment!=null)
            {
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,selectdFragment ).commit();
            }
            return true;
        }
    };
}
