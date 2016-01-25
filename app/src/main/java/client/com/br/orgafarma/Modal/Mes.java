package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rodolfo.rezende on 18/01/2016.
 */
public class Mes {

    @SerializedName("Venda")
    private String venda;

    @SerializedName("Meta")
    private String meta;

    @SerializedName("COB")
    private String cob;

    @SerializedName("Projecao")
    private String projecao;

    @SerializedName("Vazio")
    private String vazio;

    @SerializedName("Pacote")
    private String pacote;

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
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

    public String getPacote() {
        return pacote;
    }

    public void setPacote(String pacote) {
        this.pacote = pacote;
    }

    public String getVenda() {
        return venda;
    }

    public void setVenda(String venda) {
        this.venda = venda;
    }

}
