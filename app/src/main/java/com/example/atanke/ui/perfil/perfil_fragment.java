package com.example.atanke.ui.perfil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.databinding.PerfilFragmentBinding;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.perfilusuario.services.PerfilUsuarioService;
import com.example.atanke.sugerirtraduccion.Dao.GetAllUsuariosTask;
import com.example.atanke.ui.login.Login;
import com.example.atanke.ui.registrarse.Registrarse;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class perfil_fragment extends Fragment {
    private PerfilFragmentBinding binding;
    private PerfilUsuarioService perfilService;
    private List<UsuarioDTO> usuarios;
    private ConfigDataBase db;
    private TextView txtNombre;
    private TextView txtcorreo;
    private TextView txtcreado;

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
        txtNombre = binding.txtPerfilNombre;
        txtcorreo = binding.txtPerfilCorreo;
        txtcreado = binding.txtPerfilCreado;


        try {
            infoUser();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*consultar info de usr por get
        perfilService = PerfilUserClient.getApiService();
        perfilService
                .getPerfil("Bearer 89|LdCRhHUy2wpp5JCHAMpgLen3HNkKJOu1BsLz3iHU" )
                .enqueue(new Callback<PerfilUsuarioResponse>() {
                    @Override
                    public void onResponse(
                        @NonNull Call<PerfilUsuarioResponse> call,
                        Response<PerfilUsuarioResponse> response) {
                        if (response.body() == null) {
                            Toast.makeText(getContext(), "No se puede, Logearse", Toast.LENGTH_SHORT).show();
                        } else {
                            txtNombre.setText(response.body().getMensaje());
                        }
                    }

                    @Override
                    public void onFailure(Call<PerfilUsuarioResponse> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "No se puede verificar tu Perfil\n por favor Vuelve a logearte",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        */
        return root;
    }

    private void infoUser() throws ExecutionException, InterruptedException {
        UsuarioDao userDao = db.usuarioDao();
        GetAllUsuariosTask task = new GetAllUsuariosTask(userDao);
        task.execute();
        usuarios = task.get();
        if(usuarios.isEmpty()){
            mensaje(getContext());
        }
        else{
            UsuarioDTO ultimoUsuario = usuarios.get(usuarios.size() - 1);
            // Asigna los valores a los TextViews
            txtNombre.setText(ultimoUsuario.getName());
            txtcorreo.setText(ultimoUsuario.getEmail());
            txtcreado.setText(ultimoUsuario.getCreated_at().split("T")[0]);
            //txtestado.setText(ultimoUsuario.getR_users_estados()+"");
            //txtrol.setText(ultimoUsuario.getR_users_roles()+"");
        }
    }
    private String nullOString(String valor){
        return valor == null? "":valor +"";
    }
    public void mensaje(Context context){
        new MaterialAlertDialogBuilder(context)
                .setTitle("¡Ups, parece que necesitas iniciar sesión!")
                .setMessage("Para acceder a esta función, debes iniciar sesión o crear una cuenta. ¡No te preocupes, es muy fácil!")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }
                })
                .setNegativeButton("Registrarse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Registrarse.class);
                        context.startActivity(intent);
                    }
                })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}