package com.example.atanke.ui.traductor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.atanke.R;
import com.example.atanke.databinding.TraductorFragmentBinding;

public class NotificationsFragment extends Fragment {

    private TraductorFragmentBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.traductor_fragment, container, false);
        // Aquí se pueden obtener las referencias a los elementos de la vista y configurarlos
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}