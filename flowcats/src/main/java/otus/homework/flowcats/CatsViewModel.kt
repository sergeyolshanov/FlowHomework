package otus.homework.flowcats

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _catsStateFlow = MutableStateFlow<Result<Fact>>(Result.Initial)
    val catsStateFlow: StateFlow<Result<Fact>> = _catsStateFlow

    init {
        catsRepository.listenForCatFacts()
            .onEach { fact -> _catsStateFlow.emit(Result.Success(fact)) }
            .catch { exception -> _catsStateFlow.emit(Result.Error(exception.message.orEmpty())) }
            .launchIn(viewModelScope)
    }
}

class CatsViewModelFactory(private val catsRepository: CatsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CatsViewModel(catsRepository) as T
}