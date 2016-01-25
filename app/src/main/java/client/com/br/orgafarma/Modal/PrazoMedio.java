package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Felipe on 25/11/2015.
 */
public class PrazoMedio implements Serializable{

    @SerializedName("Meta")
    private String meta;

    @SerializedName("Atual")
    private String atual;

    @SerializedName("Variacao")
    private String variacao;

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getAtual() {
        return atual;
    }

    public void setAtual(String atual) {
        this.atual = atual;
    }

    public String getVariacao() {
        return variacao;
    }

    public void setVariacao(String variacao) {
        this.variacao = variacao;
    }
}
