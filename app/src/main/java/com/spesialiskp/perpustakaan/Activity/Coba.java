package com.spesialiskp.perpustakaan.Activity;

import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class Coba {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-y");
        Date now = Calendar.getInstance().getTime();
        String a = "13-04-2019";
        String b = sdf.format(now);
        int denda = 0;

        try {
            Date start = sdf.parse(a);

            Date end = sdf.parse(b);
            long sisa = end.getTime() - start.getTime();
            float sisahari = (sisa/(1000*60*60*24));

            if (sisa > 0){
                denda = 500*(Math.round(sisahari));
            } else {
                denda = 0;
            }

            System.out.println("Tanggal hari ini = "+b+", Tanggal kembali = "+a);
            System.out.println("Telat = "+ sisahari +" hari, Denda = "+denda);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
