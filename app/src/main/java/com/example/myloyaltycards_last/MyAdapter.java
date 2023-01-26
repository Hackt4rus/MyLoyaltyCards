package com.example.myloyaltycards_last;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloyaltycards_last.model.Card;
import com.example.myloyaltycards_last.model.CardsListManager;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "Adapter";

    private ArrayList<Card> mDataSet;

    private NavController navController;

    private Context context;


    public MyAdapter(Context context, ArrayList<Card> dataSet, NavController navController) {
        this.context = context;
        this.mDataSet = dataSet;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_element, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = mDataSet.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListFragmentDirections.ActionFirstFragmentToDetailsFragment action
                        = ListFragmentDirections.actionFirstFragmentToDetailsFragment();

                action.setCardToShow(card);

                navController.navigate(action);

                Log.i(TAG, "Sending card: " + card.getCompanyName());
            }
        });



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete this card?")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Card cardToDelete = deleteCard(card);
                                Snackbar snackbar = Snackbar.make(view, "Card deleted.", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setDuration(15000);
                                snackbar.setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        addCard(cardToDelete);
                                    }
                                });

                                snackbar.show();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });


        holder.setBackgroundColor(card.getColor());
        if(card.getLogoPath() == null) {
            holder.getImageView().setVisibility(View.GONE);
            holder.getTextView().setVisibility(View.VISIBLE);
            holder.getTextView().setText(card.getCompanyName());
            if(card.getColor() >= Color.WHITE && card.getColor() < 11711154)
                holder.getTextView().setTextColor(Color.BLACK);
            else
                holder.getTextView().setTextColor(Color.WHITE);


        } else {
            holder.getTextView().setVisibility(View.GONE);
            holder.getImageView().setVisibility(View.VISIBLE);
            setLogoImage(holder, card);
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    private void setLogoImage(ViewHolder viewHolder, Card card) {
        ImageView imageView = viewHolder.getImageView();
        Bitmap bitmap = getBitmapFromInternalStorage(card, imageView.getContext());
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromInternalStorage(Card card, Context context) {
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try {
            fileInputStream = context.openFileInput(card.getLogoPath());
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public Card deleteCard(Card cardInterested) {
        Card cardToDelete = null;
        for(Card card : mDataSet)
            if(card.isEqualToCard(cardInterested))
                cardToDelete = card;
        if(cardToDelete != null) {
            int position = mDataSet.indexOf(cardToDelete);
            mDataSet.remove(cardToDelete);
            notifyItemRemoved(position);
        }
        return cardToDelete;
    }


    public void addCard(Card card) {
        if(card != null) {
            mDataSet.add(card);
            int position = mDataSet.indexOf(card);
            notifyItemInserted(position);
        }
    }

    /* -------------------- ViewHolder Class ---------------------- */

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View v = null;

        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(@NonNull View v) {
            super(v);
            this.v = v;
            this.imageView = v.findViewById(R.id.item_image_view);
            this.textView = v.findViewById(R.id.item_company_name_text_view);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setBackgroundColor(int color) {
            CardView cardView = itemView.findViewById(R.id.item_card_list_view);
            cardView.setCardBackgroundColor(color);
        }
    }

}
