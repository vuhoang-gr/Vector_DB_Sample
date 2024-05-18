package com.example.searchapp.features

import EmptyCard
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.searchapp.R
import com.example.searchapp.core.common.getBitmapFromAsset
import com.example.searchapp.core.network.dto.Match
import com.example.searchapp.features.util.TabInfo
import com.example.searchapp.features.util.TabInfoConvertible
import com.example.searchapp.features.util.TabLayout
import com.example.searchapp.ui.theme.shape
import com.example.searchapp.ui.theme.spacing


@Composable
fun SearchRoute(
    viewModel: SearchScreenViewModel = viewModel()
) {
    val currentText by viewModel.currentText.collectAsStateWithLifecycle()
    SearchScreen(
        isLoading = viewModel.isLoading,
        currentImage = viewModel.currentImage,
        similarImages = viewModel.similarImages,
        currentText = currentText,
        similarText = viewModel.similarText,
        onChangeText = viewModel::onChangeText,
        onSearchText = viewModel::onSearchText,
        onChooseImage = viewModel::onChooseImage
    )
}

@OptIn(ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SearchScreen(
    isLoading: Boolean,
    currentText: String,
    similarText: String,
    onChangeText: (String) -> Unit,
    onSearchText: () -> Unit,
    currentImage: Bitmap?,
    similarImages: List<Match>,
    onChooseImage: (Bitmap) -> Unit
) {
    val context = LocalContext.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val source = ImageDecoder.createSource(
                    context.contentResolver,
                    it
                )
                val bitmap = ImageDecoder.decodeBitmap(source).copy(
                    Bitmap.Config.ARGB_8888,
                    true
                )
                onChooseImage(bitmap)
            }
        }
    )

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text(text = "Vector database")})
            }
        }
    ) { paddingValues ->
        val tabs: List<Pair<SearchScreenTab, @Composable (() -> Unit)>> = listOf(
            SearchScreenTab.IMAGE to {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                ) {
                    if (currentImage != null) {
                        AsyncImage(
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    Color.Gray,
                                    MaterialTheme.shape.roundedCornerMedium
                                )
                                .clip(MaterialTheme.shape.roundedCornerMedium)
                                .height(300.dp)
                                .fillMaxWidth()
                                .clickable { launchPhotoPicker() },
                            model = currentImage,
                            contentDescription = "Current Image",
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        EmptyCard(
                            onClick = { launchPhotoPicker() },
                            message = "Select an image"
                        )
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Text("Results: ")

                    if (isLoading) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Loading...")
                        }
                    } else {
                        if (similarImages.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Empty")
                            }
                        } else {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                similarImages.forEach {
                                    Column(
                                        modifier = Modifier
                                            .padding(vertical = MaterialTheme.spacing.small)
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .width(150.dp)
                                                .height(150.dp)
                                                .border(
                                                    1.dp,
                                                    Color.Gray,
                                                    MaterialTheme.shape.roundedCornerMedium
                                                )
                                                .clip(MaterialTheme.shape.roundedCornerMedium),
                                            model = context.getBitmapFromAsset(it.id),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                                        Text(text = "Score: ${it.score}")
                                    }
                                }
                            }
                        }
                    }
                }
            },
            SearchScreenTab.TEXT to {
                Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Command") },
                            value = currentText,
                            maxLines = 1,
                            onValueChange = onChangeText
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.large),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onSearchText
                        ) {
                            Text("Search")
                        }
                    }
                    Text(text = similarText)
                }
            }
        )
        val pagerState: PagerState = rememberPagerState(
            initialPage = tabs.indexOfFirst { it.first == SearchScreenTab.IMAGE }.coerceAtLeast(0),
            initialPageOffsetFraction = 0f
        ) {
            tabs.size
        }

        TabLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            pageState = pagerState,
            contents = tabs
        )
    }

}

enum class SearchScreenTab(
    @StringRes val tabName: Int,
    @DrawableRes val icon: Int?
) : TabInfoConvertible {
    IMAGE(R.string.image_title, null),
    TEXT(R.string.text_title, null);

    override fun toTabInfo(): TabInfo {
        return TabInfo(tabName, icon)
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    Surface {
        SearchScreen(
            isLoading = true,
            currentImage = null,
            similarImages = listOf(),
            currentText = "",
            similarText = "",
            onSearchText = {},
            onChangeText = {},
            onChooseImage = {}
        )
    }
}