package com.example.seyaha;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    ViewPager viewPager;
    List <String> imageUrls;
    List<Place> mPlace;
    viewPagerAdapter adapter;
    double latitude,longitude;
    String placeName;

    private Toolbar mToolbar;
    private TextView mTextView;


     ScrollView scrollView;
    SupportMapFragment mapFragment;
    FrameLayout seasonFlip,timeToGoFlip,estimationFlip,ageFlip;
    TextView seasonTv,timeToGoTv,ageTv1,ageTv2,estimationTv,costTv,tempTv,airQualityTv,internetTv,placeNameInfo, placeNameRecommendations,placeNameLocation,description,placeNameTitle;
    RoundCornerProgressBar costProgressBar,tempProgressBar,airQualityProgressBar,internetProgressBar;
    View frontLayoutSeason,backLayoutSeason,frontLayoutTime,backLayouTime,frontLayoutAge,backLayoutAge,frontLayoutEstimated,backLayoutEstimated;
    ImageView seasonImg,timeToGoImg,estimationImg;

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean[] mIsBackVisible = {false,false,false,false};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        mToolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        mTextView = findViewById(R.id.toolbar_title);
        mTextView.setText(R.string.detailed_activity_title);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        //text views and description deceleration
        placeNameRecommendations =findViewById(R.id.place_name_recommendations);
        placeNameInfo=findViewById(R.id.place_name_information_about);
        placeNameLocation=findViewById(R.id.place_name_location);
        description=findViewById(R.id.description_tv);
        scrollView=findViewById(R.id.scrollview);
        placeNameTitle=findViewById(R.id.place_name_title);

        //information about the place deceleration
        costProgressBar=findViewById(R.id.cost_progress);
        tempProgressBar=findViewById(R.id.temp_progress);
        airQualityProgressBar=findViewById(R.id.air_quality_progress);
        internetProgressBar=findViewById(R.id.internet_progress);
        costTv=findViewById(R.id.cost_tv);
        tempTv=findViewById(R.id.temp_tv);
        airQualityTv=findViewById(R.id.air_quality_tv);
        internetTv=findViewById(R.id.internet_tv);
        //recommendations about the place deceleration
        seasonFlip=findViewById(R.id.season_btn);
        timeToGoFlip=findViewById(R.id.time_btn);
        ageFlip=findViewById(R.id.age_btn);
        estimationFlip=findViewById(R.id.estimated_btn);


        loadAnimations();



        Intent i=getIntent();
        mPlace=(List<Place>)i.getSerializableExtra("places");


        description.setMovementMethod(new ScrollingMovementMethod());

        latitude=mPlace.get(0).latitude;
        longitude=mPlace.get(0).longitude;
        placeName=mPlace.get(0).nameEN;

        mapFragment.getMapAsync(this);

        run_viewPager();

        setCostProgress(mPlace.get(0).cost);
        setTempProgress("40");
        setAirQualityProgress("20");
        setInternetProgress(mPlace.get(0).internet);

        setSeason(mPlace.get(0).recommendedSeason);
        setTimeToGo(mPlace.get(0).recommendedTime);
        setAge(mPlace.get(0).recommendedAge);
        setEstimatedTime(mPlace.get(0).estimatedTime);

        if(SplashScreenActivity.lan.equalsIgnoreCase("ar"))
        {
            description.setText(mPlace.get(0).descAR);
            placeNameRecommendations.setText(mPlace.get(0).nameAR);
            placeNameInfo.setText(mPlace.get(0).nameAR);
            placeNameLocation.setText(mPlace.get(0).nameAR);
            placeNameTitle.setText(mPlace.get(0).nameAR);

        }
        else
        {
            placeNameTitle.setText(mPlace.get(0).nameEN);
            description.setText(mPlace.get(0).descEN);
            placeNameRecommendations.setText(mPlace.get(0).nameEN);
            placeNameInfo.setText(mPlace.get(0).nameEN);
            placeNameLocation.setText(mPlace.get(0).nameEN);
        }


        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                description.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });

        description.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                description.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

    }

    private void run_viewPager() {
        imageUrls = new ArrayList <>();

        for(int i=0;i<mPlace.size();i++)
        {
            imageUrls.add(mPlace.get(i).imageURL);
        }

        adapter = new viewPagerAdapter(imageUrls, this);
        viewPager = findViewById(R.id.ViewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0, 100, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                //System.out.println(positionOffset);
            }

            @Override
            public void onPageSelected(int position)
            {
                setCostProgress(mPlace.get(position).cost);
                setTempProgress("40");
                setAirQualityProgress("20");
                setInternetProgress(mPlace.get(position).internet);

                setSeason(mPlace.get(position).recommendedSeason);
                setTimeToGo(mPlace.get(position).recommendedTime);
                setAge(mPlace.get(position).recommendedAge);
                setEstimatedTime(mPlace.get(position).estimatedTime);
                if(SplashScreenActivity.lan.equalsIgnoreCase("ar"))
                {
                    description.setText(mPlace.get(position).descAR);
                    placeNameRecommendations.setText(mPlace.get(position).nameAR);
                    placeNameInfo.setText(mPlace.get(position).nameAR);
                    placeNameLocation.setText(mPlace.get(position).nameAR);
                    placeNameTitle.setText(mPlace.get(position).nameAR);
                }
                else
                {
                    description.setText(mPlace.get(position).descEN);
                    placeNameRecommendations.setText(mPlace.get(position).nameEN);
                    placeNameInfo.setText(mPlace.get(position).nameEN);
                    placeNameLocation.setText(mPlace.get(position).nameEN);
                    placeNameTitle.setText(mPlace.get(position).nameEN);
                }

                latitude=mPlace.get(position).latitude;
                longitude=mPlace.get(position).longitude;
                placeName=mPlace.get(position).nameEN;
                mapFragment.getMapAsync(DetailedActivity.this);


            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                System.out.println(state);

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng googleMapPlace = new LatLng(latitude, longitude);
        MarkerOptions my_own_marker = new MarkerOptions().position(googleMapPlace).title(placeName);
        my_own_marker.icon((getBitmapDescriptor(R.drawable.ic_gps)));
        mMap.addMarker(my_own_marker);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("third circle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMapPlace, 16.0f));
       // mMap.animateCamera(CameraUpdateFactory.zoomIn());
        //mMap.animateCamera(CameraUpdateFactory.zoomOut());

    }

    private BitmapDescriptor getBitmapDescriptor(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setCostProgress(int cost)
    {

        costTv.setText(cost+" "+getResources().getString(R.string.JD));
        if(cost<=20)
        {
            costProgressBar.setProgress(100);
            costProgressBar.setProgressColor(Color.GREEN);
        }
        else if(cost>20 && cost<=60)
        {
            costProgressBar.setProgress(60);
            costProgressBar.setProgressColor(Color.rgb(255,165,0));
        }
        else
        {
            costProgressBar.setProgress(30);
            costProgressBar.setProgressColor(Color.RED);
        }
        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofFloat(costProgressBar, "progress", 0.0f,costProgressBar.getProgress());
        progressAnimator.setDuration(1000);
        progressAnimator.setStartDelay(300);
        progressAnimator.start();

    }

    private void setTempProgress(String temp)
    {
        int result=Integer.parseInt(temp);

        if(result<=10)
        {
            tempProgressBar.setProgressColor(Color.RED);
            tempProgressBar.setProgress(result);
            tempTv.setText(getResources().getString(R.string.cold)+temp+"\u2103"+"(now)");

        }
        else  if(result>10 && result<=18)
        {
            tempTv.setText(getResources().getString(R.string.normal)+temp+"\u2103"+"(now)");
            tempProgressBar.setProgress(result);
            tempProgressBar.setProgressColor(Color.rgb(255,165,0));
        }
        else if(result>18 && result<=30)
        {
            tempProgressBar.setProgressColor(Color.GREEN);
            tempProgressBar.setProgress(55);
            tempTv.setText(getResources().getString(R.string.perfect)+temp+"\u2103"+"(now)");
        }
        else
        {
            tempProgressBar.setProgressColor(Color.RED);
            tempProgressBar.setProgress(55-result);
            tempTv.setText(getResources().getString(R.string.hot)+temp+"\u2103"+"(now)");
        }

        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofFloat(tempProgressBar, "progress", 0.0f,tempProgressBar.getProgress());
        progressAnimator.setDuration(1000);
        progressAnimator.setStartDelay(300);
        progressAnimator.start();

    }

    private void setAirQualityProgress(String airQuality)
    {
        airQualityTv.setText(airQuality+"\u00B5"+"g/m3"+"(now)");
        int result=Integer.parseInt(airQuality);
        airQualityProgressBar.setProgress(100-result);
        if(result<=25)
        {
            airQualityProgressBar.setProgressColor(Color.GREEN);
        }
        else if(result>25 && result<=50)
        {
            airQualityProgressBar.setProgressColor( Color.rgb(255,165,0));
        }
        else
        {
         airQualityProgressBar.setProgressColor(Color.RED);
        }
        ObjectAnimator progressAnimator;
        progressAnimator = ObjectAnimator.ofFloat(airQualityProgressBar, "progress", 0.0f,airQualityProgressBar.getProgress());
        progressAnimator.setDuration(1000);
        progressAnimator.setStartDelay(300);
        progressAnimator.start();
    }

    private void setInternetProgress(int internet)
    {
        ObjectAnimator progressAnimator;

        switch (internet)
        {
            case 0:
                internetProgressBar.setProgressColor(Color.RED);
                internetProgressBar.setProgress(30);
                internetTv.setText(getResources().getString(R.string.bad_internet));
                break;
            case 1:
                internetProgressBar.setProgressColor(Color.rgb(255,165,0));
                internetProgressBar.setProgress(60);
                internetTv.setText(getResources().getString(R.string.good_internet));
                break;
            case 2:
                internetProgressBar.setProgressColor(Color.GREEN);
                internetProgressBar.setProgress(100);
                internetTv.setText(getResources().getString(R.string.great_internet));
                break;
        }

        progressAnimator = ObjectAnimator.ofFloat(internetProgressBar, "progress", 0.0f,internetProgressBar.getProgress());
        progressAnimator.setDuration(1000);
        progressAnimator.setStartDelay(300);
        progressAnimator.start();
    }

   private void setSeason(int season)
    {

        frontLayoutSeason=seasonFlip.findViewById(R.id.front_season);
        backLayoutSeason=seasonFlip.findViewById(R.id.back_season);
        seasonTv=backLayoutSeason.findViewById(R.id.back_text);
        seasonImg=frontLayoutSeason.findViewById(R.id.front_icon);
        changeCameraDistance(frontLayoutSeason,backLayoutSeason);

        seasonFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(frontLayoutSeason,backLayoutSeason, mIsBackVisible,0);
            }
        });
       switch (season)
       {
           case 0:
             seasonTv.setText(getResources().getString(R.string.summer_season));
             seasonImg.setImageResource(R.drawable.ic_summer);
           break;

           case 1:
               seasonTv.setText(getResources().getString(R.string.winter_season));
               seasonImg.setImageResource(R.drawable.ic_winter);
           break;

           case 2:
               seasonTv.setText(getResources().getString(R.string.spring_season));
               seasonImg.setImageResource(R.drawable.ic_spring);
           break;

           case 3:
               seasonTv.setText(getResources().getString(R.string.autumn_season));
               seasonImg.setImageResource(R.drawable.ic_autumn);
           break;
       }
    }

    private  void setTimeToGo(int time)
    {
        frontLayoutTime=findViewById(R.id.front_time);
        backLayouTime=findViewById(R.id.back_time);
        timeToGoTv=backLayouTime.findViewById(R.id.back_text);
        timeToGoImg=frontLayoutTime.findViewById(R.id.front_icon);
        changeCameraDistance(frontLayoutTime,backLayouTime);
        timeToGoFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(frontLayoutTime,backLayouTime, mIsBackVisible,1);
            }
        });

        switch (time)
        {
            case 0:
                timeToGoTv.setText(getResources().getString(R.string.day_time));
                timeToGoImg.setImageResource(R.drawable.ic_day);
                break;

            case 1:
                timeToGoTv.setText(getResources().getString(R.string.night_time));
               timeToGoImg.setImageResource(R.drawable.ic_night);
                break;
        }
    }

    private void setAge(String age)
    {
        frontLayoutAge=findViewById(R.id.front_ag);
        backLayoutAge=findViewById(R.id.back_age);
        ageTv1=backLayoutAge.findViewById(R.id.back_text);
        ageTv2=frontLayoutAge.findViewById(R.id.back_text);
        ageTv2.setTextSize(20);
        ageTv1.setTextSize(20);
        ageTv1.setText(age);
        ageTv2.setText(age);

        changeCameraDistance(frontLayoutAge,backLayoutAge);
        ageFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(frontLayoutAge,backLayoutAge, mIsBackVisible,2);
            }
        });
    }

    private void setEstimatedTime(int estimatedTime)
    {
        frontLayoutEstimated=findViewById(R.id.front_estimated);
        backLayoutEstimated=findViewById(R.id.back_estimated);
        estimationTv=backLayoutEstimated.findViewById(R.id.back_text);
        estimationImg=frontLayoutEstimated.findViewById(R.id.front_icon);
        estimationTv.setText(estimatedTime+"Hrs");
        estimationImg.setImageResource(R.drawable.ic_sand_clock);

        changeCameraDistance(frontLayoutEstimated,backLayoutEstimated);
        estimationFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(frontLayoutEstimated,backLayoutEstimated, mIsBackVisible,3);
            }
        });
    }

    public void flipCard(View front,View back,boolean mIsBackVisible[],int position) {

        if (!mIsBackVisible[position]) {
            mSetRightOut.setTarget(front);
            mSetLeftIn.setTarget(back);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible[position] = true;
        } else {
            mSetRightOut.setTarget(back);
            mSetLeftIn.setTarget(front);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible[position] = false;
        }
    }

    private void changeCameraDistance(View front,View back) {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        front.setCameraDistance(scale);
        back.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}