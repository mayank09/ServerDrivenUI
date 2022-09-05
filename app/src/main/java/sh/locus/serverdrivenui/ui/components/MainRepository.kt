package sh.locus.serverdrivenui.ui.components

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import sh.locus.serverdrivenui.model.DataMap
import sh.locus.serverdrivenui.model.ResponseItem
import sh.locus.serverdrivenui.utils.Resource
import sh.locus.serverdrivenui.utils.getResponseFromAsset
import java.lang.Exception
import javax.inject.Inject

const val DATA_SOURCE = "source.json"

@ViewModelScoped
class MainRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    fun getItems(): Resource<List<ResponseItem>>{
         return try{
             Resource.Success(data = context.getResponseFromAsset(DATA_SOURCE).data)
         }catch (ex: Exception){
             ex.printStackTrace()
             Resource.Error(message = ex.message!!)
         }
    }
}