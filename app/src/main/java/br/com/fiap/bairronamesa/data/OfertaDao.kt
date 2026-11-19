package br.com.fiap.bairronamesa.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OfertaDao {
    @Query("SELECT * FROM ofertas ORDER BY criadaEm DESC")
    fun listarTodas(): Flow<List<Oferta>>

    @Query("SELECT * FROM ofertas WHERE id = :id")
    fun buscarPorId(id: Long): Flow<Oferta?>

    @Query("SELECT * FROM ofertas WHERE status = 'RESERVADO' ORDER BY feira, produto")
    fun listarReservadas(): Flow<List<Oferta>>

    @Insert
    suspend fun inserir(oferta: Oferta)

    @Update
    suspend fun atualizar(oferta: Oferta)

    @Delete
    suspend fun excluir(oferta: Oferta)
}
