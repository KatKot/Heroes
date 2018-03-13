package com.kotwicka.heroes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kotwicka.heroes.R;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.utils.HeroApiParameters;
import com.kotwicka.heroes.utils.HeroPictureUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.HeroViewHolder> {

    private static final String TAG = HeroesAdapter.class.getSimpleName();

    private final List<HeroViewModel> heroes;
    private final Context context;

    public HeroesAdapter(final Context context, final List<HeroViewModel> heroes) {
        this.heroes = heroes;
        this.context = context;
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hero, parent, false);
        return new HeroViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HeroViewHolder holder, int position) {
        Log.d(TAG, "Binding element at position : " + position);
        final HeroViewModel hero = heroes.get(position);
        holder.heroName.setText(hero.getName());
        Log.d(TAG, "Photo path :  " + hero.getPhotoPath());
        HeroPictureUtil.loadPicture(context, holder.heroImage, hero.getPhotoPath());
    }

    public void clearData(){
        this.heroes.clear();
        notifyDataSetChanged();
        HeroApiParameters.resetParameters();
    }

    @Override
    public int getItemCount() {
        return heroes.size();
    }

    public static class HeroViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hero_image)
        public ImageView heroImage;
        @BindView(R.id.hero_name)
        public TextView heroName;

        public HeroViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
