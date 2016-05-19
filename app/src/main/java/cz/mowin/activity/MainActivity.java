package cz.mowin.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import cz.mowin.AppController;
import cz.mowin.R;
import cz.mowin.communication.Api;
import cz.mowin.fragment.DefaultFragment;
import cz.mowin.fragment.DiskFragment;
import cz.mowin.fragment.InactiveFragment;
import cz.mowin.fragment.InfoFragment;
import cz.mowin.fragment.ProcessFragment;
import cz.mowin.fragment.ProcessesFragment;
import cz.mowin.fragment.ServiceFragment;
import cz.mowin.fragment.ServicesFragment;
import cz.mowin.fragment.UserCreateFragment;
import cz.mowin.fragment.UserFragment;
import cz.mowin.fragment.UsersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DefaultFragment.OnFragmentInteractionListener {

    private String TAG = "MainActivity";
    public Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        api = Api.getInstance(this);

        changeFragment(new DefaultFragment());
    }

    protected void changeFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    protected void changeFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    public void onClickItemServices(View v) {
        final TextView tw = (TextView) v.findViewById(R.id.tw_services_item_name);
        Bundle bundle = new Bundle();
        bundle.putString("service_name", String.valueOf(tw.getText()));
        changeFragment(new ServiceFragment(), bundle);
    }

    public void onClickItemProcesses(View v) {
        final TextView tw = (TextView) v.findViewById(R.id.tw_processes_item_id);
        Bundle bundle = new Bundle();
        bundle.putInt("process_id", Integer.valueOf((String) tw.getText()));
        changeFragment(new ProcessFragment(), bundle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    private void exit() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.exit_confirm))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppController.invalidateToken();
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }

    private void logout() {
        final Intent intent = new Intent(this, LoginActivity.class);
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.logout_confirm))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppController.invalidateToken();
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final TextView twAdmin = (TextView) findViewById(R.id.tw_menu_admin);
        if (twAdmin != null)
            twAdmin.setText(AppController.load("login"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_exit) {
            exit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_disk) {
            changeFragment(new DiskFragment());
        } else if (id == R.id.nav_user_create) {
            changeFragment(new UserCreateFragment());
        } else if (id == R.id.nav_info) {
            changeFragment(new InfoFragment());
        } else if (id == R.id.nav_user_get) {
            changeFragment(new UserFragment());
        } else if (id == R.id.nav_users_get) {
            changeFragment(new UsersFragment());
        } else if (id == R.id.nav_services) {
            changeFragment(new ServicesFragment());
        } else if (id == R.id.nav_processes) {
            changeFragment(new ProcessesFragment());
        } else if (id == R.id.nav_inactive) {
            changeFragment(new InactiveFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "resumed");
        api.setActivityContext(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.e(TAG, uri.toString());
    }
}
