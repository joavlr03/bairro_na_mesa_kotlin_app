package br.com.fiap.bairronamesa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.bairronamesa.data.Oferta
import br.com.fiap.bairronamesa.data.OfertaRepository
import br.com.fiap.bairronamesa.data.StatusOferta
import br.com.fiap.bairronamesa.ui.components.numeroKg
import br.com.fiap.bairronamesa.ui.components.numeroPreco
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

data class ResumoImpacto(
    val kgOfertados: Double = 0.0,
    val kgVendidos: Double = 0.0,
    val rendaEstimada: Double = 0.0,
    val totalReservadas: Int = 0
)

data class FormOfertaState(
    val id: Long = 0,
    val produto: String = "",
    val categoria: String = "",
    val quantidade: String = "",
    val preco: String = "",
    val feira: String = "",
    val status: String = StatusOferta.DISPONIVEL,
    val criadaEm: Long = 0,
    val erroProduto: String? = null,
    val erroCategoria: String? = null,
    val erroQuantidade: String? = null,
    val erroPreco: String? = null,
    val erroFeira: String? = null,
) {
    val editando get() = id != 0L
    // Teclado brasileiro usa vírgula decimal
    val quantidadeKg get() = quantidade.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 && it <= 1000 }
    val precoKg get() = preco.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it > 0 && it <= 500 }
    val valorTotal: Double? get() = quantidadeKg?.let { q -> precoKg?.let { q * it } }
    val valido get() = listOf(erroProduto, erroCategoria, erroQuantidade, erroPreco, erroFeira).all { it == null }

    fun validado() = copy(
        erroProduto = if (produto.trim().length !in 2..40) "Informe o nome do produto" else null,
        erroCategoria = if (categoria.isBlank()) "Escolha uma categoria" else null,
        erroQuantidade = if (quantidadeKg == null) "Informe uma quantidade maior que zero" else null,
        erroPreco = if (precoKg == null) "Informe um preço válido" else null,
        erroFeira = if (feira.isBlank()) "Escolha a feira" else null,
    )
}

class OfertaViewModel(private val repository: OfertaRepository) : ViewModel() {
    private val parar = SharingStarted.WhileSubscribed(5000)
    private val todas = repository.listarTodas()

    val ofertas: StateFlow<List<Oferta>> = todas.stateIn(viewModelScope, parar, emptyList())

    private val _filtroStatus = MutableStateFlow<String?>(null)
    val filtroStatus = _filtroStatus.asStateFlow()

    // null = ainda carregando (evita piscar o estado vazio na abertura)
    val ofertasFiltradas: StateFlow<List<Oferta>?> = combine(todas, _filtroStatus) { lista, filtro ->
        if (filtro == null) lista else lista.filter { it.status == filtro }
    }.stateIn(viewModelScope, parar, null)

    val resumo: StateFlow<ResumoImpacto> = ofertas.map { lista ->
        val vendidas = lista.filter { it.status == StatusOferta.VENDIDO }
        ResumoImpacto(
            kgOfertados = lista.sumOf { it.quantidadeKg },
            kgVendidos = vendidas.sumOf { it.quantidadeKg },
            rendaEstimada = vendidas.sumOf { it.valorTotal },
            totalReservadas = lista.count { it.status == StatusOferta.RESERVADO }
        )
    }.stateIn(viewModelScope, parar, ResumoImpacto())

    val reservadasPorFeira: StateFlow<Map<String, List<Oferta>>?> = repository.listarReservadas()
        .map { it.groupBy(Oferta::feira) }
        .stateIn(viewModelScope, parar, null)

    private val _formState = MutableStateFlow(FormOfertaState())
    val formState = _formState.asStateFlow()

    // Channel: mensagem de uso único (StateFlow reapareceria ao girar a tela)
    private val _eventos = Channel<String>(Channel.BUFFERED)
    val eventos = _eventos.receiveAsFlow()

    fun definirFiltro(status: String?) {
        _filtroStatus.value = status
    }

    fun atualizarForm(mudanca: (FormOfertaState) -> FormOfertaState) = _formState.update(mudanca)

    fun limparFormulario() {
        _formState.value = FormOfertaState()
    }

    fun carregarParaEdicao(id: Long) {
        viewModelScope.launch {
            val o = repository.buscarPorId(id).first() ?: return@launch
            _formState.value = FormOfertaState(
                id = o.id, produto = o.produto, categoria = o.categoria,
                quantidade = numeroKg(o.quantidadeKg), preco = numeroPreco(o.precoKg),
                feira = o.feira, status = o.status, criadaEm = o.criadaEm
            )
        }
    }

    /** Valida; se tudo certo grava e devolve true. Com erro não grava nada. */
    fun salvar(): Boolean {
        val f = _formState.updateAndGet { it.validado() }
        if (!f.valido) return false
        val oferta = Oferta(
            id = f.id, produto = f.produto.trim(), categoria = f.categoria,
            quantidadeKg = f.quantidadeKg!!, precoKg = f.precoKg!!, feira = f.feira,
            status = f.status, // na edição o status atual é mantido
            criadaEm = if (f.editando) f.criadaEm else System.currentTimeMillis()
        )
        viewModelScope.launch {
            if (f.editando) repository.atualizar(oferta) else repository.inserir(oferta)
            _eventos.send("Oferta salva")
        }
        return true
    }

    fun avancarStatus(oferta: Oferta) {
        viewModelScope.launch {
            repository.atualizar(oferta.copy(status = StatusOferta.proximo(oferta.status)))
            _eventos.send("Status atualizado")
        }
    }

    fun excluir(oferta: Oferta) {
        viewModelScope.launch {
            repository.excluir(oferta)
            _eventos.send("Oferta excluída")
        }
    }
}
