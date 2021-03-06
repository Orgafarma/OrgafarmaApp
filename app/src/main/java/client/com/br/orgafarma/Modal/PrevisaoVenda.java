package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 18/01/2016.
 */
public class PrevisaoVenda implements Serializable{

    @SerializedName("TokenValido")
    private boolean tokenValido;

    @SerializedName("Vendas")
    private VendaMes venda;

    public boolean isTokenValido() {
        return tokenValido;
    }

    public void setTokenValido(boolean tokenValido) {
        this.tokenValido = tokenValido;
    }

    public VendaMes getVenda() {
        return venda;
    }

    public void setVenda(VendaMes venda) {
        this.venda = venda;
    }
}
