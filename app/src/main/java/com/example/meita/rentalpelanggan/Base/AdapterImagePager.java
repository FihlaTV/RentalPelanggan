package com.example.meita.rentalpelanggan.Base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meita.rentalpelanggan.R;

import java.util.List;

/**
 * Created by tantr on 2/15/2017.
 */

public class AdapterImagePager extends PagerAdapter {
    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<String> list;

    public AdapterImagePager(Context c, List<String> list) {
        ctx = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.activity_custom_swip, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.swip_image_view);
        TextView txtDigit = (TextView) itemView.findViewById(R.id.txt_swip_digit);

        if (list.size() > 0) {
            ImageLoader.getInstance().loadImageOther((Activity) ctx, list.get(position), imageView);
            txtDigit.setText(String.format("%d / %d", position + 1, list.size()));
        } else if (list.size() < 0) {
            imageView.setImageResource(R.drawable.no_image);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view == object);
    }
}
