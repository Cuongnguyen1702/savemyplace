package com.congcuong.savemyplace.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.congcuong.savemyplace.R;
import com.congcuong.savemyplace.data.PlaceRepo;
import com.congcuong.savemyplace.data.model.Category;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.congcuong.savemyplace.ActivityUtils.CATEGORY_KEY_PUT_EXTRA;

public class CategoriesActivity extends AppCompatActivity {

    @BindView(R.id.txtCategoriesAct_ATM)
    TextView txtATM;
    @BindView(R.id.txtCategoriesAct_Cinema)
    TextView txtCinema;
    @BindView(R.id.txtCategoriesAct_Fashion)
    TextView txtFashion;
    @BindView(R.id.txtCategoriesAct_Restaurant)
    TextView txtRestaurant;



    private PlaceRepo placeRepo;
    private List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        placeRepo = PlaceRepo.getInstance(this);
        categories = placeRepo.getCategories();
        setCategories();
    }

    public void setCategories(){
        txtATM.setText(categories.get(0).getCategoryName());
        txtCinema.setText(categories.get(1).getCategoryName());
        txtFashion.setText(categories.get(2).getCategoryName());
        txtRestaurant.setText(categories.get(3).getCategoryName());
    }

    private void startPlaceAct(String categoryID){
        Intent placeIntent = new Intent(CategoriesActivity.this, PlaceActivity.class);
        placeIntent.putExtra(CATEGORY_KEY_PUT_EXTRA, categoryID);
        startActivity(placeIntent);
    }


    @OnClick(R.id.layoutCategoriesAct_ATM)
    public void clickATM(View v){
        String categoryID = categories.get(0).getCategoryID();
        startPlaceAct(categoryID);
    }

    @OnClick(R.id.layoutCategoriesAct_Cinema)
    public void clickCinema(View v){
        String categoryID = categories.get(1).getCategoryID();
        startPlaceAct(categoryID);
    }

    @OnClick(R.id.layoutCategoriesAct_Fashion)
    public void clickFashion(View v){
        String categoryID = categories.get(2).getCategoryID();
        startPlaceAct(categoryID);
    }

    @OnClick(R.id.layoutCategoriesAct_Restaurant)
    public void clickRestaurant(View v){
        String categoryID = categories.get(3).getCategoryID();
        startPlaceAct(categoryID);
    }

}
