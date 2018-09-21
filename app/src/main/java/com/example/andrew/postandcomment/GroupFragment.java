package com.example.andrew.postandcomment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView list;
    FloatingActionButton fabAddGroup;
    FirebaseUser user;


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = getActivity().findViewById(R.id.lstGroups);
        listItems=new ArrayList<String>();
        fabAddGroup = getActivity().findViewById(R.id.fabAddGroup);

        adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listItems);

        list.setAdapter(adapter);

        fabAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUserName = user.getDisplayName();
                listItems.add(currentUserName + "'s Group");
                adapter.notifyDataSetChanged();
            }
        });


    }
}
