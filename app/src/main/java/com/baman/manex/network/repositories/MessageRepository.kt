package com.baman.manex.network.repositories

import androidx.lifecycle.LiveData
import com.baman.manex.AppExecutors
import com.baman.manex.dao.MessageDao
import com.baman.manex.dao.OperatorConfigDao
import com.baman.manex.data.dto.MessageDto
import com.baman.manex.data.model.OperatorConfig
import com.baman.manex.network.ApiResponse
import com.baman.manex.network.Resource
import com.baman.manex.network.service.MessageService
import javax.inject.Inject


class MessageRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val messageService: MessageService,
    private val operatorConfigDao: OperatorConfigDao,
    private val messageDao: MessageDao
) {

    fun loadOperatorConfigData(): LiveData<Resource<OperatorConfig>> =
        object : NetworkBoundResource<OperatorConfig, List<Map<String, String>>>(appExecutors) {

            override fun saveCallResult(item: List<Map<String, String>>) {
                val c = convertToMap(item)
                operatorConfigDao.insert(c)
            }

            override fun shouldFetch(data: OperatorConfig?): Boolean = true

            override fun loadFromDb(): LiveData<OperatorConfig> {
                return operatorConfigDao.findAll()
            }

            override fun createCall(): LiveData<ApiResponse<List<Map<String, String>>>> {
                return messageService.operatorConfig()
            }

            private fun convertToMap(items: List<Map<String, String>>): OperatorConfig {
                val result = mutableMapOf<String, String>()
                for (item in items) {
                    for (entry in item.entries) {
                        result[entry.key] = entry.value
                    }
                }
                return OperatorConfig(messages = result)
            }
        }.asLiveData()


    fun getMessages(): LiveData<Resource<List<MessageDto>>> =
        object : NetworkBoundResource<List<MessageDto>, List<MessageDto>>(appExecutors) {
            override fun saveCallResult(item: List<MessageDto>) {
                messageDao.insertMessage(item)
            }

            override fun shouldFetch(data: List<MessageDto>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<MessageDto>> {
                return messageDao.findMessages()
            }

            override fun createCall(): LiveData<ApiResponse<List<MessageDto>>> {
                return messageService.getMessages()
            }

        }.asLiveData()


}

