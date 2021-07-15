package com.example.help.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.help.Activities.profile_activity;
import com.example.help.R;
import com.example.help.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);


        if (mToolbar != null) {

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        CardView profile = view.findViewById(R.id.profile_card_view);
        CardView about = view.findViewById(R.id.about_the_app);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile_Activity = new Intent(getActivity(), profile_activity.class);
                startActivity(profile_Activity);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent info_Activity = new Intent(getActivity(), com.example.help.Activities.info_Activity.class);
                startActivity(info_Activity);
            }
        });


        return view;


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

}