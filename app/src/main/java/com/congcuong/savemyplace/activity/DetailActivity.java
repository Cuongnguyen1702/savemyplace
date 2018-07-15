package com.congcuong.savemyplace.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.congcuong.savemyplace.ActivityUtils;
import com.congcuong.savemyplace.R;
import com.congcuong.savemyplace.data.PlaceRepo;
import com.congcuong.savemyplace.data.model.Place;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.imgDetailAct_PlacePicture)
    ImageView imgPlacePicture;
    @BindView(R.id.edtDetail_PlaceName)
    EditText edtPlaceName;
    @BindView(R.id.edtDetail_PlaceAddress)
    EditText edtPlaceAddress;
    @BindView(R.id.edtDetail_PlaceDescription)
    EditText edtPlaceDescription;

    private String placeID;
    private Place place;
    private String categoryID;
    private PlaceRepo placeRepo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        init();
        initProgressDialog();
    }

    private void init(){
        placeID = getIntent().getStringExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA);
        categoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstance(this);
        setPlace();
    }

    private void setPlace(){
        place = placeRepo.getPlace(categoryID, placeID);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (place.getPlaceImage() != null){
                    Bitmap placeBitmap = BitmapFactory.decodeByteArray(place.getPlaceImage(), 0, place.getPlaceImage().length);
                    imgPlacePicture.setImageBitmap(placeBitmap);
                }
                edtPlaceName.setText(place.getPlaceName());
                edtPlaceAddress.setText(place.getPlaceAddress());
                edtPlaceDescription.setText(place.getPlaceDescription());


            }
        }, 3500);
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(DetailActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_retrieving_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }



    @OnClick(R.id.imgbtnDetail_Delete)
    public void deletePlace(View v){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(DetailActivity.this);
        alertdialog.setTitle("Warning");
        alertdialog.setIcon(R.drawable.ic_warning);
        alertdialog.setMessage("Do you want to delete?");
        alertdialog.setCancelable(true);
        alertdialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
        });
        alertdialog.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert1 = alertdialog.create();
        alert1.show();
    }
    @OnClick(R.id.imgbtnDetail_Edit)
    public void editPlace(View v){
        Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
        intent.putExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA, place.getPlaceID());
        intent.putExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA, place.getCategoryID());
        startActivity(intent);
    }
    @OnClick(R.id.imgbtnDetail_Direction)
    public void directionPlace(View v){

    }
}
