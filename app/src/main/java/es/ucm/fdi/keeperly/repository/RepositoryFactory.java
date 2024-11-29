package es.ucm.fdi.keeperly.repository;

public class RepositoryFactory {

    private static volatile RepositoryFactory instance;

    public static RepositoryFactory getInstance(){
        if(instance == null){
            instance = new RepositoryFactory();
        }
        return instance;
    }

    public CategoriaRepository getCategoriaRepository(){
        return new CategoriaRepository();
    }

    public  CuentaRepository getCuentaRepository(){
        return new CuentaRepository();
    }

    public PresupuestoRepository getPresupuestoRepository(){
        return new PresupuestoRepository();
    }

    public TransaccionRepository getTransactionRepository(){
        return new TransaccionRepository();
    }

    public UsuarioRepository getUsuarioRepository(){
        return new UsuarioRepository();
    }

}
