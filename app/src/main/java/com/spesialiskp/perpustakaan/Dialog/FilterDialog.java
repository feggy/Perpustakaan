package com.spesialiskp.perpustakaan.Dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;
import com.spesialiskp.perpustakaan.Activity.MainFragment.TabTransaksi;
import com.spesialiskp.perpustakaan.Activity.TransaksiActivity;
import com.spesialiskp.perpustakaan.R;
import com.spesialiskp.perpustakaan.Support.RoundedBottomSheetDialogFragment;
import com.spesialiskp.perpustakaan.Support.NamaBulan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FilterDialog extends RoundedBottomSheetDialogFragment {

//    public show(FragmentManager fm, )

    View view;
    Button btnApply;
    LinearLayout vStartDate, vEndDate;
    TextView tvDateStart, tvDateEnd;
    String sTgl, sBln, eTgl, eBln, sPost, ePost;
    DatePickerDialog.OnDateSetListener tgl_awal, tgl_akhir;
    Locale locale;
    SimpleDateFormat sdf, sdfPost;
    NamaBulan nBulan;
    Calendar cal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_filter, container, false);

        init();

        return view;
    }

    private void init() {
        initItem();
        initUI();
    }

    private void initItem() {
        nBulan = new NamaBulan();
        cal = Calendar.getInstance();
        locale = new Locale("id", "ID");
        sdf = new SimpleDateFormat("dd MMMM yyyy", locale);
        sdfPost = new SimpleDateFormat("yyyy-MM-dd");
        Log.e("getTime", cal.getTime().toString());

        btnApply = view.findViewById(R.id.btnApply);
        vStartDate = view.findViewById(R.id.vStartDate);
        vEndDate = view.findViewById(R.id.vEndDate);
        tvDateStart = view.findViewById(R.id.tvDateStart);
        tvDateEnd = view.findViewById(R.id.tvEndDate);
    }

    private void initUI() {
        startDate();
        endDate();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TransaksiActivity.class);
                i.putExtra("start", sPost);
                i.putExtra("end", ePost);
                startActivity(i);

                dismiss();
            }
        });
    }

    private void startDate(){
        sPost = sdfPost.format(cal.getTime());
        tvDateStart.setText(sdf.format(cal.getTime()));

        vStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
                        getTheme(),
                        tgl_awal,
                        year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tgl_awal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month < 9){
                    sBln = "0"+(month+1);
                } else {
                    sBln = Integer.toString(month+1);
                }

                if (dayOfMonth < 10){
                    sTgl = "0"+(dayOfMonth);
                } else {
                    sTgl = Integer.toString(dayOfMonth);
                }

                sPost = year+"-"+sBln+"-"+sTgl;
                String set = sTgl+" "+nBulan.namaBulan(Integer.parseInt(sBln))+" "+year;
                tvDateStart.setText(set);
            }
        };
    }

    private void endDate(){
        ePost = sdfPost.format(cal.getTime());
        tvDateEnd.setText(sdf.format(cal.getTime()));

        vEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
                        getTheme(),
                        tgl_akhir,
                        year, month, day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tgl_akhir = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month < 9) {
                    eBln = "0" + (month + 1);
                } else {
                    eBln = Integer.toString(month + 1);
                }

                if (dayOfMonth < 10) {
                    eTgl = "0" + (dayOfMonth);
                } else {
                    eTgl = Integer.toString(dayOfMonth);
                }

                ePost = year + "-" + eBln + "-" + eTgl;
                String set = eTgl+" "+ nBulan.namaBulan(Integer.parseInt(eBln)) +" "+year;
                tvDateEnd.setText(set);
            }
        };
    }
}
