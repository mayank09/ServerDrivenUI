package sh.locus.serverdrivenui.ui.components

import sh.locus.serverdrivenui.model.ResponseItem

data class MainState (
    val isLoading:Boolean=false,
    var data:List<ResponseItem> = mutableListOf(),
    val error:String=""
)