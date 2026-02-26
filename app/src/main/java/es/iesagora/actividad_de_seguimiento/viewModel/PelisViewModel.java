package es.iesagora.actividad_de_seguimiento.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.iesagora.actividad_de_seguimiento.api_rest.Resource;
import es.iesagora.actividad_de_seguimiento.model.Peliculas;
import es.iesagora.actividad_de_seguimiento.repository.PelisRepository;

public class PelisViewModel extends ViewModel {
    private final PelisRepository repository;
    private final MutableLiveData<Resource<List<Peliculas>>> pelisResult = new MutableLiveData<>();

    public PelisViewModel() {
        repository = new PelisRepository();
        fetchPelis();
    }

    public LiveData<Resource<List<Peliculas>>> getPelisResult() { return pelisResult; }

    public void fetchPelis() {
        repository.getPelisList(result -> pelisResult.setValue(result));
    }
}