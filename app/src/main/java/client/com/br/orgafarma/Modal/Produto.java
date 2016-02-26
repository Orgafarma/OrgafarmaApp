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
    private String mCodigo;

    public String getmDescricao() {
        return mDescricao;
    }

    public void setmDescricao(String mDescricao) {
        this.mDescricao = mDescricao;
    }

    public String getmCodigo() {
        return mCodigo;
    }

    public void setmCodigo(String mCodigo) {
        this.mCodigo = mCodigo;
    }
}
