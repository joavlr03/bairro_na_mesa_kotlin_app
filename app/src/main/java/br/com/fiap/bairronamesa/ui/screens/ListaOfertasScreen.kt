package br.com.fiap.bairronamesa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.fiap.bairronamesa.data.Oferta
import br.com.fiap.bairronamesa.ui.components.EstadoVazio
import br.com.fiap.bairronamesa.ui.components.FiltroChips
import br.com.fiap.bairronamesa.ui.components.OfertaCard
import br.com.fiap.bairronamesa.ui.components.TopBarVoltar
import br.com.fiap.bairronamesa.ui.components.rotuloStatus
import br.com.fiap.bairronamesa.ui.theme.Bricolage
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Creme200
import br.com.fiap.bairronamesa.ui.theme.Erro
import br.com.fiap.bairronamesa.ui.theme.ErroFundo
import br.com.fiap.bairronamesa.ui.theme.Milho
import br.com.fiap.bairronamesa.ui.theme.Rotulo
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.VerdeHortaForte
import br.com.fiap.bairronamesa.viewmodel.OfertaViewModel

@Composable
fun ListaOfertasScreen(vm: OfertaViewModel, onVoltar: () -> Unit, onNova: () -> Unit, onEditar: (Long) -> Unit) {
    val ofertas by vm.ofertasFiltradas.collectAsStateWithLifecycle()
    val filtro by vm.filtroStatus.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var paraExcluir by remember { mutableStateOf<Oferta?>(null) }

    LaunchedEffect(Unit) { vm.eventos.collect { snackbar.showSnackbar(it) } }

    // A oferta nova entra no topo; sem isto a lista mantém a rolagem e ela fica escondida acima
    val rolagem = rememberLazyListState()
    LaunchedEffect(ofertas?.firstOrNull()?.id) { rolagem.scrollToItem(0) }

    Scaffold(
        topBar = { TopBarVoltar("Minhas ofertas", onVoltar) },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNova,
                containerColor = Milho,
                contentColor = Tinta,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(64.dp)
            ) { Icon(Icons.Rounded.Add, contentDescription = "Nova oferta", modifier = Modifier.size(32.dp)) }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            FiltroChips(filtro) { vm.definirFiltro(it) }
            val lista = ofertas
            when {
                lista == null -> Unit // carregando
                lista.isEmpty() -> EstadoVazio(
                    titulo = if (filtro == null) "Nenhuma oferta por aqui ainda." else "Nenhuma oferta com o status ${rotuloStatus(filtro!!)}.",
                    texto = if (filtro == null) "Toque em + para publicar sua colheita."
                    else "Toque em Todas para ver a lista inteira ou em + para publicar sua colheita.",
                    modifier = Modifier.fillMaxSize()
                )
                else -> LazyColumn(
                    state = rolagem,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(lista, key = { it.id }) { oferta ->
                        OfertaCard(
                            oferta = oferta,
                            onAvancar = { vm.avancarStatus(oferta) },
                            onEditar = { onEditar(oferta.id) },
                            onExcluir = { paraExcluir = oferta }
                        )
                    }
                }
            }
        }
    }

    paraExcluir?.let { oferta ->
        DialogoExcluir(
            produto = oferta.produto,
            onCancelar = { paraExcluir = null },
            onConfirmar = { vm.excluir(oferta); paraExcluir = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoExcluir(produto: String, onCancelar: () -> Unit, onConfirmar: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onCancelar) {
        Surface(shape = RoundedCornerShape(24.dp), color = Creme200, contentColor = Tinta) {
            Column(Modifier.padding(24.dp)) {
                Box(Modifier.size(48.dp).background(ErroFundo, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = Erro)
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Excluir a oferta de $produto?",
                    fontFamily = Bricolage, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp
                )
                Spacer(Modifier.height(12.dp))
                Text("A oferta some da lista e da feira. Não dá para desfazer.", style = Corpo)
                Spacer(Modifier.height(24.dp))
                Row(Modifier.align(Alignment.End), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onCancelar, modifier = Modifier.height(48.dp)) {
                        Text("Cancelar", style = Rotulo, color = VerdeHortaForte)
                    }
                    Button(
                        onClick = onConfirmar,
                        modifier = Modifier.height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Erro, contentColor = Creme200)
                    ) { Text("Excluir", style = Rotulo) }
                }
            }
        }
    }
}
