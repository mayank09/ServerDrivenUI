package sh.locus.serverdrivenui.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import sh.locus.serverdrivenui.model.DataType
import sh.locus.serverdrivenui.model.ResponseItem


@Composable
fun ItemCard(responseItem: ResponseItem) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 2.dp,
        backgroundColor = Color.LightGray,) {
        Column (Modifier.padding(8.dp)){
            Text(text = responseItem.title)
            when (responseItem.type) {
                DataType.PHOTO.name ->
                    ImageCard(responseItem = responseItem)
                DataType.SINGLE_CHOICE.name ->
                    SingleChoiceCard(responseItem = responseItem)
                DataType.COMMENT.name ->
                    CommentCard(responseItem = responseItem)
            }

        }
    }
}

@Composable
fun ImageCard(responseItem: ResponseItem) {

    val context = LocalContext.current
    val bitmap = remember { mutableStateOf(responseItem.dataMap.photo) }
    val showDialog = remember { mutableStateOf(false) }


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            if (it != null) {
                bitmap.value = it
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            launcher.launch()
            Log.d("PhotoCard", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("PhotoCard", "PERMISSION DENIED")
        }
    }

    Card(
        border = BorderStroke(1.dp,Color.Black),
        backgroundColor = Color.White,
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
            .clickable {

                if (bitmap.value != null) {
                    showDialog.value = true
                } else {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) -> {
                            launcher.launch()
                            // Some works that require permission
                            Log.d("ExampleScreen", "Code requires permission")
                        }
                        else -> {
                            // Asking for permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }

            }
    ) {
        Box(contentAlignment = Alignment.TopEnd){
            bitmap.value?.let {
                Image(
                    bitmap = bitmap.value!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                responseItem.dataMap.photo = it

                Icon(
                    tint = Color.Gray,
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        bitmap.value = null
                        responseItem.dataMap.photo = null
                    }

                )
            } ?: Text(text = "click a picture", Modifier.padding(end = 16.dp) )
        }

    }
    if(showDialog.value){
        OpenPhotoDialog(isShow = showDialog.value,
            title =responseItem.title ,
            bitmap = bitmap.value!!
        ) {
            showDialog.value = false
        }
    }


}

@Composable
fun SingleChoiceCard(responseItem: ResponseItem) {
    val radioOptions = responseItem.dataMap.options
    val selectedValue = remember { mutableStateOf(responseItem.dataMap.selectedOption) }

    val isSelectedItem: (String) -> Boolean = {
        selectedValue.value == it
    }
    val onChangeState: (String) -> Unit = {
        selectedValue.value = it
        responseItem.dataMap.selectedOption = it
    }

    Column(Modifier.padding(8.dp)) {

        radioOptions.forEach { textToEnableState ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(textToEnableState),
                        onClick = { onChangeState(textToEnableState) }
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelectedItem(textToEnableState),
                    onClick = null,
                )
                Text(text = textToEnableState)
            }
        }
    }
}

@Composable
fun CommentCard(responseItem: ResponseItem) {
    Column() {
        val mCheckedState = remember { mutableStateOf(false) }
        val textStata = remember { mutableStateOf(responseItem.dataMap.comment) }

        Switch(checked = mCheckedState.value, onCheckedChange = { mCheckedState.value = it })

        if (mCheckedState.value) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                    .background(Color.White, RoundedCornerShape(5.dp)),
                shape = RoundedCornerShape(5.dp),
                label = { Text(text = "Type Comment") },
                value = textStata.value?:"",
                onValueChange = {
                    textStata.value = it
                    responseItem.dataMap.comment = it
                },
                keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                maxLines = 3,
                textStyle = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun OpenPhotoDialog(isShow: Boolean, title: String, bitmap: Bitmap, onDismiss: () -> Unit){
    val openDialog = remember { mutableStateOf(isShow)}

    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.height(600.dp),
            onDismissRequest = {
                onDismiss
            },
            title = {
                Text(text =title)
            },
            text = {
                Column() {
                    Image(bitmap = bitmap.asImageBitmap() ,
                        contentDescription = null,
                    modifier = Modifier.fillMaxSize())
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismiss
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        )
    }
}