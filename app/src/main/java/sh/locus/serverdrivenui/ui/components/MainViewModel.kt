package sh.locus.serverdrivenui.ui.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sh.locus.serverdrivenui.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _listState: MutableState<MainState?> = mutableStateOf(MainState())
    val listState: State<MainState?> = _listState

    init {
        getItemList()
    }

    private fun getItemList() = viewModelScope.launch {
        try {
            val response = repository.getItems()

            if (response is Resource.Success) {
                response.data?.let {
                    _listState.value = MainState(data = it)
                }

            } else if (response is Resource.Error) {
                response.message?.let {
                    _listState.value = MainState(error = it)
                }
            }
        } catch (ex: Exception) {
            _listState.value = MainState(error = ex.message.toString())
        }
    }

    fun reset(){
        _listState.value = null
    }
}