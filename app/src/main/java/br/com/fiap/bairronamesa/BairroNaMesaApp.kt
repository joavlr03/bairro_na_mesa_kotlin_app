package br.com.fiap.bairronamesa

import android.app.Application
import br.com.fiap.bairronamesa.data.AppDatabase
import br.com.fiap.bairronamesa.data.OfertaRepository

class BairroNaMesaApp : Application() {
    val repository by lazy { OfertaRepository(AppDatabase.get(this).ofertaDao()) }
}
