package com.hinacle.base.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.hinacle.base.R
import com.hinacle.base.vm.AppViewModel


abstract class AppDialogFragment<V : ViewDataBinding, VM : AppViewModel>(
    private val layoutId: Int,
    private val fm: FragmentManager
) : AppCompatDialogFragment() {
    private val TAG = this::class.simpleName
    private var _binding: V? = null
    val binding get() = _binding!!
    abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        lifecycle.addObserver(viewModel)
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    open fun initView() {}
    open fun initData() {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show() {
        super.show(fm, TAG)
    }
}