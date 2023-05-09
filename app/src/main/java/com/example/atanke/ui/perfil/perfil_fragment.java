package com.example.atanke.ui.perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.databinding.PerfilFragmentBinding;
import com.example.atanke.perfilusuario.client.PerfilUserClient;
import com.example.atanke.perfilusuario.models.PerfilUsuarioResponse;
import com.example.atanke.perfilusuario.services.PerfilUsuarioService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class perfil_fragment extends Fragment {
    private PerfilFragmentBinding binding;
    private PerfilUsuarioService perfilService;

    private ConfigDataBase db;
    public perfil_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PerfilFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = ConfigDataBase.getInstance(getContext());

        perfilService = PerfilUserClient.getApiService();
        final TextView txtNombre = binding.txtPerfilNombre;
        perfilService
                .getPerfil("89|LdCRhHUy2wpp5JCHAMpgLen3HNkKJOu1BsLz3iHU")
                .enqueue(new Callback<PerfilUsuarioResponse>() {
                    @Override
                    public void onResponse(
                        @NonNull Call<PerfilUsuarioResponse> call,
                        Response<PerfilUsuarioResponse> response) {
                            txtNombre.setText("Will");
                        }

                    @Override
                    public void onFailure(Call<PerfilUsuarioResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "No se puede verificar tu Perfil\n por favor Vuelve a logearte", Toast.LENGTH_SHORT).show();
                    }
                });

        return root;
    }
}