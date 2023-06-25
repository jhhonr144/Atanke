package com.example.atanke.general.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class Imagenes {

    public  Bitmap redimensionarImagen(File fotoFile, int maxWidth, int maxHeight) {
        // Obtener las dimensiones originales de la imagen
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fotoFile.getAbsolutePath(), options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        // Calcular el factor de escala para redimensionar la imagen
        float scaleFactor = Math.min((float) originalWidth / maxWidth, (float) originalHeight / maxHeight);

        // Crear un objeto BitmapFactory.Options para redimensionar la imagen
        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.round(scaleFactor);
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // Decodificar el archivo de imagen en un objeto Bitmap redimensionado
        return BitmapFactory.decodeFile(fotoFile.getAbsolutePath(), options);
    }
    public  String convertirImagenABase64(Bitmap imagen) {
        if ( imagen != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagen.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.URL_SAFE);//"data:image/jpeg;base64,"
        } else {
            return null;
        }
    }


    public  String alg(){
        return "algo";
    }

}
