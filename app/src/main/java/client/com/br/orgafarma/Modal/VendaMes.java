package client.com.br.orgafarma.Modal;

/**
 * Created by Felipe on 20/11/2015.
 */
public class VendaMes {

    private String pacote;
    private String meta;
    private String venda;
    private String COB;
    private String projecao;
    private String vazio;

    public VendaMes() {
    }

    public VendaMes(String pacote, String meta, String venda, String COB, String projecao, String vazio) {
        this.pacote = pacote;
        this.meta = meta;
        this.venda = venda;
        this.COB = COB;
        this.projecao = projecao;
        this.vazio = vazio;
    }

    public String getPacote() {
        return pacote;
    }

    public void setPacote(String pacote) {
        this.pacote = pacote;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getVenda() {
        return venda;
    }

    public void setVenda(String venda) {
        this.venda = venda;
    }

    public String getCOB() {
        return COB;
    }

    public void setCOB(String COB) {
        this.COB = COB;
    }

    public String getProjecao() {
        return projecao;
    }

    public void setProjecao(String projecao) {
        this.projecao = projecao;
    }

    public String getVazio() {
        return vazio;
    }

    public void setVazio(String vazio) {
        this.vazio = vazio;
    }
}
