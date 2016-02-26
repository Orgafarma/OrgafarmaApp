package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 22/02/2016.
 */
public class ItemVerCotacao implements Serializable{

    @SerializedName("codigo_cotacao")
    private String codigoCotacao;

    @SerializedName("data_cadastro")
    private String data;

    @SerializedName("quantidade_total")
    private String qtdItens;

    @SerializedName("valor_total")
    private String vlrTotal;

    @SerializedName("status")
    private String status;


    public ItemVerCotacao(String data, String qtdItens, String vlrTotal, String status) {

        this.data = data;
        this.qtdItens = qtdItens;
        this.vlrTotal = vlrTotal;
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQtdItens() {
        return qtdItens;
    }

    public void setQtdItens(String qtdItens) {
        this.qtdItens = qtdItens;
    }

    public String getVlrTotal() {
        return vlrTotal;
    }

    public void setVlrTotal(String vlrTotal) {
        this.vlrTotal = vlrTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
