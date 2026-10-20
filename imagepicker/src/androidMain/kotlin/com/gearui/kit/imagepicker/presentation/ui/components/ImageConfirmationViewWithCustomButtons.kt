package com.gearui.kit.imagepicker.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hd
import androidx.compose.material.icons.filled.Sd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import coil3.compose.AsyncImage
import com.gearui.components.button.Button
import com.gearui.components.button.ButtonShape
import com.gearui.components.button.ButtonSize
import com.gearui.components.button.ButtonTheme
import com.gearui.components.button.ButtonType
import com.gearui.components.icon.Icons as GearIcons
import imagepicker.imagepicker.generated.resources.Res
import imagepicker.imagepicker.generated.resources.accept_button
import imagepicker.imagepicker.generated.resources.hd_quality_description
import imagepicker.imagepicker.generated.resources.image_confirmation_title
import imagepicker.imagepicker.generated.resources.preview_image_description
import imagepicker.imagepicker.generated.resources.retry_button
import imagepicker.imagepicker.generated.resources.sd_quality_description
import com.gearui.kit.imagepicker.presentation.config.ImagePickerUiConstants
import com.gearui.kit.imagepicker.presentation.config.UiConfig
import com.gearui.kit.imagepicker.domain.models.ImageAsset
import org.jetbrains.compose.resources.stringResource

@SuppressLint("ConfigurationScreenWidthHeight")
@Suppress("EndOfSentenceFormat","LongMethod","FunctionNaming")
@Composable
fun ImageConfirmationViewWithCustomButtons(
    result: ImageAsset,
    onConfirm: (ImageAsset) -> Unit,
    onRetry: () -> Unit,
    customConfirmationView: (@Composable (ImageAsset, (ImageAsset) -> Unit, () -> Unit) -> Unit)? = null,
    uiConfig: UiConfig = UiConfig()
) {
    if (customConfirmationView != null) {
        customConfirmationView(result, onConfirm, onRetry)
        return
    }
    val resolvedButtonColor = uiConfig.buttonColor ?: ImagePickerUiConstants.ConfirmationCardBackgroundColor
    val resolvedIconColor = uiConfig.iconColor ?: ImagePickerUiConstants.ConfirmationCardIconColor
    val resolvedButtonSize = uiConfig.buttonSize ?: ImagePickerUiConstants.ConfirmationCardButtonSize
    
    val configuration = LocalConfiguration.current
    val isLandscape = remember(configuration) { 
        configuration.screenWidthDp > configuration.screenHeightDp 
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ImagePickerUiConstants.ConfirmationCardDialogBackground)
            .padding(horizontal = ImagePickerUiConstants.ConfirmationCardDialogHorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        if (isLandscape) {
            LandscapeConfirmationLayout(
                result = result,
                onConfirm = onConfirm,
                onRetry = onRetry,
                resolvedButtonColor = resolvedButtonColor,
                resolvedIconColor = resolvedIconColor,
                resolvedButtonSize = resolvedButtonSize,
                uiConfig = uiConfig
            )
        } else {
            Box(
                modifier = Modifier
                    .widthIn(max = ImagePickerUiConstants.ConfirmationCardMaxWidth)
                    .clip(RoundedCornerShape(ImagePickerUiConstants.ConfirmationCardCornerRadius))
                    .background(resolvedButtonColor)
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                   Box(modifier = Modifier
                       .fillMaxWidth()
                       .aspectRatio(ImagePickerUiConstants.ConfirmationCardImageAspectRatio)){
                       AsyncImage(
                           model = result.uri,
                           contentDescription = stringResource(Res.string.preview_image_description),
                           modifier = Modifier
                               .fillMaxSize()
                               .aspectRatio(ImagePickerUiConstants.ConfirmationCardImageAspectRatio)
                               .clip(RoundedCornerShape(topStart = ImagePickerUiConstants.ConfirmationCardCornerRadius,
                                   topEnd = ImagePickerUiConstants.ConfirmationCardCornerRadius)),
                           contentScale = ContentScale.Crop
                       )
                       val isHD = (result.width ?: 0) >= 1280 && (result.height ?: 0) >= 720
                       Column( modifier = Modifier
                           .align(Alignment.TopEnd)
                           .padding(ImagePickerUiConstants.ConfirmationCardPadding),
                           verticalArrangement = Arrangement
                               .spacedBy(ImagePickerUiConstants.ConfirmationCardButtonSpacing)) {
                           Box(
                               modifier = Modifier
                                   .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                                   .size(resolvedButtonSize),
                               contentAlignment = Alignment.Center
                           ) {
                               androidx.compose.foundation.Image(
                                   painter = rememberVectorPainter(if (isHD) Icons.Default.Hd else Icons.Default.Sd),
                                   contentDescription = if (isHD) stringResource(Res.string.hd_quality_description)
                                   else stringResource(Res.string.sd_quality_description),
                                   colorFilter = ColorFilter.tint(resolvedIconColor)
                               )
                           }
                       }
                   }
                    Spacer(modifier = Modifier.height(ImagePickerUiConstants.ConfirmationCardSpacerHeight))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(resolvedButtonColor)
                            .padding(horizontal = ImagePickerUiConstants.ConfirmationCardPadding,
                                vertical = ImagePickerUiConstants.ConfirmationCardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BasicText(
                            text = stringResource(Res.string.image_confirmation_title),
                            style = TextStyle(
                                color = ImagePickerUiConstants.ConfirmationCardTitleColor,
                                fontSize = ImagePickerUiConstants.ConfirmationCardTitleFontSize,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(ImagePickerUiConstants
                            .ConfirmationCardButtonSpacing.plus(4.dp)))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement
                                .spacedBy(ImagePickerUiConstants.ConfirmationCardButtonSpacing,
                                    Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(resolvedButtonSize)
                            ) {
                                Button(
                                    text = stringResource(Res.string.retry_button),
                                    onClick = onRetry,
                                    icon = GearIcons.refresh,
                                    block = true,
                                    theme = ButtonTheme.DEFAULT,
                                    type = ButtonType.OUTLINE,
                                    size = ButtonSize.MEDIUM,
                                    shape = ButtonShape.ROUND
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(resolvedButtonSize)
                            ) {
                                Button(
                                    text = stringResource(Res.string.accept_button),
                                    onClick = { onConfirm(result) },
                                    icon = GearIcons.done,
                                    block = true,
                                    theme = ButtonTheme.SUCCESS,
                                    type = ButtonType.FILL,
                                    size = ButtonSize.MEDIUM,
                                    shape = ButtonShape.ROUND
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
