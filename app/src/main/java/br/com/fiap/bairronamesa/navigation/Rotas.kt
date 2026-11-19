package br.com.fiap.bairronamesa.navigation

object Rotas {
    const val INICIO = "inicio"
    const val OFERTAS = "ofertas"
    const val CADASTRO = "cadastro?id={id}"
    const val SEPARACAO = "separacao"

    fun cadastro(id: Long? = null) = if (id == null) "cadastro" else "cadastro?id=$id"
}
