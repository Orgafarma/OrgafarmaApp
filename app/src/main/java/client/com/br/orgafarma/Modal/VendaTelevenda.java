package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 19/01/2016.
 */
public class VendaTelevenda implements Serializable {


    @SerializedName("TokenValido")
    private boolean tokenValido;

    @SerializedName("Vendas")
    private VendaMesTel vendaMesTel;

    public boolean isTokenValido() {
        return tokenValido;
    }

    public void setTokenValido(boolean tokenValido) {
        this.tokenValido = tokenValido;
    }

    public VendaMesTel getVendaMesTel() {
        return vendaMesTel;
    }

    public void setVendaMesTel(VendaMesTel vendaMesTel) {
        this.vendaMesTel = vendaMesTel;
    }
}
