package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodolfo.rezende on 17/02/2016.
 */
public class CotacaoRepresentante implements Serializable{

    @SerializedName("codigo")
    private String mCodigo;

    @SerializedName("data")
    private String mData; //dia - mes - ano

    @SerializedName("codigo_representante")
    private String mCodigoRepresentante;

    @SerializedName("valor_total")
    private double mValorTotal;

    @SerializedName("quantidade")
    private double mQtdTotal;

    @SerializedName("cotacaoItens")
    private List<ItemCotacao> mCotacoesEnvio = new ArrayList<>();

    public CotacaoRepresentante(String mCodigo, String mData, String mCodigoRepresentante, double mValorTotal, double mQtdTotal, List<ItemCotacao> mCotacoesEnvio) {
        this.mCodigo = mCodigo;
        this.mData = mData;
        this.mCodigoRepresentante = mCodigoRepresentante;
        this.mValorTotal = mValorTotal;
        this.mQtdTotal = mQtdTotal;
        this.mCotacoesEnvio = mCotacoesEnvio;
    }

    public CotacaoRepresentante(){
    }

    public String getmCodigo() {
        return mCodigo;
    }

    public void setmCodigo(String mCodigo) {
        this.mCodigo = mCodigo;
    }

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }

    public String getmCodigoRepresentante() {
        return mCodigoRepresentante;
    }

    public void setmCodigoRepresentante(String mCodigoRepresentante) {
        this.mCodigoRepresentante = mCodigoRepresentante;
    }

    public double getmValorTotal() {
        return mValorTotal;
    }

    public void setmValorTotal(double mValorTotal) {
        this.mValorTotal = mValorTotal;
    }

    public double getmQtdTotal() {
        return mQtdTotal;
    }

    public void setmQtdTotal(double mQtdTotal) {
        this.mQtdTotal = mQtdTotal;
    }

    public List<ItemCotacao> getmCotacoesEnvio() {
        return mCotacoesEnvio;
    }

    public void setmCotacoesEnvio(List<ItemCotacao> mCotacoesEnvio) {
        this.mCotacoesEnvio = mCotacoesEnvio;
    }

}
