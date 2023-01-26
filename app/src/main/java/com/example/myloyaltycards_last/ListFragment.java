package com.example.myloyaltycards_last;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myloyaltycards_last.databinding.FragmentListBinding;
import com.example.myloyaltycards_last.model.Card;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.myloyaltycards_last.model.CardsListManager;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = "List Fragment";

    private RecyclerView mRecyclerView = null;
    private GridLayoutManager mLayoutManager = null;
    private MyAdapter mAdapter = null;
    private FloatingActionButton addButton = null;

    private FragmentListBinding binding;

    private ArrayList<Card> myList = null;

    private NavController navController;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentListBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();

        navController = NavHostFragment.findNavController(ListFragment.this);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.scrollToPosition(0);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        myList = CardsListManager.getInstance().getList();

        mAdapter  = new MyAdapter(this.getContext(), myList, navController);
        mRecyclerView.setAdapter(mAdapter);

        addButton = rootView.findViewById(R.id.add_button);

        return rootView;


    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_FirstFragment_to_addCardFragment2);
            }

        });

        if(getArguments() != null) {
            ListFragmentArgs args = ListFragmentArgs.fromBundle(getArguments());

            Card cardReceived = (Card)args.getCardToSend();


            if(cardReceived != null) {
                Log.i(TAG, "Received card: " + cardReceived.getCompanyName() );
                myList.add(cardReceived);
            }

            else
                Log.i(TAG, "Received card: null" );

            setArguments(null);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}