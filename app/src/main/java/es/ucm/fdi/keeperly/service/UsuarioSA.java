package es.ucm.fdi.keeperly.service;

import android.content.Context;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.UsuarioDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;


public class UsuarioSA {


    private final UsuarioDAO usuarioDao;
    private final ExecutorService executorService;
    private static Context context;

    public UsuarioSA() {
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

    public Result<Usuario> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication
            return null;
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
