package es.ucm.fdi.keeperly.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.repository.CuentaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.PresupuestoRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class InicioViewModel extends ViewModel {
    //Declaraciones de campos del fragment
    private final MutableLiveData<String> welcomeText;
    private final MutableLiveData<String> numDineroTotal;
    private final MutableLiveData<String> numTotalGastado;

    //Declaraciones de LiveData para los recyclerViews
    private final LiveData<List<Cuenta>> cuentas;
    private final LiveData<List<Presupuesto>> presupuestos;

    //Declaraciones de repositorios
    private final LoginRepository loginRepository;
    private final CuentaRepository cuentaRepository;
    private final PresupuestoRepository presupuestoRepository;

    public InicioViewModel() {

        //Obtencion de instancias de repositorios
        this.loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());
        this.cuentaRepository = RepositoryFactory.getInstance().getCuentaRepository();
        this.presupuestoRepository = RepositoryFactory.getInstance().getPresupuestoRepository();

        //Obtencion de LiveData para los recyclerViews
        this.cuentas = cuentaRepository.getAllCuentas(loginRepository.getLoggedUser().getId());
        this.presupuestos = presupuestoRepository.getAllPresupuestos(loginRepository.getLoggedUser().getId());

        //Actualizamos con un observer el valor de numDineroTotal
        cuentas.observeForever(new Observer<List<Cuenta>>() {
            @Override
            public void onChanged(List<Cuenta> cuentas) {
                double sumaTotal = 0;
                for (Cuenta cuenta : cuentas) {
                    sumaTotal += cuenta.getBalance();
                }
                numDineroTotal.postValue(String.format("%.2f€", sumaTotal));
            }
        });

        presupuestos.observeForever(new Observer<List<Presupuesto>>() {
            @Override
            public void onChanged(List<Presupuesto> presupuestos) {
                double sumaTotal = 0;
                for (Presupuesto presupuesto : presupuestos) {
                    sumaTotal += presupuesto.getGastado();
                }
                numTotalGastado.postValue(String.format("%.2f€", sumaTotal));
            }

        });



        //Construccion de los campos
        numDineroTotal = new MutableLiveData<>();

        numTotalGastado = new MutableLiveData<>();

        welcomeText = new MutableLiveData<>();
        welcomeText.setValue("Hola, " + loginRepository.getLoggedUser().getNombre());



    }

    public LiveData<String> getWelcomeText() {
        return welcomeText;
    }

    public LiveData<String> getNumDineroTotal() {
        return numDineroTotal ;
    }

    public LiveData<String> getNumTotalGastado() {
        return numTotalGastado;
    }

    public LiveData<List<Cuenta>> getCuentas() {
        return cuentas;
    }

    public LiveData<List<Presupuesto>> getPresupuestos() {
        return presupuestos;
    }
}