package br.com.fiap.bairronamesa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.bairronamesa.data.OfertaRepository

class OfertaViewModelFactory(private val repository: OfertaRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = OfertaViewModel(repository) as T
}
