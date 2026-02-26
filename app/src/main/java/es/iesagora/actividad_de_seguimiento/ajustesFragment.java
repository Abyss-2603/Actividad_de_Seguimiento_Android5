package es.iesagora.actividad_de_seguimiento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import es.iesagora.actividad_de_seguimiento.databinding.FragmentAjustesBinding;
// Importamos tu ViewModel
import es.iesagora.actividad_de_seguimiento.viewModel.AuthViewModel;

public class ajustesFragment extends Fragment {

    private FragmentAjustesBinding binding;
    private SharedPreferences prefs;
    private AuthViewModel viewModel;

    private static final String PREFS_NAME = "mis_preferencias";
    private static final String KEY_IDIOMA = "idioma_pref";
    private static final String KEY_WIFI = "wifi_only";
    private static final String KEY_TEMA = "tema_app";

    private int temaSeleccionado = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        String[] idiomas = {"Español (España)", "English (US)", "Français", "Deutsch"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, idiomas);
        binding.spinnerIdioma.setAdapter(adapter);

        cargarPreferencias();

        viewModel.getNombreUsuario().observe(getViewLifecycleOwner(), nombre -> {
            binding.etNombreUsuario.setText(nombre);
        });

        viewModel.cargarNombreUsuario();

        binding.btnTemaClaro.setOnClickListener(v -> actualizarBotonesTema(0));
        binding.btnTemaOscuro.setOnClickListener(v -> actualizarBotonesTema(1));

        binding.btnGuardar.setOnClickListener(v -> guardarPreferencias());

        binding.btnResetear.setOnClickListener(v -> resetearPreferencias());

        binding.btnCerrarSesion.setOnClickListener(v -> {
            cerrarSesion();
        });
    }

    private void cargarPreferencias() {

        int idiomaIndex = prefs.getInt(KEY_IDIOMA, 0);
        binding.spinnerIdioma.setSelection(idiomaIndex);

        boolean soloWifi = prefs.getBoolean(KEY_WIFI, false);
        binding.switchWifi.setChecked(soloWifi);

        temaSeleccionado = prefs.getInt(KEY_TEMA, 0);
        actualizarBotonesTema(temaSeleccionado);
    }

    private void actualizarBotonesTema(int modo) {
        temaSeleccionado = modo;

        int colorBorde = android.graphics.Color.BLACK;
        int colorSinBorde = android.graphics.Color.TRANSPARENT;

        if (modo == 0) {
            binding.btnTemaClaro.setStrokeColor(android.content.res.ColorStateList.valueOf(colorBorde));
            binding.btnTemaClaro.setStrokeWidth(8);

            binding.btnTemaOscuro.setStrokeColor(android.content.res.ColorStateList.valueOf(colorSinBorde));
            binding.btnTemaOscuro.setStrokeWidth(0);

        } else {
            binding.btnTemaClaro.setStrokeColor(android.content.res.ColorStateList.valueOf(colorSinBorde));
            binding.btnTemaClaro.setStrokeWidth(0);

            binding.btnTemaOscuro.setStrokeColor(android.content.res.ColorStateList.valueOf(colorBorde));
            binding.btnTemaOscuro.setStrokeWidth(8);
        }
    }

    private void guardarPreferencias() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(KEY_IDIOMA, binding.spinnerIdioma.getSelectedItemPosition());
        editor.putBoolean(KEY_WIFI, binding.switchWifi.isChecked());
        editor.putInt(KEY_TEMA, temaSeleccionado);

        editor.apply();

        if (temaSeleccionado == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
    }

    private void resetearPreferencias() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        binding.spinnerIdioma.setSelection(0);
        binding.switchWifi.setChecked(false);
        actualizarBotonesTema(0);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toast.makeText(getContext(), "Preferencias reseteadas", Toast.LENGTH_SHORT).show();
    }

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(requireContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
        Intent intent = new Intent(requireActivity(), inicioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}