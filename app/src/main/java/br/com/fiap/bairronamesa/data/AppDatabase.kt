package br.com.fiap.bairronamesa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Oferta::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ofertaDao(): OfertaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "bairro_na_mesa.db")
                .addCallback(SeedCallback)
                .build()
                .also { INSTANCE = it }
        }
    }

    // Roda só quando o arquivo do banco é criado: não sobrescreve os dados do usuário.
    // SQL direto na mesma transação de criação: o seed fica pronto antes da 1ª consulta (sem corrida com coroutine).
    private object SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            val agora = System.currentTimeMillis()
            val seed = listOf(
                arrayOf("Alface crespa", "Verduras", 12.0, 8.0, DadosMock.feiras[0], StatusOferta.DISPONIVEL, agora),
                arrayOf("Tomate", "Legumes", 30.0, 6.5, DadosMock.feiras[0], StatusOferta.RESERVADO, agora - 1),
                arrayOf("Banana prata", "Frutas", 20.0, 5.0, DadosMock.feiras[1], StatusOferta.VENDIDO, agora - 2),
            )
            seed.forEach {
                db.execSQL(
                    "INSERT INTO ofertas (produto, categoria, quantidadeKg, precoKg, feira, status, criadaEm) VALUES (?,?,?,?,?,?,?)",
                    it
                )
            }
        }
    }
}
