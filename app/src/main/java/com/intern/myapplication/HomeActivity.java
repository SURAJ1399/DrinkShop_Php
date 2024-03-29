package com.intern.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidException;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.intern.myapplication.Adapter.CategoryAdapter;
import com.intern.myapplication.Model.Banner;
import com.intern.myapplication.Model.Category;
import com.intern.myapplication.Retrofit.IDrinkShopApi;
import com.intern.myapplication.Utils.Common;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView txt_name,txt_phone;
    ImageView image1;
    SliderLayout sliderLayout;

    IDrinkShopApi mServices;
    RecyclerView lst_menu;

    //RxJava
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lst_menu=findViewById(R.id.lst_menu);
       // lst_menu.setLayoutManager(new GridLayoutManager(this, 2,GridLayoutManager.HORIZONTAL,false));
        lst_menu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        lst_menu.setHasFixedSize(true);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mServices=Common.getAPI();
       sliderLayout=findViewById(R.id.slider);



        View headerview=navigationView.getHeaderView(0);
        txt_name=(TextView)headerview.findViewById(R.id.txt_name);
        txt_phone=(TextView)headerview.findViewById(R.id.txt_phone);

        //Set Info
        txt_phone.setText(Common.currentuser.getPhone());
        txt_name.setText(Common.currentuser.getName());

        //GetBanner
        getbannerImage();

        //Get menu;
        getMenu();
    }


    private void getbannerImage() {


        compositeDisposable.add(mServices.getBanners().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImages(banners);

                    }
                }));
    }



    private void getMenu() {

        compositeDisposable.add(mServices.getMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Category>>() {
                                                                         @Override
                                                                         public void accept(List<Category> categories) throws Exception {
                                                                             displayMenu(categories);
                                                                         }
                                                                     }
                ));}
    private void displayMenu(List<Category>categories) {
        CategoryAdapter categoryAdapter=new CategoryAdapter(this,categories);
        lst_menu.setAdapter(categoryAdapter);
    }



    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();

    }

    private void displayImages(List<Banner> banners) {
        HashMap<String,String> bannerMap=new HashMap<>();
        for (Banner item:banners)
        bannerMap.put(item.getName(),item.getLink());
        for(String name:bannerMap.keySet())
        {
            TextSliderView textSliderView=new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
           // Toast.makeText(this, "ID:"+bannerMap.get(name), Toast.LENGTH_SHORT).show();



            sliderLayout.addSlider(textSliderView);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

