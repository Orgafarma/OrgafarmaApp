package client.com.br.orgafarma;

/**
 * Created by Felipe on 16/11/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.Toast;

import application.OrgafarmaApplication;
import client.com.br.orgafarma.Fragment.CotacaoFragment;
import client.com.br.orgafarma.Fragment.InboxFragment;
import client.com.br.orgafarma.Fragment.InboxVendaVendedorFragment;


public class ActivityPrincipal extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

        setFragment(0);
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_sub_item_1:
                                menuItem.setChecked(true);
                                setFragment(R.id.navigation_sub_item_1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.navigation_sub_item_2:
                                menuItem.setChecked(true);
                                setFragment(R.id.navigation_sub_item_2);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.navigation_cotacao:
                                menuItem.setChecked(true);
                                setFragment(R.id.navigation_cotacao);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                        }
                        return true;
                    }
                });
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (position) {
            case R.id.navigation_sub_item_1:
                InboxFragment inboxFragment = new InboxFragment();
                fragmentTransaction.replace(R.id.fragment, inboxFragment);
                fragmentTransaction.commit();
                break;
            case R.id.navigation_sub_item_2:
                InboxVendaVendedorFragment fragment = new InboxVendaVendedorFragment();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.navigation_cotacao:
                CotacaoFragment cotacaoFragment = new CotacaoFragment();
                fragmentTransaction.replace(R.id.fragment, cotacaoFragment);
                fragmentTransaction.commit();
                break;

        }
    }

    private void setConclurOn(){

    }
}

