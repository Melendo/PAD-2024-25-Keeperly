package es.ucm.fdi.keeperly.repository;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;

public class LoginRepository {

    private static volatile LoginRepository instance;

    private UsuarioRepository usuarioRepository;


    private Usuario user = null;

    private LoginRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static LoginRepository getInstance(UsuarioRepository usuarioRepository) {
        if (instance == null) {
            instance = new LoginRepository(usuarioRepository);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    private void setLoggedInUser(Usuario user) {
        this.user = user;

    }

    public Result<Usuario> login(String username, String password) {
        Result<Usuario> result = usuarioRepository.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<Usuario>) result).getData());
        }
        return result;
    }

    public Usuario getLoggedUser() {
        if (user == null) {
            throw new IllegalStateException("No se encontro ningun usuario");
        }
        return user;
    }
}