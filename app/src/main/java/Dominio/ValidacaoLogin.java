package Dominio;

/**
 * Created by Felipe on 19/11/2015.
 */
public class ValidacaoLogin {
    private boolean valido;

    private boolean erro;

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
