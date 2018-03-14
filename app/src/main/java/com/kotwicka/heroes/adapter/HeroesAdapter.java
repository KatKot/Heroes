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

public class HeroesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnHeroClickListener {
        void onHeroClick(HeroViewModel hero);
    }

    private static final String TAG = HeroesAdapter.class.getSimpleName();

    private final List<HeroViewModel> heroes;
    private final Context context;
    private final OnHeroClickListener listener;

    public HeroesAdapter(final Context context, final List<HeroViewModel> heroes, final OnHeroClickListener listener) {
        this.heroes = heroes;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ViewHolderType.HERO.getType()) {
            viewHolder = new HeroViewHolder(inflater.inflate(R.layout.hero, parent, false));
        } else {
            viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.loading, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "Binding element at position : " + position);
        if (getItemViewType(position) == ViewHolderType.HERO.getType()) {
            final HeroViewHolder heroViewHolder = (HeroViewHolder) holder;
            heroViewHolder.bind(context, listener, heroes.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return heroes.get(position).getName() == null ? ViewHolderType.LOADING.getType() : ViewHolderType.HERO.getType();
    }

    public void addLoadingItem() {
        add(new HeroViewModel());
    }

    public void removeLoadingItem() {
        if (hasLoadingStubItem()) {
            final int stubItemPosition = heroes.size() - 1;
            heroes.remove(stubItemPosition);
            notifyItemRemoved(stubItemPosition);
        }
    }

    public void add(HeroViewModel heroViewModel) {
        this.heroes.add(heroViewModel);
        notifyItemInserted(heroes.size() - 1);
    }

    private boolean hasLoadingStubItem() {
        return getItemViewType(heroes.size() - 1) == ViewHolderType.LOADING.getType();
    }

    public void clearData() {
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

        public void bind(final Context context,  final OnHeroClickListener listener, final HeroViewModel heroViewModel) {
            heroName.setText(heroViewModel.getName());
            Log.d(TAG, "Photo path :  " + heroViewModel.getPhotoPath());
            HeroPictureUtil.loadPicture(context, heroImage, heroViewModel.getPhotoPath());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onHeroClick(heroViewModel);
                }
            });
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
