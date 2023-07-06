package com.example.atanke.lectura.models.intems;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atanke.R;

public class LecturaSesion extends Fragment {

    public LecturaSesion() {
    }

    public static LecturaSesion newInstance(String param1, String param2) {
        LecturaSesion fragment = new LecturaSesion();
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
        return inflater.inflate(R.layout.fragment_lectura_sesion, container, false);
    }
}