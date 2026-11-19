package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Creme200
import br.com.fiap.bairronamesa.ui.theme.Erro
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.LinhaCampo
import br.com.fiap.bairronamesa.ui.theme.Rotulo
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.TintaSuave
import br.com.fiap.bairronamesa.ui.theme.VerdeHorta

private val CantoCampo = RoundedCornerShape(8.dp)

/** Rótulo acima + conteúdo + mensagem de erro (ícone + texto) abaixo. */
@Composable
private fun CampoBase(rotulo: String, erro: String?, modifier: Modifier, campo: @Composable () -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(rotulo, style = Rotulo)
        campo()
        if (erro != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Ícone alinhado à 1ª linha quando a mensagem quebra
                Icon(Icons.Rounded.ErrorOutline, contentDescription = null, tint = Erro, modifier = Modifier.padding(top = 2.dp).size(16.dp))
                Text(erro, style = Legenda, color = Erro)
            }
        }
    }
}

/** Caixa de 52 dp: borda 1 dp LinhaCampo, 2 dp VerdeHorta com foco, 2 dp Erro com erro. */
private fun Modifier.moldura(focado: Boolean, comErro: Boolean) = this
    .fillMaxWidth()
    .height(52.dp)
    .background(Creme200, CantoCampo)
    .border(
        width = if (focado || comErro) 2.dp else 1.dp,
        color = when { comErro -> Erro; focado -> VerdeHorta; else -> LinhaCampo },
        shape = CantoCampo
    )
    .padding(horizontal = 14.dp)

@Composable
fun CampoTexto(
    rotulo: String,
    valor: String,
    onValor: (String) -> Unit,
    erro: String?,
    modifier: Modifier = Modifier,
    numerico: Boolean = false,
) {
    val interacao = remember { MutableInteractionSource() }
    val focado by interacao.collectIsFocusedAsState()
    CampoBase(rotulo, erro, modifier) {
        BasicTextField(
            value = valor,
            onValueChange = onValor,
            singleLine = true,
            textStyle = Corpo.copy(color = Tinta),
            cursorBrush = SolidColor(VerdeHorta),
            interactionSource = interacao,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (numerico) KeyboardType.Decimal else KeyboardType.Text,
                capitalization = if (numerico) KeyboardCapitalization.None else KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.semantics {
                contentDescription = rotulo
                if (erro != null) error(erro)
            },
            decorationBox = { inner ->
                Box(Modifier.moldura(focado, erro != null), contentAlignment = Alignment.CenterStart) { inner() }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDropdown(
    rotulo: String,
    valor: String,
    opcoes: List<String>,
    onValor: (String) -> Unit,
    erro: String?,
    modifier: Modifier = Modifier,
) {
    var aberto by remember { mutableStateOf(false) }
    CampoBase(rotulo, erro, modifier) {
        ExposedDropdownMenuBox(expanded = aberto, onExpandedChange = { aberto = it }) {
            Row(
                Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .moldura(aberto, erro != null)
                    .semantics {
                        contentDescription = "$rotulo: ${valor.ifEmpty { "nenhuma escolhida" }}"
                        if (erro != null) error(erro)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Texto(valor)
                Icon(Icons.Rounded.ExpandMore, contentDescription = null, tint = Tinta)
            }
            ExposedDropdownMenu(expanded = aberto, onDismissRequest = { aberto = false }, containerColor = Creme200) {
                opcoes.forEach { opcao ->
                    DropdownMenuItem(
                        text = { Text(opcao, style = Corpo) },
                        onClick = { onValor(opcao); aberto = false },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.Texto(valor: String) {
    Text(
        valor.ifEmpty { "Escolha" },
        style = Corpo,
        color = if (valor.isEmpty()) TintaSuave else Tinta,
        modifier = Modifier.weight(1f)
    )
}
