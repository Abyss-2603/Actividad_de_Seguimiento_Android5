package es.iesagora.actividad_de_seguimiento.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesRepository {

    private static final String PREFS_NAME = "mis_preferencias";
    private static final String KEY_NOMBRE_USUARIO = "nombre_usuario";

    private final SharedPreferences sharedPreferences;

    public PreferencesRepository(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getNombreUsuario() {
        return sharedPreferences.getString(KEY_NOMBRE_USUARIO, null);
    }

    public void guardarNombreUsuario(String nombre) {
        sharedPreferences.edit().putString(KEY_NOMBRE_USUARIO, nombre).apply();
    }
}