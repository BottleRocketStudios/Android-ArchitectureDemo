package com.bottlerocketstudios.brarchitecture.ui.repository

/*
class RepositoryFolderActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, repo: Repository, folder: RepoFile): Intent {
            val i = Intent(context, RepositoryFolderActivity::class.java)
            i.putExtra(EXTRA_REPO, repo)
            i.putExtra(EXTRA_FOLDER, folder)
            return i
        }

        const val EXTRA_REPO = "repo"
        const val EXTRA_FOLDER = "folder"
    }

    private val repository_folderViewModel: RepositoryFolderActivityViewModel by lazy {
        getProvidedViewModel(RepositoryFolderActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<RepositoryFolderActivityBinding>(this, R.layout.repository_folder_activity).apply {
            viewModel = repository_folderViewModel
            val repo : Repository = intent.getParcelableExtra(EXTRA_REPO)
            val folder : RepoFile = intent.getParcelableExtra(EXTRA_FOLDER)
            file_list.adapter = GroupAdapter<ViewHolder>().apply {
                add(repository_folderViewModel.filesGroup)
                setOnItemClickListener { item, view ->
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type=="commit_directory") {
                        startActivity(RepositoryFolderActivity.newIntent(this@RepositoryFolderActivity, repo, item.viewModel.file))
                    }
                    if (item is ViewModelItem<*> && item.viewModel is RepoFileViewModel && item.viewModel.file.type=="commit_file") {
                        startActivity(RepositoryFileActivity.newIntent(this@RepositoryFolderActivity, repo, item.viewModel.file))
                    }
                }
            }
            file_list.layoutManager = LinearLayoutManager(this@RepositoryFolderActivity)

            repository_folderViewModel.loadRepo(
                repo.owner?.nickname?:"",
                repo.name?:"",
                folder.commit?.hash?:"",
                folder.path?:"")
            setLifecycleOwner(this@RepositoryFolderActivity)
        }
    }
}*/
