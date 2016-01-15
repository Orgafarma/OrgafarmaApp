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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("mensagem")
    private String mensagem;

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
