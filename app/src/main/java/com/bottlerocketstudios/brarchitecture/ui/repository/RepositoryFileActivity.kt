package com.bottlerocketstudios.brarchitecture.ui.repository

/*
class RepositoryFileActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, repo: Repository, file: RepoFile): Intent {
            val i = Intent(context, RepositoryFileActivity::class.java)
            i.putExtra(EXTRA_REPO, repo)
            i.putExtra(EXTRA_FILE, file)
            return i
        }
        
        const val EXTRA_REPO = "hash_id"
        const val EXTRA_FILE = "path_id"
    }
		
    private val repository_fileViewModel: RepositoryFileActivityViewModel by lazy {
        getProvidedViewModel(RepositoryFileActivityViewModel::class.java)
    }
		
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<RepositoryFileActivityBinding>(this, R.layout.repository_file_activity).apply {
            viewModel = repository_fileViewModel
            val repo : Repository = intent.getParcelableExtra(EXTRA_REPO)
            val file : RepoFile = intent.getParcelableExtra(EXTRA_FILE)
            repository_fileViewModel.loadFile(
                repo.owner?.nickname?:"",
                repo.name?:"",
                file.mimetype?:"",
                file.commit?.hash?:"",
                file.path?:"")
            setLifecycleOwner(this@RepositoryFileActivity)
        }
    }
}*/
