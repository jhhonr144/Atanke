package com.example.atanke.palabras.models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atanke.R;
import com.example.atanke.config.ConfigDataBase;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.utils.Imagenes;
import com.example.atanke.palabras.client.PalabrasAddFotoClient;
import com.example.atanke.palabras.client.PalabrasClient;
import com.example.atanke.palabras.services.PalabrasAddFotoService;
import com.example.atanke.sugerirtraduccion.Dao.GetAllUsuariosTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class dicicionario_agregar_contenido extends AppCompatActivity {
    private Spinner spinnerTipoArchivo;
    private TextView editTextArchivo,palabra;
    private Button btn_guardar,btn_agregar;
    Imagenes img;
    File archivo = null;
    palabrasRelacion objeto;
    private List<UsuarioDTO> usuarios;
    private ConfigDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicicionario_agregar_contenido);
        db = ConfigDataBase.getInstance(this);
        agregarReferencia();
        objeto= (palabrasRelacion) getIntent().getSerializableExtra("objeto");
        palabra.setText(objeto.palabra);
    }

    private void agregarReferencia() {
        spinnerTipoArchivo= findViewById(R.id.spinner);
        agregartipoSpiner();
        editTextArchivo= findViewById(R.id.textView8);
        palabra= findViewById(R.id.txt_palabra);
        btn_guardar= findViewById(R.id.button);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    GuardarArchivo();
            }
        });
        btn_agregar= findViewById(R.id.btn_agregar);
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarArchivo();
            }
        });
        img= new Imagenes();
    }
    private static final int REQUEST_CODE_ARCHIVO = 1;

    private void agregarArchivo() {
        String tipoArchivo = spinnerTipoArchivo.getSelectedItem().toString();
        String mimeType = getMimeType(tipoArchivo);
        if (mimeType.equals("*/*")) {
         //$$mensaje debe selecionar antes tipo archivo
            return;
        }
        // Abrir el explorador de archivos con el tipo de archivo filtrado
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar archivo"), REQUEST_CODE_ARCHIVO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ARCHIVO && resultCode == RESULT_OK) {
            Uri archivoUri = data.getData();
            // Obtener la ruta del archivo seleccionado
            String rutaArchivo = obtenerRutaArchivo(archivoUri);
            obtenerArchivo(archivoUri);
            // Mostrar la ruta del archivo en el TextView
            editTextArchivo.setText(rutaArchivo);
            btn_agregar.setText("Cambiar");
        }
    }
    private File obtenerArchivo(Uri archivoUri) {
        if (archivoUri.getScheme().equals("file")) {
            archivo = new File(archivoUri.getPath());
        } else {
            // Si el archivo se encuentra en un proveedor de contenido, se puede acceder a través de una consulta
            Cursor cursor = getContentResolver().query(archivoUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    String nombreArchivo = cursor.getString(index);
                    String directorioTemp = getCacheDir().getAbsolutePath(); // Obtener el directorio temporal de la aplicación
                    archivo = new File(directorioTemp, nombreArchivo);
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(archivoUri);
                        OutputStream outputStream = new FileOutputStream(archivo);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        outputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
        }
        return archivo;
    }
    //solo para seber que tiene el spiner
    private String getMimeType(String tipoArchivo) {
        switch (tipoArchivo) {
            case "Foto":
                return "image/*";
            case "Video":
                return "video/*";
            case "Audio":
                return "audio/*";
            default:
                return "*/*"; // Tipo de archivo genérico
        }
    }
    //solo para saber el nombre del archivo
    private String obtenerRutaArchivo(Uri archivoUri) {
        String ruta = null;
        if (archivoUri.getScheme().equals("file")) {
            ruta = archivoUri.getPath();
        } else {
            // Si el archivo se encuentra en un proveedor de contenido, se puede acceder a través de una consulta
            Cursor cursor = getContentResolver().query(archivoUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    ruta = cursor.getString(index);
                }
                cursor.close();
            }
        }
        return ruta;
    }
    //para agregar datos al spiner
    private void agregartipoSpiner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_archivo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoArchivo.setAdapter(adapter);
    }
    public void GuardarArchivo() {
        String tipoArchivo = spinnerTipoArchivo.getSelectedItem().toString();
        if (tipoArchivo.equals("Foto")) {
            procesarFoto();
        } else if (tipoArchivo.equals("Video")) {
            procesarVideo("nombreArchivo");
        } else if (tipoArchivo.equals("Audio")) {
            procesarAudio("nombreArchivo");
        } else {
            //$$mensaje debe selecionar antes tipo archivo
        }
    }

    private void procesarFoto() {
        // Redimensionar la imagen a una resolución máxima deseada
        Bitmap imagenRedimensionada = img.redimensionarImagen(archivo, 1080, 720);
        // Codificar la imagen redimensionada a Base64
        String imagenBase64 = img.convertirImagenABase64(imagenRedimensionada);
        // Enviar la imagen codificada al servidor
        enviarImagenAlServidor(imagenBase64.replace("\n", ""));
    }
    private PalabrasAddFotoService Servi;
    private void enviarImagenAlServidor(String imagenBase64) {
        String nombre=editTextArchivo.getText().toString();
        String id=objeto.id+"";
        String token="";
        try {
            this.infoUser();
            UsuarioDTO ultimoUsuario = usuarios.get(usuarios.size() - 1);
            token="Bearer "+ultimoUsuario.getToken();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        //$$MOSTRARCARGANDO$$
        //no hay datos descargado hay qe consultar a la web
        Servi = PalabrasAddFotoClient.getApiService();
        Servi.postAddFofo ( token, addFotoRequest.builder()
                        .contenido(imagenBase64)
                        .id(id)
                        .nombre(nombre).build())
                    .enqueue(new Callback<PalabrasAddFotoResponse>() {
                        @Override
                        public void onResponse(
                                @NonNull Call<PalabrasAddFotoResponse> call,
                                Response<PalabrasAddFotoResponse> response) {
                            if (response.body() == null) {
                                Toast.makeText(getBaseContext(), "No obtubimos respuesta del servidor", Toast.LENGTH_SHORT).show();
                            } else {
                                if(response.body().getId()==0){
                                    Toast.makeText(getBaseContext(), "Agregado", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    //$$mostrar el mensaje dde que hubo el error de mensaje
                                    Toast.makeText(getBaseContext(), "No  Agregado\n"+response.body().getMensaje(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<PalabrasAddFotoResponse> call, Throwable t) {
                            Toast.makeText(getBaseContext(),
                                    "No se puede conectar con el servidor, validad tu conecion a internet",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
    }
    private void infoUser() throws ExecutionException, InterruptedException {
        UsuarioDao userDao = db.usuarioDao();
        GetAllUsuariosTask task = new GetAllUsuariosTask(userDao);
        task.execute();
        usuarios = task.get();
    }
    private void procesarVideo(String nombreArchivo) {
        // Lógica para procesar archivo de video
        // por ejemplo, reproducir el video en un reproductor multimedia o guardar la ruta del archivo en una lista de videos
    }

    private void procesarAudio(String nombreArchivo) {
        // Lógica para procesar archivo de audio
        // por ejemplo, reproducir el audio en un reproductor multimedia o guardar la ruta del archivo en una lista de audios
    }

}