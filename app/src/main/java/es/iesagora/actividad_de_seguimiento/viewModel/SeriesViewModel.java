package es.iesagora.actividad_de_seguimiento.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.iesagora.actividad_de_seguimiento.api_rest.Resource;
import es.iesagora.actividad_de_seguimiento.model.Series;
import es.iesagora.actividad_de_seguimiento.repository.SeriesRepository;

public class SeriesViewModel extends ViewModel {
    private final SeriesRepository repository;
    private final MutableLiveData<Resource<List<Series>>> seriesResult = new MutableLiveData<>();

    public SeriesViewModel() {
        repository = new SeriesRepository();
        fetchSeries();
    }

    public LiveData<Resource<List<Series>>> getSeriesResult() { return seriesResult; }

    public void fetchSeries() {
        repository.getSeriesList(result -> seriesResult.setValue(result));
    }
}