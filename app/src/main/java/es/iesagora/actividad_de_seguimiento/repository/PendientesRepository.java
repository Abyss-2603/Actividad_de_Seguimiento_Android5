package es.iesagora.actividad_de_seguimiento.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import es.iesagora.actividad_de_seguimiento.data.PendienteDao;
import es.iesagora.actividad_de_seguimiento.data.PendientesDatabase;
import es.iesagora.actividad_de_seguimiento.data.PendientesEntidad;

public class PendientesRepository {
    private PendienteDao pendienteDao;
    private Executor executor;
    private String currentUserId;
    public PendientesRepository(Application application){
        pendienteDao = PendientesDatabase.getInstance(application).pendienteDao();
        executor = Executors.newSingleThreadExecutor();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
        } else {
            currentUserId = "";
        }
    }
    public void insertar(PendientesEntidad pendientesEntidad){
        pendientesEntidad.setUserId(currentUserId);

        executor.execute(() -> {
            pendienteDao.insertar(pendientesEntidad);
        });
    }

    public void eliminar(PendientesEntidad pendientesEntidad) {
        executor.execute(() -> {
            pendienteDao.eliminar(pendientesEntidad);
        });
    }

    public void eliminarPorIdApi(int idApi, String tipo) {
        executor.execute(() -> {
            pendienteDao.eliminarPorIdApi(idApi, tipo, currentUserId);
        });
    }

    public PendientesEntidad obtenerPendientesAleatorio() {
        return pendienteDao.obtenerAleatorio(currentUserId);
    }

    public LiveData<List<PendientesEntidad>> obtenertodo(){
        return pendienteDao.obtenerTodos(currentUserId);
    }
}
