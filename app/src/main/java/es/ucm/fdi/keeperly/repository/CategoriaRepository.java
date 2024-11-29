package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CategoriaDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;

public class CategoriaRepository {

    private final MutableLiveData<Integer> operationStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> deleteStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> updateStatus = new MutableLiveData<>();


    private final CategoriaDAO categoriaDao;
    private final ExecutorService executorService;

    public CategoriaRepository() {
        categoriaDao = KeeperlyDB.getInstance().categoriaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Categoria categoria) {
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
                            operationStatus.postValue(-1);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Categoria categoria) {
        if (categoria.getNombre().isEmpty()) {
            updateStatus.postValue(-2);

        } else {
            try {
                Categoria existeCategoria = categoriaDao.getCategoriaByNombre(categoria.getNombre());
                if(existeCategoria == null || (existeCategoria != null && existeCategoria.getId() == categoria.getId())){
                    executorService.execute(() -> categoriaDao.update(categoria));
                    updateStatus.postValue(1);
                }else{
                    updateStatus.postValue(-3);
                }
            }catch (Exception e){
                e.printStackTrace();
                updateStatus.postValue(-1);
            }
        }
    }

    public void delete(Categoria categoria) {
        PresupuestoDAO presupuestoDAO = KeeperlyDB.getInstance().presupuestoDao();
        TransaccionDAO transaccionDAO = KeeperlyDB.getInstance().transaccionDao();
        deleteStatus.postValue(-1);
        try {
            if (!presupuestoDAO.getPresupuestosPorCategoria(categoria.getId()).isEmpty()) {
                deleteStatus.postValue(-2);
            } else {
                if(transaccionDAO.getTransaccionesPorCategoria(categoria.getId()).isEmpty()) {
                    executorService.execute(() -> categoriaDao.delete(categoria));
                    deleteStatus.postValue(1);
                }else{
                    deleteStatus.postValue(-3);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            deleteStatus.postValue(-1);
        }

    }

    public LiveData<List<Categoria>> getAllCategorias() {
        return categoriaDao.getAllCategorias();
    }

    public Categoria getCategoriaById(int id) {
        return categoriaDao.getCategoriaById(id);
    }

    public Categoria getCategoriaByNombre(String nombre) {
        return categoriaDao.getCategoriaByNombre(nombre);
    }

    public LiveData<Integer> getOperationStatus() {
        return operationStatus;
    }

    public LiveData<Integer> getDeleteStatus() {
        return deleteStatus;
    }

    public LiveData<Integer> getUpdateStatus() {
        return updateStatus;
    }
}
