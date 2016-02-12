package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 07/02/2016.
 */
public class CotacaoEnvio implements Serializable{

    public CotacaoEnvio(String mProduto, int mValor, int mQtd, boolean mIsGenerico) {
        this.mProduto = mProduto;
        this.mValor = mValor;
        this.mQtd = mQtd;
        this.mIsGenerico = mIsGenerico;
    }

    @SerializedName("produto")
    private String mProduto;

    @SerializedName("valor")
    private int mValor;

    @SerializedName("qtd")
    private int mQtd;

    @SerializedName("isGenerico")
    private boolean mIsGenerico;

    public String getmProduto() {
        return mProduto;
    }

    public void setmProduto(String mProduto) {
        this.mProduto = mProduto;
    }

    public int getmValor() {
        return mValor;
    }

    public void setmValor(int mValor) {
        this.mValor = mValor;
    }

    public int getmQtd() {
        return mQtd;
    }

    public void setmQtd(int mQtd) {
        this.mQtd = mQtd;
    }

    public boolean ismIsGenerico() {
        return mIsGenerico;
    }

    public void setmIsGenerico(boolean mIsGenerico) {
        this.mIsGenerico = mIsGenerico;
    }

    @Override
    public boolean equals(Object o) {
        return ((CotacaoEnvio) o).getmProduto().equals(this.getmProduto()) && ((CotacaoEnvio) o).ismIsGenerico() == this.ismIsGenerico();
    }
}
