package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import br.com.fiap.bairronamesa.ui.theme.Legenda
import br.com.fiap.bairronamesa.ui.theme.Numero
import br.com.fiap.bairronamesa.ui.theme.Tinta
import br.com.fiap.bairronamesa.ui.theme.TintaSuave
import br.com.fiap.bairronamesa.ui.theme.VerdeHorta

@Composable
fun ResumoCard(rotulo: String, valor: String, modifier: Modifier = Modifier, destaque: Boolean = false) {
    Cartao(modifier.semantics(mergeDescendants = true) { contentDescription = "$rotulo: $valor" }) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(rotulo, style = Legenda, color = TintaSuave)
            Spacer(Modifier.height(4.dp))
            Text(valor, style = Numero, color = if (destaque) VerdeHorta else Tinta)
        }
    }
}
