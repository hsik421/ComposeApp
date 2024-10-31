package com.project.composeapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.composeapp.data.model.ContentModel
import com.project.composeapp.ui.theme.ComposeAppTheme

@Composable
fun SearchLayout(
    text: String,
    onSearchEvent: (MainContract.Event) -> Unit,
    onValueChange: (MainContract.Event) -> Unit
) {
    Log.i("hsik", "SearchLayout")
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(modifier = Modifier.weight(1f), value = text, onValueChange = {
            onValueChange.invoke(MainContract.Event.ValueChange(it))
        })
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_search),
            contentDescription = null,
            modifier = Modifier.clickable {
                onSearchEvent.invoke(MainContract.Event.Search(text))
            })
    }
}

@Composable
fun ListLayout(
    contentItems: List<ContentModel.Content>,
    isLoading: Boolean,
    onLoadMoreEvent: (MainContract.Event) -> Unit
) {
    Log.i("hsik", "ListLayout")
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == listState.layoutInfo.totalItemsCount - 1) {
                    onLoadMoreEvent.invoke(MainContract.Event.LoadMore)
                }
            }
    }
    LazyColumn(state = listState) {
        itemsIndexed(contentItems) { index, item ->
            ListItem(item = item)
            if (index < contentItems.lastIndex) HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp
            )
        }
        if (isLoading) {
            item { ProgressItem() }
        }
    }
}

@Composable
fun ProgressItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ListItem(item: ContentModel.Content) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(modifier = Modifier.padding(10.dp), text = item.title)
        Text(modifier = Modifier.padding(10.dp), text = item.contents)
    }
}

@Composable
fun MainView(modifier: Modifier = Modifier) {
    Log.i("hsik", "MainView")
    val viewModel = hiltViewModel<MainViewModel>()
    Column(modifier = modifier) {
        SearchLayout(
            text = viewModel.uiState.value.textFieldValue,
            onSearchEvent = { viewModel.setEvent(it) },
            onValueChange = { viewModel.setEvent(it) })
        ListLayout(
            contentItems = viewModel.uiState.value.data,
            isLoading = viewModel.uiState.value.isLoading
        ) {
            viewModel.setEvent(it)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    ComposeAppTheme {
//        ListLayout()
//        SearchLayout(hiltViewModel())
    }
}