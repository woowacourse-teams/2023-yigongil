package com.created.team201.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.created.domain.model.Study
import com.created.domain.model.Todo
import com.created.domain.model.UserInfo
import com.created.team201.presentation.home.model.HomeUiModel
import com.created.team201.presentation.home.model.TodoUiModel

class HomeViewModel : ViewModel() {
    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> get() = _userName

    private val _userStudies: MutableLiveData<List<HomeUiModel>> = MutableLiveData()
    val userStudies: LiveData<List<HomeUiModel>> get() = _userStudies

    fun getUserStudyInfo() {
        _userName.value = DUMMY.userName
        _userStudies.value = DUMMY.studies.map { it.toUiModel() }
    }

    fun temp() {
        // 스터디 네임, 투두 아이디, 투두 여부 3개 줘야함

        _userStudies.value = userStudies.value?.map {
            it.copy(studyName = "1234")
        }
    }

    fun update(id: Int, isDone: Boolean) {



    }

    private fun Study.toUiModel(): HomeUiModel =
        HomeUiModel(
            studyName = this.studyName,
            progressRate = this.progressRate,
            leftDays = this.leftDays,
            nextDate = this.nextDate,
            necessaryTodo = this.necessaryTodo.toUiModel(),
            optionalTodos = this.optionalTodo.map { it.toUiModel() },
        )

    private fun Todo.toUiModel(): TodoUiModel = TodoUiModel(
        todoId = this.todoId,
        content = this.content,
        isDone = this.isDone,
    )

    companion object {
        private val DUMMY = UserInfo(
            "산군",
            listOf(
                Study(
                    1,
                    "빨리 만들자",
                    20,
                    5,
                    "2023.01.02",
                    Todo(2, "잔디구현끝내기", true),
                    listOf(
                        Todo(3, "홈뷰 끝내기", false),
                        Todo(3, "홈뷰 끝내기", false),
                        Todo(3, "홈뷰 끝내기", false),
                        Todo(3, "홈뷰 끝내기", false),
                    ),

                ),
                Study(
                    2,
                    "빨리 만들자고",
                    87,
                    9,
                    "2023.07.02",
                    Todo(2, "선택 투두 끝내기", true),
                    listOf(Todo(3, "홈뷰 끝내기2", true)),

                ),
            ),
        )
    }
}
