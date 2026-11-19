package br.com.fiap.bairronamesa

import br.com.fiap.bairronamesa.data.StatusOferta
import br.com.fiap.bairronamesa.ui.components.dinheiro
import br.com.fiap.bairronamesa.ui.components.kg
import br.com.fiap.bairronamesa.viewmodel.FormOfertaState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegrasTest {
    private val valido = FormOfertaState(produto = "Mandioca", categoria = "Legumes", quantidade = "25", preco = "4,00", feira = "Praça São Mateus · Sábado")

    @Test fun formularioValidoAceitaVirgula() {
        val f = valido.validado()
        assertTrue(f.valido)
        assertEquals(100.0, f.valorTotal!!, 0.001)
    }

    @Test fun formularioInvalidoMostraMensagens() {
        val f = valido.copy(produto = " ", categoria = "", quantidade = "0", preco = "501", feira = "").validado()
        assertFalse(f.valido)
        assertEquals("Informe o nome do produto", f.erroProduto)
        assertEquals("Escolha uma categoria", f.erroCategoria)
        assertEquals("Informe uma quantidade maior que zero", f.erroQuantidade)
        assertEquals("Informe um preço válido", f.erroPreco)
        assertEquals("Escolha a feira", f.erroFeira)
        assertNull(f.valorTotal) // sem prévia quando a conta não é válida
    }

    @Test fun limitesDeQuantidade() {
        assertTrue(valido.copy(quantidade = "1000").validado().valido)
        assertFalse(valido.copy(quantidade = "1000,5").validado().valido)
        assertFalse(valido.copy(quantidade = "abc").validado().valido)
    }

    @Test fun statusAvanca() {
        assertEquals(StatusOferta.RESERVADO, StatusOferta.proximo(StatusOferta.DISPONIVEL))
        assertEquals(StatusOferta.VENDIDO, StatusOferta.proximo(StatusOferta.RESERVADO))
        assertEquals(StatusOferta.VENDIDO, StatusOferta.proximo(StatusOferta.VENDIDO))
    }

    @Test fun formatos() {
        assertEquals("R$ 96,00", dinheiro(96.0))
        assertEquals("R$ 1.250,50", dinheiro(1250.5))
        assertEquals("12 kg", kg(12.0))
        assertEquals("12,5 kg", kg(12.5))
    }
}
