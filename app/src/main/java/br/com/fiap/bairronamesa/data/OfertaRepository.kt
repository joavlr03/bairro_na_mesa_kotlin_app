package br.com.fiap.bairronamesa.data

class OfertaRepository(private val dao: OfertaDao) {
    fun listarTodas() = dao.listarTodas()
    fun buscarPorId(id: Long) = dao.buscarPorId(id)
    fun listarReservadas() = dao.listarReservadas()
    suspend fun inserir(oferta: Oferta) = dao.inserir(oferta)
    suspend fun atualizar(oferta: Oferta) = dao.atualizar(oferta)
    suspend fun excluir(oferta: Oferta) = dao.excluir(oferta)
}
