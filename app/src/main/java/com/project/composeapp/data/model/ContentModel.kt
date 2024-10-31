package com.project.composeapp.data.model

class ContentModel {
    data class ContentResponse(
        val meta : Meta,
        val contents : List<Content>
    )

    data class Meta(
        val totalCount : Long,
        val pageableCount : Long,
        val isEnd : Boolean
    ){
        companion object{
            fun initial() = Meta(
                totalCount = 200,
                pageableCount = 10,
                isEnd = false
            )
        }
    }

    data class Content(
        val datetime : String,
        val contents : String,
        val title : String,
        val url : String
    ){
        companion object{
            fun tempItems(page : Int) = (page * 10 until  (page + 1) * 10).map {
                Content(
                    datetime = System.currentTimeMillis().toString(),
                    contents = "contents : $it",
                    title = "title : $it",
                    url = ""
                )
            }.toList()
        }
    }
}
