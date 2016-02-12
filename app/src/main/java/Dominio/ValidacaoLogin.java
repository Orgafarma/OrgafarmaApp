package Dominio;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Felipe on 19/11/2015.
 */
public class ValidacaoLogin implements Serializable{

    @SerializedName("login")
    private boolean valido;

    @SerializedName("erro")
    private boolean erro;

    @SerializedName("token")
    private String token;

    @SerializedName("mensagem")
    private String mensagem;

    @SerializedName("id_representante")
    private int idRepresentante;

    @SerializedName("codigo_representante")
    private int codRepresentante;

    public int getIdRepresentante() {
        return idRepresentante;
    }

    public void setIdRepresentante(int idRepresentante) {
        this.idRepresentante = idRepresentante;
    }

    public int getCodRepresentante() {
        return codRepresentante;
    }

    public void setCodRepresentante(int codRepresentante) {
        this.codRepresentante = codRepresentante;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
