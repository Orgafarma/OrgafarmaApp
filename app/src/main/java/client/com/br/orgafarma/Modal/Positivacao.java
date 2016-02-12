package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Felipe on 25/11/2015.
 */
public class Positivacao implements Serializable {

    @SerializedName("BaseClientes")
    private String baseClientes;

    @SerializedName("Meta")
    private String meta;

    @SerializedName("Positivados")
    private String positivados;

    @SerializedName("COB")
    private String cob;

    @SerializedName("Projecao")
    private String projecao;

    @SerializedName("Vazio")
    private String vazio;

    @SerializedName("Pacote")
    private String pacote;

    @SerializedName("VendaMes")
    private String venda;

    public String getBaseClientes() {
        return baseClientes;
    }

    public void setBaseClientes(String baseClientes) {
        this.baseClientes = baseClientes;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getPositivados() {
        return positivados;
    }

    public void setPositivados(String positivados) {
        this.positivados = positivados;
    }

    public String getCob() {
        return cob;
    }

    public void setCob(String cob) {
        this.cob = cob;
    }

    public String getProjecao() {
        return projecao;
    }

    public void setProjecao(String projecao) {
        this.projecao = projecao;
    }

    public String getVazio() {
        return vazio;
    }

    public void setVazio(String vazio) {
        this.vazio = vazio;
    }
}
