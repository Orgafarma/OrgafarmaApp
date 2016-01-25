package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rodolfo.rezende on 19/01/2016.
 */
public class VendaMesTel implements Serializable {

    @SerializedName("Logistica")
    private double logistica;

    @SerializedName("Pacotes")
    List<Pacote> pacotes;

    @SerializedName("PrazoMedio")
    List<PrazoMedio> prazoMedios;

    public List<PrazoMedio> getPrazoMedios() {
        return prazoMedios;
    }

    public void setPrazoMedios(List<PrazoMedio> prazoMedios) {
        this.prazoMedios = prazoMedios;
    }

    public List<Pacote> getPacotes() {
        return pacotes;
    }

    public void setPacotes(List<Pacote> pacotes) {
        this.pacotes = pacotes;
    }

    public double getLogistica() {
        return logistica;
    }

    public void setLogistica(double logistica) {
        this.logistica = logistica;
    }
}
