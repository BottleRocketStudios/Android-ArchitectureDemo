# Project Skills & Rules: BR_Architecture

## Architecture
- **Modules**:
  - `:app`: Main Android application module containing UI logic and framework integrations.
  - `:compose`: UI library containing reusable Compose components.
  - `:data`: Data layer containing repositories and network logic.
  - `:domain`: Core business logic containing UseCases and Entities (Non-Android).
- **Dependency Injection**: Use **Koin**. ViewModels should implement `KoinComponent` and use `by inject()`.

## ViewModels
- All ViewModels must extend `BaseViewModel`.
- Use `viewModelScope` for coroutines.
- **Coroutines**:
  - Always use `dispatcherProvider` (injected in `BaseViewModel`) instead of `Dispatchers`.
  - Use `launchIO { ... }` for background tasks.
  - Use `runOnMain { ... }` for switching back to the UI thread.
- **State Management**:
  - Use `MutableStateFlow` for internal state and expose as `StateFlow`.
  - Use `groundState(initialValue)` to convert `Flow` to `StateFlow`.
  - Do not use `LiveData`. Use `SharedFlow` and `event()` for navigation via `externalNavigationEvent`.
  - Use helper methods like `setValue()` and `emit()` (defined in `BaseViewModel`) to update flows without explicit casting.

## UI & Resources
- Use **Jetpack Compose** for all new UI.
- Use `showLoadingIndicator.wrapIndicator { ... }` to automatically manage loading states for async operations.

## Logging & Error Handling
- Use **Timber** for logging.
- Use `handleError(@StringRes messageId: Int)` or `notifyUser(@StringRes messageId: Int)` for simple user notifications.
- Use `onFailureLogged` extension on `Result<T>` to handle repository failures with automatic logging and optional toast display.
- Use `Toaster` (injected) for displaying messages.

## Code Style
- Follow the rules defined in `.editorconfig`.
- Group properties/methods using `// region` comments as seen in `BaseViewModel.kt`.
