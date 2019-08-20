package com.example.seyaha;

import android.app.AlertDialog;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class adminPlaceAdapter extends RecyclerView.Adapter<adminPlaceAdapter.ImageViewHolder> {
    public static List<Place> mPlace;
    public static List<Place>chosen_places;
    ColorDrawable colorDrawable;
    Context context;
    int counter[];

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_categories, viewGroup, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);

        return imageViewHolder;
    }

    public adminPlaceAdapter (Context context,List<Place> place)
    {
        mPlace= place;
        this.context=context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position)
    {
        counter=new int[mPlace.size()];
        Place place= mPlace.get(position);
        colorDrawable = new ColorDrawable(Color.GRAY);
        chosen_places=new ArrayList <Place>();

        Picasso.get().load(mPlace.get(position).imageURL).placeholder(colorDrawable).fit().into(holder.circle);
        holder.circle.setClipToOutline(true);
        holder.place_name_admin.setText(mPlace.get(position).nameEN);
        holder.circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(counter[position]%2==0)
                {
                    holder.checkable.setVisibility(View.VISIBLE);
                    chosen_places.add(mPlace.get(position));
                }
                else
                {
                    holder.checkable.setVisibility(View.INVISIBLE);
                    chosen_places.remove(mPlace.get(position));
                }
                counter[position]++;
            }
        });

    }
    @Override
    public int getItemCount() {
        return mPlace.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
       ImageView circle,checkable;
       TextView place_name_admin;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ImageViewHolder(View itemView) {
            super(itemView);
            circle=itemView.findViewById(R.id.round_img);
            checkable=itemView.findViewById(R.id.img_correct);
            place_name_admin=itemView.findViewById(R.id.place_name_admin);
           /* vi = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(R.layout.custom_rating_dialog, null, false);
            post_btn = mView.findViewById(R.id.post_btn);
            mClost_btn = mView.findViewById(R.id.close_btn);
            editText = mView.findViewById(R.id.comment_post);
            builder = new AlertDialog.Builder(itemView.getContext());
            builder.setView(mView);
            mTitle = itemView.findViewById(R.id.title_txt);
            mDescription = itemView.findViewById(R.id.desc);
            mRating = itemView.findViewById(R.id.rate_num);
            mArrowDown = itemView.findViewById(R.id.arrowDown);
            mComments = itemView.findViewById(R.id.comment_num);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            mShare_btn = itemView.findViewById(R.id.share_btn);
            mComment_btn = itemView.findViewById(R.id.comment_btn);
            mRate_btn = itemView.findViewById(R.id.star);
*/
        }

    }


}
