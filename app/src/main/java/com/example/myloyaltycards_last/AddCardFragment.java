package com.example.myloyaltycards_last;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.provider.ContactsContract;
import android.provider.MediaStore;
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

import com.example.myloyaltycards_last.databinding.FragmentAddCardBinding;
import com.example.myloyaltycards_last.databinding.FragmentListBinding;
import com.example.myloyaltycards_last.model.Card;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddCardFragment extends Fragment {

    private static final String TAG = "Add Card Fragment";

    private FragmentAddCardBinding binding;

    private Context context = null;

    private int selectedColor;
    private boolean colorSelected = false;
    private RadioGroup radioGroup = null;

    private String logoFileName;

    private final ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    ImageView addCardLogoImageView = binding.getRoot().findViewById(R.id.add_logo_image_view);
                    if(uri != null) {
                        addCardLogoImageView.setImageURI(uri);
                        saveImageIntoInternalStorage(uri);
                    }
                    else {
                        Log.i(TAG, "no image selected");

                    }
                }
            });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCardBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();

        context = binding.getRoot().getContext();

        radioGroup = binding.radioGroup;

        binding.colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
                colorSelected = true;
            }
        });


        binding.logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContent.launch("image/*");
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);


        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = null;

                if (binding.companyNameTextView.getText().toString().equals(""))
                    Snackbar.make(binding.getRoot(), "Please insert Company Name.", Snackbar.LENGTH_SHORT).show();
                else if (binding.clientCodeTextView.getText().toString().equals(""))
                    Snackbar.make(binding.getRoot(), "Please insert Client Code.", Snackbar.LENGTH_SHORT).show();

                else if (!colorSelected)
                    Snackbar.make(binding.getRoot(), "Please select color.", Snackbar.LENGTH_SHORT).show();


                else {
                    String companyName = binding.companyNameTextView.getText().toString();
                    String clientCode = binding.clientCodeTextView.getText().toString();
                    int clientCodeFormat = radioGroup.getCheckedRadioButtonId();
                    card = new Card(companyName, clientCode, clientCodeFormat, selectedColor, logoFileName);
                    Log.i(TAG, "Card logoPath: " + card.getLogoPath());

                    AddCardFragmentDirections.ActionAddCardFragmentToFirstFragment2 action = AddCardFragmentDirections.actionAddCardFragmentToFirstFragment2();
                    action.setCardToSend(card);
                    navController.navigate(action);
                    Log.i(TAG, "Sending card: " + card.getCompanyName());
                }
            }
        });
    }


    private void saveImageIntoInternalStorage(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] pathParts = uri.getPath().split("/");
        logoFileName = pathParts[pathParts.length-1];
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(logoFileName, Context.MODE_PRIVATE);
            compressBitmapToOutputStream(bitmap, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context, selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                selectedColor = color;
                binding.addColorImageView.setBackgroundColor(selectedColor);
            }
        });

        colorPicker.show();
    }

    private void compressBitmapToOutputStream(Bitmap bitmap, FileOutputStream fos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}