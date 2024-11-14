package es.ucm.fdi.keeperly.repository;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.service.UsuarioSA;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private UsuarioSA usuarioSA;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private Usuario user = null;

    // private constructor : singleton access
    private LoginRepository(UsuarioSA usuarioSA) {
        this.usuarioSA = usuarioSA;
    }

    public static LoginRepository getInstance(UsuarioSA usuarioSA) {
        if (instance == null) {
            instance = new LoginRepository(usuarioSA);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        usuarioSA.logout();
    }

    private void setLoggedInUser(Usuario user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<Usuario> login(String username, String password) {
        // handle login
        Result<Usuario> result = usuarioSA.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<Usuario>) result).getData());
        }
        return result;
    }
}