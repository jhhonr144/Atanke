package com.example.atanke.lectura.models.intems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.atanke.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link lectura_item#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lectura_item extends Fragment {
    public lectura_item() {
    }

    public static lectura_item newInstance(String param1, String param2) {
        lectura_item fragment = new lectura_item();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lectura_item_fragment, container, false);
    }
}