package com.project.composeapp.data.datasource

import com.project.composeapp.data.model.ContentModel
import com.project.composeapp.data.repository.makeApiCall
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface ContentDataSource {
    suspend fun getContents(page : Int) : Result<ContentModel.ContentResponse>
}

class ContentDataSourceImpl @Inject constructor(): ContentDataSource{
    override suspend fun getContents(page: Int): Result<ContentModel.ContentResponse> = makeApiCall(Dispatchers.IO){
        ContentModel.ContentResponse(
            meta = ContentModel.Meta.initial(),
            contents = ContentModel.Content.tempItems(page)
        )
    }
}