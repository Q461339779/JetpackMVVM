package com.xhsj.commonlib.base.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xhsj.commonlib.base.ext.view.getVmClazz
import com.xhsj.commonlib.base.viewmodel.BaseViewModel
import com.xhsj.commonlib.network.manager.NetState
import com.xhsj.commonlib.network.manager.NetworkStateManager

/**
 * viewModelActivity 基类 把viewModel注入进来
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    /**
     * 是否使用dataBinding
     */
    private var isUseDB = false

    /**
     * 定义VM类型的viewModel
     */
    lateinit var mViewModel: VM

    /**
     * 加载布局文件
     */
    abstract fun layoutId() : Int

    /**
     * 初始化View
     *
     * var name: String = null//Error:Null can not be a value of a non-null type String
     * var name1: String? = null//可空类型，可以赋值为null
     */
    abstract fun initView(saveIntanceState :Bundle?)

    /**
     * 显示对话框
     */
    abstract fun showLoading(message:String = "网络请求中。。。")

    /**
     * 隐藏对话框
     */
    abstract fun dismissLoading()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isUseDB){
            setContentView(layoutId())
        }else{
            initDataBinding()
        }
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = careateViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.intance.mNetworkstateCallback.observeInActivity(this, Observer {
            onNetworkStateChanged(it)

        })

    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {

        mViewModel.loadingChange.showDialog.observeInActivity(this , Observer{
            showLoading(it)
        })

        mViewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
            dismissLoading()
        })
    }

    /**
     * 创建viewModel
     */
    private fun careateViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }


    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel){
        viewModels.forEach {viewModel ->
            //显示弹窗
            viewModel.loadingChange.showDialog.observeInActivity(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissDialog.observeInActivity(this, Observer {
                dismissLoading()
            })
        }
    }

    fun userDataBinding(isUserDb: Boolean) {
        this.isUseDB = isUserDb
    }

    /**
     * 提供给BaseVmDataBinding 初始化DataBinding
     */
    open fun initDataBinding() {}


}