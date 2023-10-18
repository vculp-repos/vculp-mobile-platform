package com.example.vculp.features.rideFeedback.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vculp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RideFeedbackBottomSheet: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_ride_feedback_bottom_sheet,container,false)
    }
    companion object {
        const val TAG = "BottomSheetRideFeedback"
    }
}