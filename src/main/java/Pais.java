import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum Pais implements Serializable {
    AFGANISTAO("Afeganistão"),
    ALBANIA("Albânia"),
    ARGELIA("Argélia"),
    ARGENTINA("Argentina"),
    AUSTRALIA("Austrália"),
    AUSTRIA("Áustria"),
    BANGLADESH("Bangladesh"),
    BELGICA("Bélgica"),
    BRASIL("Brasil"),
    CANADA("Canadá"),
    CHINA("China"),
    COLÔMBIA("Colômbia"),
    CROACIA("Croácia"),
    CUBA("Cuba"),
    REPUBLICA_TCHECA("República Tcheca"),
    DINAMARCA("Dinamarca"),
    EGITO("Egito"),
    ETIOPIA("Etiópia"),
    FINLANDIA("Finlândia"),
    FRANCA("França"),
    ALEMANHA("Alemanha"),
    GRECIA("Grécia"),
    INDIA("Índia"),
    INDONESIA("Indonésia"),
    IRA("Irã"),
    IRAQUE("Iraque"),
    IRLANDA("Irlanda"),
    ISRAEL("Israel"),
    ITALIA("Itália"),
    JAPAO("Japão"),
    QUENIA("Quênia"),
    MALASIA("Malásia"),
    MEXICO("México"),
    MARROCOS("Marrocos"),
    PAISES_BAIXOS("Países Baixos"),
    NOVA_ZELANDIA("Nova Zelândia"),
    NIGERIA("Nigéria"),
    NORUEGA("Noruega"),
    PAQUISTAO("Paquistão"),
    PERU("Peru"),
    FILIPINAS("Filipinas"),
    POLONIA("Polônia"),
    PORTUGAL("Portugal"),
    RUSSIA("Rússia"),
    ARABIA_SAUDITA("Arábia Saudita"),
    AFRICA_DO_SUL("África do Sul"),
    COREIA_DO_SUL("Coreia do Sul"),
    ESPANHA("Espanha"),
    SUECIA("Suécia"),
    SUICA("Suíça"),
    TURQUIA("Turquia"),
    REINO_UNIDO("Reino Unido"),
    ESTADOS_UNIDOS("Estados Unidos da América");

    private final String nomeCompleto;

    Pais(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
}

