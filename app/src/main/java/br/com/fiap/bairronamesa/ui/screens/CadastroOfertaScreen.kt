package br.com.fiap.bairronamesa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.fiap.bairronamesa.data.DadosMock
import br.com.fiap.bairronamesa.ui.components.CampoDropdown
import br.com.fiap.bairronamesa.ui.components.CampoTexto
import br.com.fiap.bairronamesa.ui.components.TopBarVoltar
import br.com.fiap.bairronamesa.ui.components.dinheiro
import br.com.fiap.bairronamesa.ui.theme.Bricolage
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.Rotulo
import br.com.fiap.bairronamesa.ui.theme.TintaSuave
import br.com.fiap.bairronamesa.ui.theme.VerdeBroto
import br.com.fiap.bairronamesa.ui.theme.VerdeHortaForte
import br.com.fiap.bairronamesa.viewmodel.OfertaViewModel

@Composable
fun CadastroOfertaScreen(vm: OfertaViewModel, id: Long?, onVoltar: () -> Unit, onSalvo: () -> Unit) {
    val form by vm.formState.collectAsStateWithLifecycle()

    // Carrega/limpa só na 1ª abertura: girar a tela não apaga o que já foi digitado
    var iniciado by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!iniciado) {
            if (id == null) vm.limparFormulario() else vm.carregarParaEdicao(id)
            iniciado = true
        }
    }

    Scaffold(topBar = { TopBarVoltar(if (id == null) "Nova oferta" else "Editar oferta", onVoltar) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().imePadding()) {
            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CampoTexto("Produto", form.produto, { v -> vm.atualizarForm { it.copy(produto = v, erroProduto = null) } }, form.erroProduto)
                CampoDropdown("Categoria", form.categoria, DadosMock.categorias, { v -> vm.atualizarForm { it.copy(categoria = v, erroCategoria = null) } }, form.erroCategoria)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CampoTexto(
                        "Quantidade (kg)", form.quantidade,
                        { v -> vm.atualizarForm { it.copy(quantidade = v, erroQuantidade = null) } },
                        form.erroQuantidade, Modifier.weight(1f), numerico = true
                    )
                    CampoTexto(
                        "Preço por kg (R$)", form.preco,
                        { v -> vm.atualizarForm { it.copy(preco = v, erroPreco = null) } },
                        form.erroPreco, Modifier.weight(1f), numerico = true
                    )
                }
                CampoDropdown("Feira", form.feira, DadosMock.feiras, { v -> vm.atualizarForm { it.copy(feira = v, erroFeira = null) } }, form.erroFeira)

                form.valorTotal?.let { total ->
                    Row(
                        Modifier.fillMaxWidth().background(VerdeBroto, RoundedCornerShape(14.dp)).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Valor total estimado", style = Corpo, modifier = Modifier.weight(1f))
                        Text(dinheiro(total), fontFamily = Bricolage, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = VerdeHortaForte)
                    }
                }
                Text("A oferta entra como Disponível. Os moradores reservam pelo app.", style = Legenda, color = TintaSuave)
            }
            Button(
                onClick = { if (vm.salvar()) onSalvo() },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp)
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Salvar oferta", style = Rotulo)
            }
        }
    }
}
