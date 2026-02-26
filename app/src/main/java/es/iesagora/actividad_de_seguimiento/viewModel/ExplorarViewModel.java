package es.iesagora.actividad_de_seguimiento.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import es.iesagora.actividad_de_seguimiento.data.PendientesEntidad;
import es.iesagora.actividad_de_seguimiento.repository.PendientesRepository;
import es.iesagora.actividad_de_seguimiento.repository.PreferencesRepository;

public class ExplorarViewModel extends AndroidViewModel {
    private final PreferencesRepository repository;
    private PendientesRepository pendientesRepo;

    private MutableLiveData<Integer> accionDialogo = new MutableLiveData<>();

    private MutableLiveData<PendientesEntidad> pendienteEncontrado = new MutableLiveData<>();
    public ExplorarViewModel(@NonNull Application application) {
        super(application);
        repository = new PreferencesRepository(application);
        pendientesRepo = new PendientesRepository(application);
    }

    public void comprobarBienvenida() {
        accionDialogo.setValue(0);

        String nombre = repository.getNombreUsuario();

        if (nombre == null || nombre.isEmpty()) {
            accionDialogo.postValue(1);
        } else {
            new Thread(() -> {
                PendientesEntidad p = pendientesRepo.obtenerPendientesAleatorio();

                if (p != null) {
                    pendienteEncontrado.postValue(p);
                    accionDialogo.postValue(2);
                } else {
                    accionDialogo.postValue(0);
                }
            }).start();
        }
    }

    public String getNombreUsuario() {
        return repository.getNombreUsuario();
    }

    // Getters para los LiveData
    public LiveData<Integer> getAccionDialogo() { return accionDialogo; }
    public LiveData<PendientesEntidad> getPendienteEncontrado() { return pendienteEncontrado; }
}
