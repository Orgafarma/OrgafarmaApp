package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rodolfo.rezende on 18/01/2016.
 */
public class VendaMes implements Serializable {
    @SerializedName("Vendedor")
    private String vendedor;

    @SerializedName("Mes")
    private List<Mes> mes;

    @SerializedName("Logistica")
    private List<Logistica> logistica;

    @SerializedName("Positivados")
    private List<Positivacao> positivados;

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public List<Mes> getMes() {
        return mes;
    }

    public void setMes(List<Mes> mes) {
        this.mes = mes;
    }

    public List<Logistica> getLogistica() {
        return logistica;
    }

    public void setLogistica(List<Logistica> logistica) {
        this.logistica = logistica;
    }

    public List<Positivacao> getPositivados() {
        return positivados;
    }

    public void setPositivados(List<Positivacao> positivados) {
        this.positivados = positivados;
    }
}
