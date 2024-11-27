package es.ucm.fdi.keeperly.ui.categorias;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.repository.CategoriaRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CategoriasViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final CategoriaRepository categoriaRepository;
    private final LiveData<List<Categoria>> categorias;


    public CategoriasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is categories fragment");
        this.categoriaRepository = RepositoryFactory.getInstance().getCategoriaRepository();
        this.categorias = categoriaRepository.getAllCategorias();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void crearCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoriaRepository.insert(categoria); // Insertamos el presupuesto en la base de datos
    }

    public LiveData<List<Categoria>> getCategorias() {
        return categorias;
    }

    public LiveData<Integer> getOperationStatus() {
        return categoriaRepository.getOperationStatus();
    }
}