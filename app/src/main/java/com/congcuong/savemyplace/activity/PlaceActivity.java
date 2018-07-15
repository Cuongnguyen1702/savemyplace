package com.congcuong.savemyplace.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.congcuong.savemyplace.ActivityUtils;
import com.congcuong.savemyplace.R;
import com.congcuong.savemyplace.adapter.PlaceAdapter;
import com.congcuong.savemyplace.data.PlaceRepo;
import com.congcuong.savemyplace.data.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceActivity extends AppCompatActivity {
    @BindView(R.id.lvPlacesAct)
    ListView lvPlaces;
    @BindView(R.id.txtPlaceAct_null)
    TextView txtNoData;

    private String categoryID;
    private PlaceRepo placeRepo;
    private List<Place> places = new ArrayList<>();
    private PlaceAdapter placeAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.tlbPlace);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        ButterKnife.bind(this);
        init();
    }


    private void init(){
        categoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstance(this);
        placeAdapter = new PlaceAdapter(this, places);
        initProgressDialog();
        getPlaces();
        onPlaceClick();
    }

    private void getPlaces(){
        places = placeRepo.getPlaces(categoryID);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (!places.isEmpty()) {
                    txtNoData.setVisibility(View.GONE);
                }
                lvPlaces.setAdapter(placeAdapter);
                placeAdapter.updatePlaces(places);
            }
        }, 3500);

    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(PlaceActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_retrieving_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void onPlaceClick() {
        lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place place = places.get(i);
                Intent detail = new Intent(PlaceActivity.this, DetailActivity.class);
                detail.putExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA, place.getPlaceID());
                detail.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA, place.getCategoryID());
                startActivity(detail);
            }
        });
    }



    @OnClick(R.id.fabPlaceAct_AddNewPlace)
    public void addNewPlace(View view){
        Intent addEdit = new  Intent(PlaceActivity.this, AddEditActivity.class);
        addEdit.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA, categoryID);
        startActivity(addEdit);
    }
    @OnClick(R.id.btnPlaceAct_ShowAll)
    public void showAllOnMap(View view){
        Toast.makeText(getApplicationContext(), "Show all", Toast.LENGTH_SHORT).show();
    }


}
