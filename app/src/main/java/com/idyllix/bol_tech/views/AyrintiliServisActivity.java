package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.GetCurrentDate;

public class AyrintiliServisActivity extends AppCompatActivity {

    private Service service;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayrintili_servis);
        getData();
        convertStringToImage();
        print();
    }

    private void convertStringToImage() {
        ImageView img=findViewById(R.id.imageImza);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            byte[] data = Base64.decode(service.getYetkiliImzasi(), Base64.DEFAULT);
            Glide.with(this)
                    .asBitmap()
                    .load(data)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        }
    }

    private void print(){
        EditText etAyrintiliMusteriAdi = findViewById(R.id.etAyrintiliMusteriAdi);
        etAyrintiliMusteriAdi.setText(service.getCariAdi());

        EditText etAyrintiliServisBitis=findViewById(R.id.etAyrintiliServisBitis);
        etAyrintiliServisBitis.setText(GetCurrentDate.dateConvertToString(service.getFinishingDate()));

        EditText etAyrintiliServisYapan=findViewById(R.id.etAyrintiliServisYapan);
        etAyrintiliServisYapan.setText(service.getAdiSoyadi());

        EditText etAyrintiliServisIsteyenYetkili=findViewById(R.id.etAyrintiliServisIsteyenYetkili);
        etAyrintiliServisIsteyenYetkili.setText(service.getIstekYapanCariYetkilisi());

        EditText etAyrintiliServisKonusu=findViewById(R.id.etAyrintiliServisKonusu);
        etAyrintiliServisKonusu.setText(service.getKonu());

        EditText etAyrintiliYapilanIslemler=findViewById(R.id.etAyrintiliYapilanIslemler);
        etAyrintiliYapilanIslemler.setText(service.getDescription());

        EditText etAyrintiliKullanilanMalzemeler=findViewById(R.id.etAyrintiliKullanilanMalzemeler);
        etAyrintiliKullanilanMalzemeler.setText(service.getUsage());

        EditText etAyrintiliServisSuresi=findViewById(R.id.etAyrintiliServisSuresi);
        String duration=GetCurrentDate.getDuration(GetCurrentDate.dateConvertToString(service.getBeginingDate()),
                GetCurrentDate.dateConvertToString(service.getFinishingDate()),this);

        String[] durationArray=duration.split(":");
        String time;
        if(durationArray[0].equals("0")){
            time=String.format(getString(R.string.duration_placeholder_2), durationArray[1]);
        }else {
            time=String.format(getString(R.string.duration_placeholder), durationArray[0],durationArray[1]);
        }

        etAyrintiliServisSuresi.setText(time);

        EditText etAyrintiliServisTeslimAlanCariYetkilisi=findViewById(R.id.etAyrintiliServisTeslimAlanCariYetkilisi);
        etAyrintiliServisTeslimAlanCariYetkilisi.setText(service.getImzaAtanKisi());
    }

    private void getData(){
        Gson gson = new Gson();
        String userAsString = getIntent().getStringExtra("user");
        user = gson.fromJson(userAsString, User.class);

        String serviceAsString = getIntent().getStringExtra("service");
        service=gson.fromJson(serviceAsString,Service.class);
    }
}
