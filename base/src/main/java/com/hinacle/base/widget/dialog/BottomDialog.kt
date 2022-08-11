package com.hinacle.base.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.hinacle.base.R
import com.hinacle.base.util.dp
import com.hinacle.base.util.onShakeClickListener


class BottomDialog(context:Context) : Dialog(context , cn.vove7.bottomdialog.R.style.BottomDialog) {

    lateinit var bottomSheetLayout :View
    lateinit var root :View

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
////        theme =  cn.vove7.bottomdialog.R.style.BottomDialog
//        val view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet , container , false)
//        return view
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        init(view)
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        if (activity == null) return super.onCreateDialog(savedInstanceState)
//
//        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(
//            requireActivity(), cn.vove7.bottomdialog.R.style.BottomDialog
//        )
//
//        dialog.setContentView(com.hinacle.base.R.layout.dialog_bottom_sheet)
//
//        return dialog
//
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
setContentView(com.hinacle.base.R.layout.dialog_bottom_sheet)
        init()
    }

    fun init(){

        root = findViewById(R.id.root)
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout)
        val behaviorController = BehaviorController(bottomSheetLayout , object : StatusCallback{
            override fun onSlide(slideOffset: Float) {

            }

            override fun onCollapsed() {

            }

            override fun onExpand() {


            }

            override fun onHidden() {


            }
        })
                behaviorController.hide()
        behaviorController.peekHeight = 350.dp
        behaviorController.collapsed()
        root.onShakeClickListener { behaviorController.hide() }
//        bsb = BottomSheetBehavior.from( bottomSheetLayout).apply {
//                    peekHeight = 350.dp
//                    state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//
//        bsb.addBottomSheetCallback(object : BottomSheetCallback(){
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    dismiss()
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//        })
    }

//    lateinit var bsb :  BottomSheetBehavior<View>

}