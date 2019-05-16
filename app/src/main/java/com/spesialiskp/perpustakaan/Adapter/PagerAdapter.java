package com.spesialiskp.perpustakaan.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spesialiskp.perpustakaan.Tab.TabBuku;
import com.spesialiskp.perpustakaan.Tab.TabDashboard;
import com.spesialiskp.perpustakaan.Tab.TabTransaksi;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs){
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0 :
                TabDashboard tabDashboard = new TabDashboard();
                return tabDashboard;
            case 1 :
                TabBuku tabBuku = new TabBuku();
                return tabBuku;
            case 2 :
                TabTransaksi tabTransaksi = new TabTransaksi();
                return tabTransaksi;

                default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
