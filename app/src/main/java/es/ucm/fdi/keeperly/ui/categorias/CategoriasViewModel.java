package es.ucm.fdi.keeperly.ui.categorias;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.repository.CategoriaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;
import es.ucm.fdi.keeperly.repository.UsuarioRepository;

public class CategoriasViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final CategoriaRepository categoriaRepository;
    private final LoginRepository loginRepository;
    private final LiveData<List<Categoria>> categorias;


    public CategoriasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is categories fragment");
        this.categoriaRepository = RepositoryFactory.getInstance().getCategoriaRepository();
        this.loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());
        this.categorias = categoriaRepository.getAllCategoriasDeUsuario(loginRepository.getLoggedUser().getId());
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void crearCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setId_usuario(loginRepository.getLoggedUser().getId());
        categoriaRepository.insert(categoria);
    }

    public LiveData<List<Categoria>> getCategorias() {
        return categorias;
    }


    public Categoria getCategoriaByNombre(String nombre) {
        return categoriaRepository.getCategoriaByNombre(nombre);
    }

    public void delete(Categoria categoria) {
        categoriaRepository.delete(categoria);
    }

    public void update(Categoria categoria) {
        categoriaRepository.update(categoria);
    }

    public LiveData<Integer> getOperationStatus() {
        return categoriaRepository.getOperationStatus();
    }

    public LiveData<Integer> getDeleteStatus() {
        return categoriaRepository.getDeleteStatus();
    }

    public LiveData<Integer> getUpdateStatus() {
        return categoriaRepository.getUpdateStatus();
    }

    public Categoria getCategoriaById(int categoriaId) {
        return categoriaRepository.getCategoriaById(categoriaId);
    }
}