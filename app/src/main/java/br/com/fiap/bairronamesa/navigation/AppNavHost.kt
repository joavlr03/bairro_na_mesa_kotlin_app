package br.com.fiap.bairronamesa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.bairronamesa.BairroNaMesaApp
import br.com.fiap.bairronamesa.ui.screens.CadastroOfertaScreen
import br.com.fiap.bairronamesa.ui.screens.InicioScreen
import br.com.fiap.bairronamesa.ui.screens.ListaOfertasScreen
import br.com.fiap.bairronamesa.ui.screens.SeparacaoScreen
import br.com.fiap.bairronamesa.viewmodel.OfertaViewModel
import br.com.fiap.bairronamesa.viewmodel.OfertaViewModelFactory

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val app = LocalContext.current.applicationContext as BairroNaMesaApp
    // Um ViewModel para a Activity: o Snackbar "Oferta salva" sai do cadastro e aparece na lista
    val vm: OfertaViewModel = viewModel(factory = OfertaViewModelFactory(app.repository))
    val voltar: () -> Unit = { nav.popBackStack() }

    NavHost(navController = nav, startDestination = Rotas.INICIO) {
        composable(Rotas.INICIO) {
            InicioScreen(
                vm = vm,
                onNovaOferta = { nav.navigate(Rotas.cadastro()) },
                onMinhasOfertas = { nav.navigate(Rotas.OFERTAS) },
                onSeparacao = { nav.navigate(Rotas.SEPARACAO) }
            )
        }
        composable(Rotas.OFERTAS) {
            ListaOfertasScreen(
                vm = vm,
                onVoltar = voltar,
                onNova = { nav.navigate(Rotas.cadastro()) },
                onEditar = { nav.navigate(Rotas.cadastro(it)) }
            )
        }
        composable(
            Rotas.CADASTRO,
            arguments = listOf(navArgument("id") { type = NavType.LongType; defaultValue = -1L })
        ) { entrada ->
            val id = entrada.arguments?.getLong("id")?.takeIf { it > 0 }
            CadastroOfertaScreen(
                vm = vm,
                id = id,
                onVoltar = voltar,
                onSalvo = {
                    // Depois de salvar vai sempre para a lista (mesmo se o cadastro foi aberto pela Início)
                    if (!nav.popBackStack(Rotas.OFERTAS, inclusive = false)) {
                        nav.navigate(Rotas.OFERTAS) { popUpTo(Rotas.INICIO) }
                    }
                }
            )
        }
        composable(Rotas.SEPARACAO) {
            SeparacaoScreen(vm = vm, onVoltar = voltar)
        }
    }
}
