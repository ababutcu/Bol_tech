package com.idyllix.bol_tech.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.idyllix.bol_tech.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ITextMethods {

    /*private void prepareText(Document document, Service service, User user){
        printText("Servis İsteğinde Bulunan Yetkili : ",service.getIstekYapanCariYetkilisi(),document);
        printText("Servis İsteği Tarihi : ",GetCurrentDate.dateConvertToString(service.getIstekSaati()),document);
        printText("Servis İsteği Nedeni : ",service.getKonu(),document);
        printText("Servis Başlangıç : ",GetCurrentDate.dateConvertToString(service.getBeginingDate()),document);
        printText("İşlem Yapan : ",user.getUserName(),document);
        printText("Yapılan İşlemler ",yapilanIslemler,document);
        printText("Kullanılan Malzemeler : ",kullanilanMalzemeler,document);
        printText("İşlem Bitiş : ",finishingTime,document);
        printText("Servis Süresi : ",duration,document);
        printText("Teslim Alan Yetkili : ",etImzaAtanCalisan.getText().toString().trim(),document);
        printText("İmza : ","İmza",document);
    }*/

    private void printText(String s1,String s2,Document document){
        BaseFont baseFont1=null;
        BaseFont baseFont2=null;
        try {
            baseFont1=BaseFont.createFont("res/font/roboto.ttf", "iso-8859-9", BaseFont.EMBEDDED);
            baseFont2=BaseFont.createFont("res/font/roboto_bold.ttf", "iso-8859-9", BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        Font bold = new Font(baseFont2, 14, Font.BOLD, BaseColor.RED);
        Font regular = new Font(baseFont1, 12, Font.NORMAL, BaseColor.BLACK);
        Paragraph p = new Paragraph(s1,bold);
        p.add(new Chunk(s2,regular).setLineHeight(10f));
        try {
            document.add(p);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        p.setSpacingAfter(30f);
        p.setMultipliedLeading(1.5f);

    }

    public static void  setImage(Context context){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_logo);
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        Bitmap bitmap;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (drawable != null) {
                drawable = (DrawableCompat.wrap(drawable)).mutate();

                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas2.getWidth(), canvas2.getHeight());
                drawable.draw(canvas2);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream2);
            }
        }

        Chunk chunk=null;

        Image image2 = null;
        try {
            image2 = Image.getInstance(stream2.toByteArray());
            image2.scaleAbsolute(160,100);
            chunk=new Chunk(image2,20,0);

        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
    }
}
