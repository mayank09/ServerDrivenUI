package sh.locus.serverdrivenui.model

import android.graphics.Bitmap

data class DataMap(
    var photo: Bitmap? = null,
    var comment: String? = null,
    var options: List<String>,
    var selectedOption: String? = null
)