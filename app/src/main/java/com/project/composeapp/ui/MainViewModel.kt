package com.project.composeapp.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.composeapp.data.model.ContentModel
import com.project.composeapp.data.repository.ContentRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val contentRepository: ContentRepositoryImpl
) : ViewModel() {

    private var originItems = listOf<ContentModel.Content>()

    private val _uiState: MutableState<MainContract.State> =
        mutableStateOf(MainContract.State.initial())
    val uiState get() = _uiState

    private val _event: MutableSharedFlow<MainContract.Event> = MutableSharedFlow()

    init {
        handelEvents()
        load(0)
    }

    private fun handelEvents() {
        viewModelScope.launch {
            _event.collect {
                when (it) {
                    is MainContract.Event.LoadMore -> loadMore()
                    is MainContract.Event.Search -> onSearch(it.text)
                    is MainContract.Event.ValueChange -> onValueChange(it.text)
                }
            }
        }
    }

    fun setEvent(event: MainContract.Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    private fun onValueChange(text: String) {
        _uiState.value = uiState.value.copy(textFieldValue = text)
    }

    private fun loadMore() {
        uiState.value.let {
            val size = it.data.size
            if (!it.meta.isEnd && it.meta.totalCount > size && it.textFieldValue.isEmpty()) {
                load(size / 10)
            }
        }
    }

    private fun onSearch(text: String) {
        _uiState.value = uiState.value.copy(
            data = originItems.filter { it.title.contains(text) }
        )
    }

    private fun load(page: Int) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true)
            delay(3000) // Test
            contentRepository.getContents(page)
                .onSuccess {
                    Log.i("hsik", "load onSuccess = $it")
                    originItems = uiState.value.data + it.contents
                    _uiState.value = uiState.value.copy(
                        meta = it.meta,
                        data = originItems,
                        isLoading = false
                    )
                }
                .onFailure {
                    Log.i("hsik", "load onFailure = $it")
                    _uiState.value = uiState.value.copy(isLoading = false)
                }
        }
    }
}
