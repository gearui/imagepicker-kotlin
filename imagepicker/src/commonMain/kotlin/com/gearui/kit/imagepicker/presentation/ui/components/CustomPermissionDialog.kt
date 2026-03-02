package com.gearui.kit.imagepicker.presentation.ui.components

import androidx.compose.runtime.Composable
import com.gearui.components.dialog.AlertDialog
import com.gearui.components.dialog.ConfirmDialog

@Composable
fun CustomPermissionDialog(
    title: String,
    description: String,
    confirmationButtonText: String,
    onConfirm: () -> Unit,
    cancelButtonText: String? = null,
    onCancel: (() -> Unit)? = null
) {
    if (cancelButtonText != null && onCancel != null) {
        ConfirmDialog(
            visible = true,
            title = title,
            message = description,
            confirmText = confirmationButtonText,
            cancelText = cancelButtonText,
            onConfirm = onConfirm,
            onCancel = onCancel,
            onDismiss = onCancel,
            dismissOnOutside = false
        )
    } else {
        AlertDialog(
            visible = true,
            title = title,
            message = description,
            buttonText = confirmationButtonText,
            onConfirm = onConfirm,
            onDismiss = onConfirm,
            dismissOnOutside = false
        )
    }
}

