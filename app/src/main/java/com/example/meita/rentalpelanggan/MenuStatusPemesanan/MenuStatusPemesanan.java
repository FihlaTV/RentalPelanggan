package com.example.meita.rentalpelanggan.MenuStatusPemesanan;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meita.rentalpelanggan.R;

import java.util.ArrayList;
import java.util.List;

public class MenuStatusPemesanan extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    View view;
    Adapter adapter;
    int value;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Kelola Penyewaan");
        View v = inflater.inflate(R.layout.fragment_menu_status_pemesanan, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getAdapter().notifyDataSetChanged();

        //ini bisa
        try {
            final int positionTab1 = getArguments().getInt("tab1");
            final int positionTab2 = getArguments().getInt("tab2");
            final int positionTab3 = getArguments().getInt("tab3");
            final int positionTab4 = getArguments().getInt("tab4");
            final int positionTab7 = getArguments().getInt("tab7");
            final int positionTab8 = getArguments().getInt("tab8");
            if (positionTab1 == 1) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(0);

                    }
                });
            } else if (positionTab2 == 2) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(1);
                    }
                });
            } else if (positionTab3 == 3) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(2);
                    }
                });
            } else if (positionTab4 == 4) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(3);
                    }
                });
            } else if (positionTab7 == 7) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(6);
                    }
                });
            } else if (positionTab8 == 8) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(7);
                    }
                });
            }
        } catch (Exception e) {

        }


        return v;

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new TabStatus1(), "Belum Bayar");
        adapter.addFragment(new TabStatus2(), "Menunggu Konfirmasi");
        adapter.addFragment(new TabStatus3(), "Pesanan Berhasil");
        adapter.addFragment(new TabStatus4(), "Selesai");
        adapter.addFragment(new TabStatus5(), "Pengajuan Pembatalan");
        adapter.addFragment(new TabStatus6(), "Batal");
        adapter.addFragment(new TabStatus7(), "Menunggu Sisa Pembayaran");
        adapter.addFragment(new TabStatus8(), "Konfirmasi Sisa Pembayaran");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
