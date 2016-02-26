package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 26/02/2016.
 */

public class BuscarCliente implements Serializable {

    @SerializedName("razao_social_cliente")
    private String clienteNome;

    @SerializedName("codigo_entidade")
    private String codCliente;

    public BuscarCliente(String clienteNome, String codCliente) {
        this.clienteNome = clienteNome;
        this.codCliente = codCliente;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }
}
