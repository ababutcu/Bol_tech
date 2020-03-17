package com.idyllix.bol_tech.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idyllix.bol_tech.BuildConfig;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.Item;
import com.idyllix.bol_tech.models.SaveServiceRequest;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.models.User;
import com.idyllix.bol_tech.utils.DrawingView;
import com.idyllix.bol_tech.utils.GetCurrentDate;
import com.idyllix.bol_tech.utils.HideKeyboard;
import com.idyllix.bol_tech.viewmodels.ItemViewModel;
import com.idyllix.bol_tech.viewmodels.ServiceViewModel;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServisKapatActivity extends AppCompatActivity {

    private List<String> customerNameList;
    private User user;
    private List<Service> serviceList;
    private EditText etStartingDate;
    private Service selectedService;
    private EditText etOperations;
    private EditText etUsages;
    private ServiceViewModel viewModel;
    private Spinner spCustomers;
    private Spinner spServices;
    private List<String> serviceNameList;
    private String selectedServiceCode;
    private DrawingView drawingView;
    private EditText etImzaAtanCalisan;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    private String yapilanIslemler;
    private String kullanilanMalzemeler;
    private String finishingTime;
    private String duration;
    private Service service;
    private StrictMode.VmPolicy.Builder builder;
    private byte[] imza;
    private ItemViewModel itemViewModel;
    private List<Item> itemList;
    private List<Item> selectedItemList;
    private List<String> itemNameList;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> autoCompleteAdapter;
    private EditText etServisKapatStokMiktari;
    private EditText etServisKapatStokAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servis_kapat);

        initLists();
        getData();
        initViews();
        //getAllItems();
        populateCustomerSpinner(customerNameList);
    }

    private void initLists() {
        serviceList = new ArrayList<>();
        customerNameList = new ArrayList<>();
        serviceNameList = new ArrayList<>();
        itemList=new ArrayList<>();
        selectedItemList=new ArrayList<>();
        itemNameList=new ArrayList<>();
    }

    private void getData() {
            Gson gson = new Gson();
            String userAsString = getIntent().getStringExtra("user");
            user = gson.fromJson(userAsString, User.class);

            Gson gson2 = new Gson();
            Type listType = new TypeToken<List<Service>>() {
            }.getType();
            String serviceListAsString = getIntent().getStringExtra("service");

            serviceList = gson2.fromJson(serviceListAsString, listType);

            if (serviceList != null) {
                customerNameList.clear();
                for (int i = 0; i < serviceList.size(); i++) {
                    if (!customerNameList.contains(serviceList.get(i).getCariAdi()))
                        customerNameList.add(serviceList.get(i).getCariAdi());
                }
            }
        }

    private void initViews() {
        spCustomers = findViewById(R.id.spCustomerName2);
         drawingView = findViewById(R.id.scratch_pad);
        drawingView.setPenSize(2f);

        spCustomers.setOnItemSelectedListener(customerSelectedListener);
        spServices = findViewById(R.id.spServiceNames);
        spServices.setOnItemSelectedListener(serviceSelectedListener);
        etStartingDate = findViewById(R.id.etServiceStarts);
        etOperations = findViewById(R.id.etServiceDescription);
        etUsages = findViewById(R.id.etServiceUsing);
        etImzaAtanCalisan=findViewById(R.id.etImzaAtanYetkili);
        Button btnServisKapat = findViewById(R.id.btnFinishService);
        btnServisKapat.setOnClickListener(kaydetListener);
        ImageButton imageButton = findViewById(R.id.imgClearCanvas);
        imageButton.setOnClickListener(v -> drawingView.clear());

        autoCompleteTextView=findViewById(R.id.autoServisKapat);
        autoCompleteTextView.setOnItemClickListener(autoCompleteListener);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        etServisKapatStokMiktari=findViewById(R.id.etServisKapatStokMiktari);
        etServisKapatStokAdi=findViewById(R.id.etServisKapatStokAdi);
        Button btnStokKaydet = findViewById(R.id.btnStokKaydet);
        btnStokKaydet.setOnClickListener(stokListener);

        builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        viewModel = ViewModelProviders.of(this).get(ServiceViewModel.class);
        viewModel.init();
        itemViewModel=ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.init();
    }

    private View.OnClickListener stokListener=v -> {
        Item item=new Item();
        int id=-1;
        for(int i=0;i<itemList.size();i++){
            if(etServisKapatStokAdi.getText().toString().trim().equals(itemList.get(i).getItemName())){
                id=itemList.get(i).getId();
            }
        }
        item.setId(id);
        item.setItemName(etServisKapatStokAdi.getText().toString().trim());
        item.setQuantity(etServisKapatStokMiktari.getText().toString().trim());
        selectedItemList.add(item);

        StringBuilder s=new StringBuilder(etUsages.getText().toString().trim());
        s.append(etServisKapatStokAdi);
        s.append("-");
        s.append(etServisKapatStokMiktari.getText().toString().trim());
        s.append("\n");
        etUsages.setText(s);
    };

    private View.OnClickListener kaydetListener = v -> {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.buildDrawingCache();
        Bitmap bitmap = drawingView.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        for(int i=0;i<serviceList.size();i++){
            if(spServices.getSelectedItem().toString().equals(serviceList.get(i).getServiceCode())){
                service=serviceList.get(i);
            }
        }
         imza = stream.toByteArray();
        String image = Base64.encodeToString(imza, Base64.DEFAULT);
        yapilanIslemler = etOperations.getText().toString().trim();
        kullanilanMalzemeler = etUsages.getText().toString().trim();

        finishingTime = GetCurrentDate.getThisTime();
        duration =
                GetCurrentDate.getDuration(GetCurrentDate.dateConvertToString(selectedService.getBeginingDate()),
                finishingTime, this);

        SaveServiceRequest serviceRequest=new SaveServiceRequest();
        serviceRequest.setServiceCode(selectedService.getServiceCode());
        serviceRequest.setDescription(yapilanIslemler);
        serviceRequest.setUsage(kullanilanMalzemeler);
        serviceRequest.setFinishingDate(GetCurrentDate.getThisTime());
        serviceRequest.setImzaAtanKisi(etImzaAtanCalisan.getText().toString().trim());
        serviceRequest.setYetkiliImzasi(image);
        serviceRequest.setServisYapanID(user.getUserID());
        if(selectedItemList!=null&&selectedItemList.size()>0){
            serviceRequest.setItems(selectedItemList);
        }

        viewModel.closeService(serviceRequest).observe(this, apiResponse -> {
            if (apiResponse.getAnswer().equals("1")) {
                Toast.makeText(ServisKapatActivity.this, R.string.servis_basariyla_kapatildi
                        , Toast.LENGTH_LONG).show();
                servisRaporuOlustur();
            }else {
               Toast.makeText(ServisKapatActivity.this, R.string.servis_kapatilamadi
                        , Toast.LENGTH_LONG).show();
            }
        });
    };

    private void getAllItems(){
        itemViewModel.getAllItems().observe(this,items -> {
            itemList.addAll(items);
            for (int i=0;i<items.size();i++){
                itemNameList.add(items.get(i).getItemName());
            }
            autoCompleteAdapter = new ArrayAdapter<>(getApplicationContext(),
                    R.layout.item_autocomplete,
                    itemNameList);

            autoCompleteTextView.setAdapter(autoCompleteAdapter);
        });
    }

    private AdapterView.OnItemClickListener autoCompleteListener =
            (parent, view, position, id) -> {
                etServisKapatStokAdi.setText("");
                etServisKapatStokAdi.setText(autoCompleteTextView.getText().toString());
            };

    private TextWatcher textWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (start == 0 && before == 1 && count == 0) {
                itemNameList.clear();
                itemList.clear();
                HideKeyboard.hide(ServisKapatActivity.this);
                getAllItems();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };


    private void servisRaporuOlustur() {
        try {
            createPdfWrapper();
        }catch (FileNotFoundException| DocumentException e){
            Log.d("Hata", Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    private void createPdfWrapper()  throws FileNotFoundException,DocumentException {
            int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                        showMessageOKCancel(
                                (dialog, which) ->
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS));
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            } else {
                //createPdf();
                GeneratePdf generatePdf=new GeneratePdf();
                generatePdf.execute();
            }
    }

    public class GeneratePdf extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                createPdf();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void createPdf() throws FileNotFoundException, DocumentException {

        builder.detectFileUriExposure();
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/ServisPdfleri");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        String pdfname = spServices.getSelectedItem().toString();
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        BaseFont baseFont=null;
        BaseFont baseFont2=null;
        try {
             baseFont=BaseFont.createFont("res/font/roboto_bold.ttf",
                     "iso-8859-9",
                     BaseFont.EMBEDDED);
            baseFont2=BaseFont.createFont("res/font/roboto.ttf",
                    "iso-8859-9",
                    BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font red = new Font(baseFont, 20, Font.NORMAL, BaseColor.RED);
        Chunk redText = new Chunk("BolTech Bolu Teknoloji Servis Raporu ", red);

        Font bold = new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK);
        Font regular = new Font(baseFont2, 12, Font.NORMAL, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(new float[]{5, 5});
        PdfPTable table2 = new PdfPTable(new float[]{1});
        PdfPTable table3 = new PdfPTable(new float[]{1});

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setPaddingTop(10f);
        table.getDefaultCell().setPaddingBottom(10f);
        table.getDefaultCell().setPaddingLeft(5f);
        table.getDefaultCell().setPaddingRight(5f);

        setFirstColumnBackgroundColor(table,"Servis İsteğinde Bulunan Yetkili",bold);
        table.addCell(new Phrase(service.getIstekYapanCariYetkilisi(),regular));
        setFirstColumnBackgroundColor(table,"Servis İsteği Tarihi",bold);
        table.addCell(new Phrase(GetCurrentDate.dateConvertToString(service.getIstekSaati()),regular));
        setFirstColumnBackgroundColor(table,"Servis İsteği Nedeni",bold);
        table.addCell(new Phrase(service.getKonu(),regular));
        setFirstColumnBackgroundColor(table,"Servis Başlangıç",bold);
        table.addCell(new Phrase(GetCurrentDate.dateConvertToString(service.getBeginingDate()),regular));
        setFirstColumnBackgroundColor(table,"İşlem Yapan",bold);
        table.addCell(new Phrase(user.getUserName(),regular));
        setFirstColumnBackgroundColor(table,"Yapılan İşlemler",bold);
        table.addCell(new Phrase(yapilanIslemler,regular));
        setFirstColumnBackgroundColor(table,"Kullanılan Malzemeler",bold);
        table.addCell(new Phrase(kullanilanMalzemeler,regular));
        setFirstColumnBackgroundColor(table,"İşlem Bitiş",bold);
        table.addCell(new Phrase(finishingTime,regular));
        setFirstColumnBackgroundColor(table,"Servis Süresi",bold);
        table.addCell(new Phrase(duration,regular));
        setFirstColumnBackgroundColor(table,"Teslim Alan Yetkili",bold);
        table.addCell(new Phrase(etImzaAtanCalisan.getText().toString().trim(),regular));
        PdfPCell cell1=new PdfPCell(new Phrase("İmza",bold));
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setPaddingLeft(5f);
        table.addCell(cell1);
        Image image=null;

        try {
            image= Image.getInstance(imza);
            image.scaleAbsolute(210,120);
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.addCell(image);

        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.setTotalWidth(PageSize.A4.getWidth());
        table2.setWidthPercentage(100);
        table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table2.getDefaultCell().setPaddingLeft(5f);
        table2.getDefaultCell().setPaddingRight(5f);
        table2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table2.setSpacingAfter(20f);

        table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table3.setTotalWidth(PageSize.A4.getWidth());
        table3.setWidthPercentage(30);
        table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table3.getDefaultCell().setPaddingLeft(5f);
        table3.getDefaultCell().setPaddingRight(5f);
        table3.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table3.setSpacingAfter(15f);
        PdfWriter.getInstance(document, output);
        document.open();

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        Bitmap bitmap = drawableToBitmap(getResources().getDrawable(R.drawable.logooo));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream2);
        Image myImg=null;

        try {
            myImg= Image.getInstance(stream2.toByteArray());
            myImg.scaleAbsolute(200,90);
        } catch (IOException e) {
            e.printStackTrace();
        }

        table3.addCell(myImg);
        table2.addCell(new Phrase(redText));

        document.add(table3);
        document.add(table2);
        document.add(table);
        document.close();
        previewPdf();
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void setFirstColumnBackgroundColor(PdfPTable table, String s, Font bold){
        PdfPCell cell1=new PdfPCell(new Phrase(s,bold));
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setPaddingLeft(5f);
        table.addCell(cell1);
    }

    private void previewPdf() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(ServisKapatActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Toast.makeText(this,
                    "Download a PDF Viewer to see the generated PDF",
                    Toast.LENGTH_SHORT).show();
        }
    }

        private void showMessageOKCancel( DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Pdf Oluşturmak için depolama alanına erişim izni vermeniz lazım")
                .setPositiveButton("Tamam", okListener)
                .setNegativeButton("İptal", null)
                .create()
                .show();
    }

    private void populateCustomerSpinner(List<String> customerNames) {
        //Müşteri Spinner'ini doldur
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ServisKapatActivity.this, R.layout.item_spinner, customerNames);

        spCustomers.setAdapter(adapter);
        spCustomers.setSelection(0);

        //Müşteriye ait servis Spinner'ini doldur
        populateCustomersServiceSpinner(spCustomers.getSelectedItem().toString());
    }

    private void populateCustomersServiceSpinner(String customer) {
        serviceNameList.clear();
        for (int i=0;i<serviceList.size();i++){
            if(serviceList.get(i).getCariAdi().equals(customer)){
                serviceNameList.add(serviceList.get(i).getServiceCode());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ServisKapatActivity.this, R.layout.item_spinner, serviceNameList);
        spServices.setAdapter(adapter);
        spServices.setSelection(0);
        selectedServiceCode=spServices.getSelectedItem().toString();
        setSelectedService(selectedServiceCode);
        populateServiceData(selectedServiceCode);
    }

    private void populateServiceData(String selectedServiceCode) {
        for (int i=0;i<serviceList.size();i++){
            if(serviceList.get(i).getServiceCode().equals(selectedServiceCode)){
                etStartingDate.setText(GetCurrentDate.dateConvertToString(serviceList.get(i).getBeginingDate()));
                etOperations.setText(serviceList.get(i).getDescription());
                etUsages.setText(serviceList.get(i).getUsage());
            }
        }
    }

    private AdapterView.OnItemSelectedListener customerSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedServiceCode=spCustomers.getSelectedItem().toString();
                    populateCustomersServiceSpinner(selectedServiceCode);
                    setSelectedService(selectedServiceCode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

    private AdapterView.OnItemSelectedListener serviceSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedServiceCode = spServices.getSelectedItem().toString();
                    populateServiceData(selectedServiceCode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

    private void setSelectedService(String selectedServiceCode) {
        for(int i=0;i<serviceList.size();i++){
            if(serviceList.get(i).getServiceCode().equals(selectedServiceCode)){
                selectedService=serviceList.get(i);
            }
        }
    }
}