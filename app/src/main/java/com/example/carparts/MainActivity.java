package com.example.carparts;

import android.content.Intent;
import android.os.Bundle;

import com.example.carparts.ui.cart.Cart;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*findViewById(R.id.bt_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Cart.class));
            }
        });*/


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem nav_login = menu.findItem(R.id.nav_login);
        MenuItem nav_myorders = menu.findItem(R.id.nav_my_orders);
        MenuItem nav_addanouncement = menu.findItem(R.id.nav_add_announcement);
        MenuItem nav_myanouncement = menu.findItem(R.id.nav_my_announcement);
        View header = navigationView.getHeaderView(0);
        TextView textView= (TextView) header.findViewById(R.id.textView);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //textView.setText("Witaj "+SharedPrefManager.getUserName());
            textView.setText("Hi "+ SharedPrefManager.getUserName());
            nav_login.setTitle("My account");
            nav_myorders.setVisible(true);
            nav_addanouncement.setVisible(true);
            nav_myanouncement.setVisible(true);

        } else {
            textView.setText("You are not logged in");
            nav_myorders.setVisible(false);
            nav_addanouncement.setVisible(false);
            nav_myanouncement.setVisible(false);


        }

       /* final Fragment[] fragment = {null};
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                String title = getString(R.string.app_name);
                switch (id) {
                    case R.id.nav_home: {
                        fragment[0] = new HomeFragment();
                        title = "Home";
                        break;
                    }
                    case R.id.nav_add_announcement: {
                        fragment[0] = new AddAnnouncementFragment();
                        title = "Add announcement";
                        break;
                    }

                    case R.id.nav_all_announcement: {
                        fragment[0] = new AllAnnouncementFragment();
                        title = "All announcement";
                        break;
                    }

                    case R.id.nav_category: {
                        fragment[0] = new CategoryFragment();
                        title = "Category";
                        break;
                    }
                    case R.id.nav_contact: {
                        fragment[0] = new ContactFragment();
                        title = "Contact";
                        break;
                    }
                    case R.id.nav_login: {
                        title = "Login/Register";
                        fragment[0] = new LoginFragment();
                        break;
                    }
                    case R.id.nav_contact: {
                        fragment[0] = new Contact();
                        title = "Contact";
                        break;
                    }
                    case R.id.nav_login: {
                        title = "";
                        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(myIntent);
                        break;
                    }

                    default:
                        return true;
                }
                if (fragment[0] != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, fragment[0]);
                    ft.commit();
                }
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_category,
                R.id.nav_my_orders, R.id.nav_all_announcement, R.id.nav_add_announcement, R.id.nav_my_announcement,R.id.nav_contact)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.bt_cart) {
            Intent intent1 = new Intent(this, Cart.class);
            this.startActivity(intent1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
