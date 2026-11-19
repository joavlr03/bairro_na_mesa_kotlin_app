package br.com.fiap.bairronamesa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.bairronamesa.ui.theme.Bricolage
import br.com.fiap.bairronamesa.ui.theme.Corpo
import br.com.fiap.bairronamesa.ui.theme.Creme300
import br.com.fiap.bairronamesa.ui.theme.TintaSuave

@Composable
fun EstadoVazio(titulo: String, texto: String, modifier: Modifier = Modifier) {
    Column(
        modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.size(96.dp).background(Creme300, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.ShoppingBasket, contentDescription = null, tint = TintaSuave, modifier = Modifier.size(48.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(titulo, fontFamily = Bricolage, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(texto, style = Corpo, color = TintaSuave, textAlign = TextAlign.Center)
    }
}
