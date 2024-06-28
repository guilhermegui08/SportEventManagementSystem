public enum Estado {
    POR_INICIAR,
    INICIADO,
    TERMINADO_DESCL,
    TERMINADO;

    @Override
    public String toString() {
        switch (this) {
            case POR_INICIAR:
                return "Por Iniciar";
            case INICIADO:
                return "Iniciado";
            case TERMINADO:
                return "Terminado";
            case TERMINADO_DESCL:
                return "Desclacificação";
            default:
                return super.toString();
        }
    }
}
