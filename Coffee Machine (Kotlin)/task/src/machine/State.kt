package machine

sealed class State {
    fun next(): Filling {
        val stateFillState = (this as Filling).fillState
        return Filling(FillState.entries[stateFillState.ordinal + 1])
    }

    object Idle : State()
    object Buying : State()
    data class Filling(var fillState: FillState) : State() {
    }

    enum class FillState {
        WATER,
        MILK,
        BEANS,
        CUPS;
    }
}