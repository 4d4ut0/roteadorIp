package br.com.neto.adauto.model.enumeration;

public enum EnumCommands {

    HELP(0, "Lista de Comandos", "ajuda"),

    CREATE_ROUTER(1, "Criar Roteador", "roteador"),
    
    ROUTER_DETAIL(2, "Detalhes de Roteador", "detail"),
    
    STOP_ROUTER(3, "Parar Roteador", "stop"),

    ROUTER_LIST(4, "Lista de Roteadores Ativos", "list"),

    EMIT_MESSAGE(5, "Emitir Mensagem", "emissor"),
    
    ROUTER_LIST_DETAIL(6, "Lista de Roteadores Ativos(Detalhada)", "list_detail");;

    private Integer index;
    private String nome;
    private String comando;

    EnumCommands(Integer index, String nome, String comando) {
        this.index = index;
        this.nome = nome;
        this.comando = comando;
    }

    public Integer getIndex() {
        return index;
    }

    public String getNome() {
        return nome;
    }
    
    public String getComando() {
        return comando;
    }

}
