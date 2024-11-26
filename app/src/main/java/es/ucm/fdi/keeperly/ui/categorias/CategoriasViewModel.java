package es.ucm.fdi.keeperly.ui.categorias;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.keeperly.repository.CategoriaRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CategoriasViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final CategoriaRepository categoriaRepository;

    public CategoriasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is categories fragment");
        this.categoriaRepository = RepositoryFactory.getInstance().getCategoriaRepository();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void crearCategoria(String nombre) {
        categoriaRepository.insert(categoriaRepository.construirCategoria(nombre)); // Insertamos el presupuesto en la base de datos

    }

    public LiveData<Integer> getOperationStatus() {
        return categoriaRepository.getOperationStatus();
    }
}