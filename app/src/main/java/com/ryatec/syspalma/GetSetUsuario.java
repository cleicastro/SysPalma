package com.ryatec.syspalma;

public class GetSetUsuario {

    private static String nome;
    private static String usuario;
    private static String funcao;
    private static String tipo;
    private static String matricula;
    private static String departamento;

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        GetSetUsuario.nome = nome;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        GetSetUsuario.usuario = usuario;
    }

    public static String getFuncao() {
        return funcao;
    }

    public static void setFuncao(String funcao) {
        GetSetUsuario.funcao = funcao;
    }

    public static String getTipo() {
        return tipo;
    }

    public static void setTipo(String tipo) {
        GetSetUsuario.tipo = tipo;
    }

    public static String getMatricula() {
        return matricula;
    }

    public static void setMatricula(String matricula) {
        GetSetUsuario.matricula = matricula;
    }

    public static String getDepartamento() {
        return departamento;
    }

    public static void setDepartamento(String departamento) {
        GetSetUsuario.departamento = departamento;
    }
}
