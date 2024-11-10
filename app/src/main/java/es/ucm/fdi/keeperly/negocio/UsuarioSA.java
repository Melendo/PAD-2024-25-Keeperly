package es.ucm.fdi.keeperly.negocio;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.integracion.KeeperlyDB;
import es.ucm.fdi.keeperly.integracion.daos.UsuarioDAO;
import es.ucm.fdi.keeperly.integracion.entities.Usuario;


public class UsuarioSA {


    private final UsuarioDAO usuarioDao;
    private final ExecutorService executorService;

    public UsuarioSA(Context context) {
        KeeperlyDB db = KeeperlyDB.getInstance(context);
        usuarioDao = db.usuarioDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Usuario usuario) {
        executorService.execute(() -> usuarioDao.insert(usuario));
    }

    public void update(Usuario usuario) {
        executorService.execute(() -> usuarioDao.update(usuario));
    }

    public void delete(Usuario usuario) {
        executorService.execute(() -> usuarioDao.delete(usuario));
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioDao.getAllUsuarios();
    }

    public Usuario getUsuarioById(int id) {
        return usuarioDao.getUsuarioById(id);
    }
}