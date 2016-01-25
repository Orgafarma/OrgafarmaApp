package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 19/01/2016.
 */
public class Pacote implements Serializable{

    @SerializedName("Pacote")
    private String pacote;

    @SerializedName("Vendedor")
    private double vendedor;

    @SerializedName("Televendas")
    private double televenda;

    public String getPacote() {
        return pacote;
    }

    public void setPacote(String pacote) {
        this.pacote = pacote;
    }

    public double getVendedor() {
        return vendedor;
    }

    public void setVendedor(double vendedor) {
        this.vendedor = vendedor;
    }

    public double getTelevenda() {
        return televenda;
    }

    public void setTelevenda(double televenda) {
        this.televenda = televenda;
    }
}
