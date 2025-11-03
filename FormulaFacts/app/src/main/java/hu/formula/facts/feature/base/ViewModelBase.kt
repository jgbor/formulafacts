package hu.formula.facts.feature.base

import androidx.lifecycle.ViewModel

abstract class ViewModelBase : ViewModel() {
    fun onEvent(event: BaseEvent) {
        when (event) {
            is BaseEvent.Refresh -> loadData()
        }
    }

    protected abstract fun loadData()
}
