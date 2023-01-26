package com.example.myloyaltycards_last;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.myloyaltycards_last.databinding.FragmentDetailsBinding;
import com.example.myloyaltycards_last.model.Card;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileInputStream;

public class DetailsFragment extends Fragment {
    private static final String TAG = "Details Fragment";

    private FragmentDetailsBinding binding;

    private CardView front = null;
    private CardView back = null;
    private ImageView logoImageView = null;
    private ImageView codeImageView = null;
    private TextView frontCompanyName = null;
    private TextView clientCodeTextView = null;
    private Card card;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        front = binding.detailsFrontCardView;

        back = binding.detailsBackCardView;

        frontCompanyName = binding.detailsCompanyNameTextView;

        logoImageView = binding.detailsLogoImageView;

        codeImageView = binding.detailsClientCodeImageView;

        clientCodeTextView = binding.detailsClientCodeTextView;


        return binding.getRoot();

    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        if(getArguments() != null) {
            DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());

            card = (Card)args.getCardToShow();

            if(card != null) {
                Log.i(TAG, "Received card: " + card.getCompanyName());

                front.setCardBackgroundColor(card.getColor());

                back.setCardBackgroundColor(card.getColor());

                frontCompanyName.setText(card.getCompanyName());
                if(card.getColor() >= Color.WHITE && card.getColor() < 11711154)
                    frontCompanyName.setTextColor(Color.BLACK);
                else
                    frontCompanyName.setTextColor(Color.WHITE);


                if(card.getLogoPath() != null) {
                    frontCompanyName.setVisibility(View.GONE);
                    logoImageView.setVisibility(View.VISIBLE);

                    Bitmap bitmap = getBitmapFromInternalStorage(card, binding.getRoot().getContext());
                    logoImageView.setImageBitmap(bitmap);
                }

                else {
                    frontCompanyName.setVisibility(View.VISIBLE);
                    logoImageView.setVisibility(View.GONE);
                }

                printCode(card.getClientCode(), card.getClientCodeFormat());

                clientCodeTextView.setText(card.getClientCode());

                if(card.getColor() >= Color.WHITE && card.getColor() < 11711154)
                    clientCodeTextView.setTextColor(Color.BLACK);
                else
                    clientCodeTextView.setTextColor(Color.WHITE);
            }
            else
                Log.i(TAG, "Received card: null");

        }
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


    private void printCode(String clientCode, int formatCode) {
        BarcodeFormat barcodeFormat = BarcodeFormat.QR_CODE;
        int codeWidth = 400, codeHeight = 400;
        switch(formatCode) {
            case R.id.qr_code_button:
                barcodeFormat = BarcodeFormat.QR_CODE;
                break;
            case R.id.bar_code_button:
                barcodeFormat = BarcodeFormat.CODE_128;
                codeWidth = codeHeight * 2;
                break;
        }
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bm = barcodeEncoder.encodeBitmap(clientCode, barcodeFormat,
                    codeWidth, codeHeight);
            codeImageView.setImageBitmap(bm);
        } catch(Exception e) {
            Log.e("ERROR", "Error encoding QR code / barcode");
        }
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}