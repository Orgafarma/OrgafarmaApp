package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 29/01/2016.
 */
public class Produto implements Serializable {

    @SerializedName("descricao")
    private String mDescricao;

    @SerializedName("codigo")
    private double mCodigo;

    public String getmDescricao() {
        return mDescricao;
    }

    public void setmDescricao(String mDescricao) {
        this.mDescricao = mDescricao;
    }

    public double getmCodigo() {
        return mCodigo;
    }

    public void setmCodigo(double mCodigo) {
        this.mCodigo = mCodigo;
    }
}
