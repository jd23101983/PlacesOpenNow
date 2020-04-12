package com.bigbang.placesopennow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bigbang.placesopennow.R;
import com.bigbang.placesopennow.database.FavoritePlacesEntity;
import com.bumptech.glide.Glide;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritePlacesAdapter extends RecyclerView.Adapter<FavoritePlacesAdapter.FavoritePlacesViewHolder> {

    private ViewGroup theParent;
    private List<FavoritePlacesEntity> favoritePlacesEntityList;
    private FavoritePlaceInterface favoritePlaceInterface;

    public FavoritePlacesAdapter(List<FavoritePlacesEntity> favoritePlacesEntityList, FavoritePlaceInterface favoritePlaceInterface) {
        this.favoritePlacesEntityList = favoritePlacesEntityList;
        this.favoritePlaceInterface = favoritePlaceInterface;
    }

    public interface FavoritePlaceInterface {
        void deleteFavoritePlace(FavoritePlacesEntity favoritePlacesEntity);
    }

    @NonNull
    @Override
    public FavoritePlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        theParent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_place_item_layout, parent, false);
        return new FavoritePlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePlacesViewHolder holder, int position) {

        holder.deleteFavoriteButton.setOnClickListener(view -> {
            favoritePlaceInterface.deleteFavoritePlace(favoritePlacesEntityList.get(position));
        });
        Glide.with(theParent.getContext()).load(favoritePlacesEntityList.get(position).getPlaceIcon()).into(holder.favoritePlaceIconImageView);
        holder.favoritePlaceNameTextView.setText(favoritePlacesEntityList.get(position).getPlaceName());
    }

    @Override
    public int getItemCount() {
        return favoritePlacesEntityList.size();
    }

    class FavoritePlacesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.favorite_place_icon)
        ImageView favoritePlaceIconImageView;

        @BindView(R.id.favorite_place_name_textview)
        TextView favoritePlaceNameTextView;

        @BindView(R.id.delete_favorite_button)
        Button deleteFavoriteButton;

        public FavoritePlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
