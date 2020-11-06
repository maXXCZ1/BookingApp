package opkp.solutions.bookingapp

abstract class DataState

class LoadingState : DataState()

data class ErrorState(val error: Exception) : DataState()

class CompletedState : DataState()