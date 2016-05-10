package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity implements KitchenFragment.CallbackOne {
    public static final String TAG = "myLogTag";

    private Toolbar mToolbar;
    private Drawer drawer;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = MenuFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .commit();

        }

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.menu).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.kitchen_title).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.information_title).withEnabled(false)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d(TAG, "position clicked: " + position);
                        Fragment fragment = MenuFragment.newInstance();
                        switch (position) {
                            case 1:
                                fragment = MenuFragment.newInstance();
                                break;
                            case 2:
                                fragment = KitchenFragment.newInstance();
                                break;
                            default:
                                fragment = new Fragment();
                        }
                        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        return false;
                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View view) {
                        Log.d(TAG, "CLICK");
                        if (!drawer.getActionBarDrawerToggle().isDrawerIndicatorEnabled()) {
                            onBackPressed();
                            return true;
                        } else
                            return false;
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "CLCK!");
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen())
            drawer.closeDrawer();
        else if (getFragmentManager().getBackStackEntryCount() == 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawer.getActionBarDrawerToggle().syncState();
            getFragmentManager().popBackStack();
        } else if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void setFirstSelected() {
        drawer.setSelection(1);
    }
}
