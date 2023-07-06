package com.example.atanke.ui.lectura;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atanke.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LecturaContenidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LecturaContenidos extends Fragment {
    public LecturaContenidos() {
        // Required empty public constructor
    }
    public static LecturaContenidos newInstance(String param1, String param2) {
        LecturaContenidos fragment = new LecturaContenidos();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {  }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lectura_contenidos, container, false);
    }
}