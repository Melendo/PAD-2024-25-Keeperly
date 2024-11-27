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
import es.ucm.fdi.keeperly.exceptions.usuario.UserAlreadyExistsException;
import es.ucm.fdi.keeperly.exceptions.usuario.UserNotFoundException;
import es.ucm.fdi.keeperly.exceptions.usuario.WrongPasswordException;


public class UsuarioRepository {


    private final UsuarioDAO usuarioDao;
    private final ExecutorService executorService;


    public UsuarioRepository() {
        usuarioDao = KeeperlyDB.getInstance().usuarioDao();
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
            Usuario usuario = usuarioDao.getUsuarioByEmail(username);
            if (usuario != null) {
                if (Objects.equals(usuario.getPw(), password)) {
                    return new Result.Success<Usuario>(usuario);
                } else {
                    return new Result.Error(new WrongPasswordException("Conraseña incorrecta"));
                }
            } else {
                return new Result.Error(new UserNotFoundException("El usuario no existe"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error al hacer login", e));
        }
    }

    public Result<Usuario> register(String email, String name, String password) {
        try {
            Usuario exists = usuarioDao.getUsuarioByEmail(email);
            if (exists == null) {
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setNombre(name);
                usuario.setPw(password);
                usuarioDao.insert(usuario);

                Usuario inserted = usuarioDao.getUsuarioByEmail(email);
                return new Result.Success<Usuario>(inserted);
            } else {
                return new Result.Error(new UserAlreadyExistsException("El correo ya está en uso"));
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error en el registro", e));
        }
    }

}
