package ru.itis.recipeslayout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.itis.recipeslayout.model.entity.ExtendedIngredientEntity

@Dao
interface ExtendedIngredientDao {
    @Query("SELECT * FROM extended_ingredients")
    fun getExtendedIngredient(): Flow<List<ExtendedIngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExtendedIngredient(extendedIngredient: ExtendedIngredientEntity)
}