package com.project.composeapp.ui

import com.project.composeapp.data.model.ContentModel

class MainContract {
    sealed class Event {
        data object LoadMore : Event()
        data class Search(val text : String) : Event()
        data class ValueChange(val text : String) : Event()
    }

    data class State(
        val meta: ContentModel.Meta,
        val data: List<ContentModel.Content>,
        val isLoading: Boolean,
        val textFieldValue : String
    ){
        companion object{
            fun initial() = State(
                meta = ContentModel.Meta.initial(),
                data = emptyList(),
                isLoading = true,
                textFieldValue = ""
            )
        }
    }

}