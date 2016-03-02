package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 29/02/2016.
 */
public class ItemVerCotacaoEspecifico implements Serializable{
    private static final long serialVersionUID = 42L;

    @SerializedName("codigo_cotacao")
    private String codProduto;

    @SerializedName("produto")
    private String produto;

    @SerializedName("qtd")
    private String qtd;

    @SerializedName("preco")
    private String precoSugerido;

    public ItemVerCotacaoEspecifico(String codProduto, String produto, String qtd, String precoSugerido) {

        this.codProduto = codProduto;
        this.produto = produto;
        this.qtd = qtd;
        this.precoSugerido = precoSugerido;
    }

    public String getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(String codProduto) {
        this.codProduto = codProduto;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public String getPrecoSugerido() {
        return precoSugerido;
    }

    public void setPrecoSugerido(String precoSugerido) {
        this.precoSugerido = precoSugerido;
    }
}
