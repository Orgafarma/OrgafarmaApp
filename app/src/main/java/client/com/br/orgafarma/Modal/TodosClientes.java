package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rodolfo.rezende on 26/02/2016.
 */
public class TodosClientes implements Serializable {
    @SerializedName("clientes")
    private List<BuscarCliente> clientes;

    public List<BuscarCliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<BuscarCliente> clientes) {
        this.clientes = clientes;
    }
}
