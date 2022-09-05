package sh.locus.serverdrivenui.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.painter.BitmapPainter

data class ResponseItem(
    val dataMap: DataMap,
    val id: String,
    val title: String,
    val type: String
)