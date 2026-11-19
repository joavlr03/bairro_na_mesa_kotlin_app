package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.com.fiap.bairronamesa.ui.theme.Creme100
import br.com.fiap.bairronamesa.ui.theme.Linha
import br.com.fiap.bairronamesa.ui.theme.TituloTopBar
import androidx.compose.foundation.background

@Composable
fun TopBarVoltar(titulo: String, onVoltar: () -> Unit) {
    Column(Modifier.background(Creme100).windowInsetsPadding(WindowInsets.statusBars)) {
        Row(
            Modifier.height(64.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onVoltar, modifier = Modifier.size(48.dp)) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Voltar")
            }
            Text(titulo, style = TituloTopBar, modifier = Modifier.semantics { heading() })
        }
        HorizontalDivider(thickness = 1.dp, color = Linha)
    }
}
