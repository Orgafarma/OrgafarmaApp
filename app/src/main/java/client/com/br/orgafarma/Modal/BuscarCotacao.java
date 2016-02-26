package client.com.br.orgafarma.Modal;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rodolfo.rezende on 25/02/2016.
 */
public class BuscarCotacao implements Serializable{
    @SerializedName("codigo_representante")
    private String mCodRepresentacao;

    @SerializedName("data_inicio")
    private String mDataInicial;

    @SerializedName("data_fim")
    private String mDataFim;

    @SerializedName("status")
    private String mStatus;

    public BuscarCotacao(String mCodRepresentacao, String mDataInicial, String mDataFim, String mStatus) {
        this.mCodRepresentacao = mCodRepresentacao;
        this.mDataInicial = mDataInicial;
        this.mDataFim = mDataFim;
        this.mStatus = mStatus;
    }

    public String getmCodRepresentacao() {
        return mCodRepresentacao;
    }

    public void setmCodRepresentacao(String mCodRepresentacao) {
        this.mCodRepresentacao = mCodRepresentacao;
    }

    public String getmDataInicial() {
        return mDataInicial;
    }

    public void setmDataInicial(String mDataInicial) {
        this.mDataInicial = mDataInicial;
    }

    public String getmDataFim() {
        return mDataFim;
    }

    public void setmDataFim(String mDataFim) {
        this.mDataFim = mDataFim;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
