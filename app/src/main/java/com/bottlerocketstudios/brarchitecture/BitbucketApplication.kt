package com.bottlerocketstudios.brarchitecture

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bottlerocketstudios.brarchitecture.infrastructure.auth.TokenAuthRepository
import com.bottlerocketstudios.brarchitecture.infrastructure.repository.BitbucketRepository
import com.bottlerocketstudios.brarchitecture.ui.RepoViewModel
import timber.log.Timber
import java.lang.reflect.InvocationTargetException

class BitbucketApplication : Application() {
    val factory: BitbucketViewModelFactory
        get() = BitbucketViewModelFactory(this, repository)
    lateinit var repository: BitbucketRepository

    class BitbucketViewModelFactory(val app: Application, val repo: BitbucketRepository) : ViewModelProvider.AndroidViewModelFactory(app) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (RepoViewModel::class.java.isAssignableFrom(modelClass)) {
                try {
                    return modelClass.getConstructor(Application::class.java, BitbucketRepository::class.java).newInstance(app, repo);
                } catch (e: NoSuchMethodException) {
                    throw RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (e: IllegalAccessException) {
                    throw RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (e: InstantiationException) {
                    throw RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (e: InvocationTargetException) {
                    throw RuntimeException("Cannot create an instance of " + modelClass, e);
                }
            }
            return super.create(modelClass);
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        repository = BitbucketRepository(TokenAuthRepository(TokenAuthRepository.retrofit))
    }
}