package sh.locus.serverdrivenui.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sh.locus.serverdrivenui.R

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val result = viewModel.listState.value
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(id = R.string.app_name)) },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 10.dp,
            actions = {
                Text(text = stringResource(id = R.string.submit),
                    color = Color.White,
                    modifier = Modifier.padding(end = 8.dp).clickable {
                        Log.d("FinalResult Start", "*******************************")
                        Log.d("UserInput", result.toString())
                        Log.d("FinalResult  End", "*******************************")
                        //reset result
                        viewModel.reset()
                        Toast.makeText(context, "User Input Logged Successfully", Toast.LENGTH_SHORT).show()
                    })
            }
        )
    }) { contentPadding ->
        // Screen content
        Box(modifier = Modifier.padding(contentPadding)) {
            if (result?.isLoading == true) {
                Log.d("TAG", "MainContent: in the loading")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            if (result?.error?.isNotBlank() == true) {
                Log.d("TAG", "MainContent: ${result.error}")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = result.error
                    )
                }
            }

            if (result?.data?.isNotEmpty() == true) {
                LazyColumn(Modifier.padding(top = 8.dp)) {
                    items(result.data) {
                        ItemCard(responseItem = it)
                    }
                }
            }
        }
    }
}



