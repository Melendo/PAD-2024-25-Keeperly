package es.ucm.fdi.keeperly.ui.login;

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