package es.ucm.fdi.keeperly.repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.UsuarioDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.exceptions.usuario.UserNotFoundException;
import es.ucm.fdi.keeperly.exceptions.usuario.WrongPasswordException;


public class UsuarioRepository {

    private static volatile UsuarioRepository instance;

    private final UsuarioDAO usuarioDao;
    private final ExecutorService executorService;


    private UsuarioRepository() {
        usuarioDao = KeeperlyDB.getInstance().usuarioDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static UsuarioRepository getInstance() {
        if (instance == null)
            instance = new UsuarioRepository();
        return instance;
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
            Usuario usuario = usuarioDao.getUsuarioByEmail(username);
            if (usuario != null) {
                if (Objects.equals(usuario.getPw(), password)) {
                    return new Result.Success<Usuario>(usuario);
                } else {
                    return new Result.Error(new WrongPasswordException("Wrong pw"));
                }
            } else {
                return new Result.Error(new UserNotFoundException("User not found in database"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

}
