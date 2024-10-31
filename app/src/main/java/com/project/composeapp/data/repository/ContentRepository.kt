package com.project.composeapp.data.repository

import com.project.composeapp.data.datasource.ContentDataSourceImpl
import com.project.composeapp.data.model.ContentModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ContentRepository{
    suspend fun getContents(page : Int) : Result<ContentModel.ContentResponse>
}

class ContentRepositoryImpl @Inject constructor(
    private val contentDataSource: ContentDataSourceImpl
) : ContentRepository{
    override suspend fun getContents(page: Int) : Result<ContentModel.ContentResponse>{
        return contentDataSource.getContents(page)
    }
}

suspend fun <T> makeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T
): Result<T> = runCatching {
    withContext(dispatcher) {
        call.invoke()
    }
}