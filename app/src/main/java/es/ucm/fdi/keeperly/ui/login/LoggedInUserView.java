package es.ucm.fdi.keeperly.ui.login;

/**
 * Clase para datos de la UI
 */
class LoggedInUserView {
    private String displayName;
    // TODO : Añadir otros datos

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}