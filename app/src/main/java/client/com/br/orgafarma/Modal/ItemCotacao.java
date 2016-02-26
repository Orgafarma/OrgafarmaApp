package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 07/02/2016.
 */
public class ItemCotacao implements Serializable{

    public ItemCotacao(String mCodProduto, String mProduto, double mValor, int mQtd, boolean mIsGenerico) {
        this.mCodProduto = mCodProduto;
        this.mProduto = mProduto;
        this.mValor = mValor;
        this.mQtd = mQtd;
        this.mIsGenerico = mIsGenerico;
        this.mValorTotal = mQtd * mValor;
    }

    @SerializedName("codigo_produto")
    private String mCodProduto;

    @SerializedName("produto")
    private String mProduto;

    @SerializedName("valor")
    private double mValor;

    @SerializedName("qtd")
    private int mQtd;

    @SerializedName("isGenerico")
    private boolean mIsGenerico;

    @SerializedName("valorTotal")
    private double mValorTotal;

    public void setmValor(double mValor) {
        this.mValor = mValor;
    }

    public double getmValorTotal() {
        mValorTotal = getmQtd() * getmValor();
        return mValorTotal;
    }

    public void setmValorTotal(double mValorTotal) {
        this.mValorTotal = mValorTotal;
    }

    public String getmProduto() {
        return mProduto;
    }

    public void setmProduto(String mProduto) {
        this.mProduto = mProduto;
    }

    public double getmValor() {
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
        return ((ItemCotacao) o).getmProduto().equals(this.getmProduto()) && ((ItemCotacao) o).ismIsGenerico() == this.ismIsGenerico();
    }
}
