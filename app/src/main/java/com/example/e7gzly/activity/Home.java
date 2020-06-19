package com.example.e7gzly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e7gzly.R;
import com.example.e7gzly.dialog.ErrorConnectionDialog;
import com.example.e7gzly.fragment.MyTicketFragment;
import com.example.e7gzly.fragment.SearchFragment;
import com.example.e7gzly.fragment.SettingsFragment;
import com.example.e7gzly.model.CheckConnection;
import com.example.e7gzly.model.User;
import com.example.e7gzly.utilities.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // FireBase

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private TextView title;
    String userId;

    // Drawer
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private View viewHeader;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView nav_Drawer_Email, nav_Drawer_Name;
    private CircleImageView img_Drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
        }
        // Drawer

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_menu);
        title = findViewById(R.id.tv_title);
        viewHeader = navigationView.getHeaderView(0);
        nav_Drawer_Name = viewHeader.findViewById(R.id.tx_drawer_Name);
        nav_Drawer_Email = viewHeader.findViewById(R.id.tx_drawer_Email);
        img_Drawer = viewHeader.findViewById(R.id.Drawer_Img);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setItemIconTintList(null); // this code to change color icon image in navigation drawer to original color  //

        returnData();

        navigationView.setNavigationItemSelectedListener(this);


        loadFragment(new SearchFragment());
        title.setText("Search");


    }

    public void setActionBarTitle(String title) {
        this.title.setText(title);
    }

    public void loadFragment(Fragment Fragment) {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, Fragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();

    }

    /**
     * {@link Picasso#get().placeholder}  && {@link Picasso#get().error}
     * ده بيجيب الصوره من ال drawable في حالت ان حصل اي مشكله في ال URL اللي راجع
     */
    public void returnData() {

        databaseReference.child(Constants.USERS).child(Constants.getUID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                nav_Drawer_Email.setText(user.getEmail());
                nav_Drawer_Name.setText(user.getFullName());
                Picasso.get()
                        .load(user.getImgUrl())
                        .placeholder(R.drawable.default_pic_user)
                        .error(R.drawable.default_pic_user)
                        .into(img_Drawer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (CheckConnection.isConnected(this)) {

            switch (menuItem.getItemId()) {
                case R.id.get_ticket:
                    loadFragment(new SearchFragment());
                    title.setText("Search");
                    break;
                case R.id.ticket:
                    loadFragment(new MyTicketFragment());
                    title.setText("My Tickets");
                    break;
                case R.id.settings:
                    loadFragment(new SettingsFragment());
                    title.setText("Settings");
                    break;
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(Home.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

            }

            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            ErrorConnectionDialog dialog = new ErrorConnectionDialog(this);
            dialog.show();
            dialog.checkConnection();
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        returnData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        returnData();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            moveTaskToBack(true);
        }
    }

}
