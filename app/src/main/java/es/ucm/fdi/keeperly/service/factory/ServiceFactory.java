package es.ucm.fdi.keeperly.service.factory;

public abstract class ServiceFactory {

    private static ServiceFactory instance;

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactoryImp();
        }
        return instance;
    }
}
