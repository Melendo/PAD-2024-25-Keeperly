package es.ucm.fdi.keeperly.repository;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;

public class RegisterRepository {

    private UsuarioRepository usuarioRepository;

    public RegisterRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Result<Usuario> register(String email, String name, String password) {
        return null;
    }
}
