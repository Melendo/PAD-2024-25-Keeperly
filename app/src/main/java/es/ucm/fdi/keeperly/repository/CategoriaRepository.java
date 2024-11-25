package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CategoriaDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;

public class CategoriaRepository {

    private final MutableLiveData<Integer> operationStatus = new MutableLiveData<>();


    private final CategoriaDAO categoriaDao;
    private final ExecutorService executorService;

    public CategoriaRepository() {
        categoriaDao = KeeperlyDB.getInstance().categoriaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Integer> getInsertStatus() {
        return operationStatus;
    }

    public void insert(Categoria categoria) {
        operationStatus.postValue(-1);
        if (categoria.getNombre().isEmpty()) {
            operationStatus.postValue(-2);

        } else {
            try {
                Categoria categoriaExiste = categoriaDao.getCategoriaByNombre(categoria.getNombre());
                if (categoriaExiste != null) {
                    operationStatus.postValue(-3);
                } else {
                    executorService.execute(() -> {
                        try {
                            categoriaDao.insert(categoria);
                            operationStatus.postValue(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Categoria categoria) {
        executorService.execute(() -> categoriaDao.update(categoria));
    }

    public void delete(Categoria categoria) {
        executorService.execute(() -> categoriaDao.delete(categoria));
    }

    public List<Categoria> getAllCategorias() {
        return categoriaDao.getAllCategorias();
    }

    public Categoria getCategoriaById(int id) {
        return categoriaDao.getCategoriaById(id);
    }

    public Categoria getCategoriaByNombre(String nombre) {
        return categoriaDao.getCategoriaByNombre(nombre);
    }
}
