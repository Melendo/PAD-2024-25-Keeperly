package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CategoriaDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;

public class CategoriaRepository {

    private static volatile CategoriaRepository instance;

    private final CategoriaDAO categoriaDao;
    private final ExecutorService executorService;

    private CategoriaRepository() {
        categoriaDao = KeeperlyDB.getInstance().categoriaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static CategoriaRepository getInsance() {
        if (instance == null)
            instance = new CategoriaRepository();
        return instance;
    }

    public void insert(Categoria categoria) {
        executorService.execute(() -> categoriaDao.insert(categoria));
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
}
