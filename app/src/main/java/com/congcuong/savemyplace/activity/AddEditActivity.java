package com.congcuong.savemyplace.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.congcuong.savemyplace.ActivityUtils;
import com.congcuong.savemyplace.R;
import com.congcuong.savemyplace.data.PlaceRepo;
import com.congcuong.savemyplace.data.model.Place;
import com.congcuong.savemyplace.map.service.ServiceMapAPI;
import com.congcuong.savemyplace.map.service.pojo.GeocodingRoot;
import com.congcuong.savemyplace.map.service.pojo.Location;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.congcuong.savemyplace.ActivityUtils.BASE_URL;
import static com.congcuong.savemyplace.ActivityUtils.CATEGORY_KEY_PUT_EXTRA;

public class AddEditActivity extends AppCompatActivity {
    @BindView(R.id.imgAddEditAct_PlacePicture)
    ImageView imgPlacePic;
    @BindView(R.id.edtAddEdit_PlaceName)
    EditText edtPlaceName;
    @BindView(R.id.edtAddEdit_PlaceAddress)
    EditText edtPlaceAddress;
    @BindView(R.id.edtAddEdit_PlaceDescription)
    EditText edtPlaceDescription;

    private String placeID;
    private String categoryID;
    private PlaceRepo placeRepo;
    private Location location;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private boolean hasImage = false;
    private boolean allowSave = false;

    private static final int IMAGE_CAPTURE_REQUEST_CODE = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ButterKnife.bind(this);
        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK){
            if (data == null){
                if (placeID == null){
                    hasImage = false;
                    allowSave = false;
                }else {
                    hasImage = true;
                }
            }else {
                hasImage = true;
                allowSave = true;
                Bitmap placeImage = (Bitmap) data.getExtras().get("data");
                imgPlacePic.setImageBitmap(placeImage);
            }
        }
    }

    private void init(){
        placeID = getIntent().getStringExtra(ActivityUtils.PLACE_KEY_PUT_EXTRA);
        categoryID = getIntent().getStringExtra(ActivityUtils.CATEGORY_KEY_PUT_EXTRA);
        placeRepo = PlaceRepo.getInstance(this);
        initRetrofit();
        initProgressDialog();

        if (placeID != null){
            hasImage = true;
        }
    }

    private void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initProgressDialog(){
        progressDialog = new ProgressDialog(AddEditActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_saving));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void getLatLng(String address){
        ServiceMapAPI serviceMapAPI = retrofit.create(ServiceMapAPI.class);
        Call<GeocodingRoot> call = serviceMapAPI.getLocation(address);
        call.enqueue(new Callback<GeocodingRoot>() {
            @Override
            public void onResponse(Call<GeocodingRoot> call, Response<GeocodingRoot> response) {
                GeocodingRoot geocodingRoot = response.body();
                double lat = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLat();
                double lng = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLng();

                location = new Location(lat, lng);
            }

            @Override
            public void onFailure(Call<GeocodingRoot> call, Throwable throwable) {

            }

        });
    }

    private byte[] convertImageToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void redirectPlaceAct(){
        Intent placeIntent = new Intent(AddEditActivity.this, PlaceActivity.class);
        placeIntent.putExtra(CATEGORY_KEY_PUT_EXTRA, categoryID);
        startActivity(placeIntent);
        finish();
    }



    @OnClick(R.id.btnAddEdit_Save)
    public void savePlace(View v){
        final String placeName = edtPlaceName.getText().toString();
        final String placeAddress = edtPlaceAddress.getText().toString();
        final String placeDescription = edtPlaceDescription.getText().toString();


        if (Place.validateInput(placeName, placeAddress, placeDescription, categoryID)){
            allowSave = true;
            getLatLng(placeName + ", " + placeAddress);
        }else {
            Toast.makeText(getApplicationContext(), "Please fill the information", Toast.LENGTH_SHORT).show();
        }

        if (allowSave){
            progressDialog.show();
            if (hasImage && placeID == null){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Place place = new Place.Builder()
                                .setPlaceID(UUID.randomUUID().toString())
                                .setCategoryID(categoryID)
                                .setPlaceName(placeName)
                                .setPlaceAddress(placeAddress)
                                .setPlaceDescription(placeDescription)
                                .setPlaceImage(convertImageToByte(imgPlacePic))
                                .setPlaceLat(location.getLat())
                                .setPlaceLng(location.getLng())
                                .build();
                       placeRepo.insert(place);
                       progressDialog.dismiss();
                       redirectPlaceAct();
                    }
                }, 2500);

            }

            if (placeID != null){
                Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.imgAddEditAct_PlacePicture)
    public void openCam(View v){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, IMAGE_CAPTURE_REQUEST_CODE);
    }
}
