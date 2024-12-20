package org.example.mytask.data.repository

import dev.gitlive.firebase.firestore.Direction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import org.example.mytask.core.launchWithAwait
import org.example.mytask.data.modules.FirebaseModule
import org.example.mytask.domain.models.Task
import org.example.mytask.domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val firebaseModule: FirebaseModule,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : TaskRepository {

    override fun getTasks(userId: String): Flow<List<Task>> =
        firebaseModule.firestore().collection(TASK_COLLECTION)
            .orderBy(TITLE_FIELD, Direction.DESCENDING).snapshots.conflate()
            .map { snapshot ->
                snapshot.documents
                    .filter { (it.get("userId") as String) == userId }
                    .map { it.data() }
            }

    override fun getTaskById(id: String): Flow<Task> =
        firebaseModule.firestore().collection(TASK_COLLECTION)
            .document(id).snapshots.map { it.data() }

    override suspend fun addTask(task: Task) {
        launchWithAwait(scope) {
            val taskId = firebaseModule.firestore().collection(TASK_COLLECTION).add(task).id
            firebaseModule.firestore().collection(TASK_COLLECTION).document(taskId)
                .update(Pair("id", taskId))
        }
    }

    override suspend fun updateTask(task: Task) {
        launchWithAwait(scope) {
            firebaseModule.firestore().collection(TASK_COLLECTION).document(task.id).set(task)
        }
    }

    override suspend fun deleteTask(task: Task) {
        launchWithAwait(scope) {
            firebaseModule.firestore().collection(TASK_COLLECTION).document(task.id).delete()
        }
    }

    companion object {
        private const val TASK_COLLECTION = "tasks"
        private const val TITLE_FIELD = "title"
    }

}