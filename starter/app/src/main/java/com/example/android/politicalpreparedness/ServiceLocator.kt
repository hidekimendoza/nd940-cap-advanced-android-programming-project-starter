package com.example.android.politicalpreparedness

object ServiceLocator {
//
//    private val lock = Any()
//    private var database: ElectionDatabase? = null
//    @Volatile
//    var tasksRepository: ElectionRepository? = null
//        @VisibleForTesting set
//
//    fun provideElectionRepository(context: Context): ElectionRepository {
//        synchronized(this) {
//            return ElectionRepository ?: createElectionRepository(context)
//        }
//    }
//
//    private fun createElectionRepository(context: Context): ElectionRepository {
//        val newRepo = ElectionRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
//        tasksRepository = newRepo
//        return newRepo
//    }
//
//    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
//        val database = database ?: createDataBase(context)
//        return TasksLocalDataSource(database.taskDao())
//    }
//
//    private fun createDataBase(context: Context): ElectionDatabase {
//        val result = Room.databaseBuilder(
//            context.applicationContext,
//            ElectionDatabase::class.java, "Tasks.db"
//        ).build()
//        database = result
//        return result
//    }
//
//    @VisibleForTesting
//    fun resetRepository() {
//        synchronized(lock) {
//            runBlocking {
//                TasksRemoteDataSource.deleteAllTasks()
//            }
//            // Clear all data to avoid test pollution.
//            database?.apply {
//                clearAllTables()
//                close()
//            }
//            database = null
//            tasksRepository = null
//        }
//    }

}