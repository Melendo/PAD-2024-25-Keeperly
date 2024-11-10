package es.ucm.fdi.keeperly.negocio;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.integracion.KeeperlyDB;
import es.ucm.fdi.keeperly.integracion.daos.CategoriaDAO;
import es.ucm.fdi.keeperly.integracion.entities.Categoria;

public class CategoriaSA {
    private final CategoriaDAO categoriaDao;
    private final ExecutorService executorService;

    public CategoriaSA(Context context) {
        KeeperlyDB db = KeeperlyDB.getInstance(context);
        categoriaDao = db.categoriaDao();
        executorService = Executors.newSingleThreadExecutor();
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
