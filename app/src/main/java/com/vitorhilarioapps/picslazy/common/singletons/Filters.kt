package com.vitorhilarioapps.picslazy.common.singletons

import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageEmbossFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHazeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageKuwaharaFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLightenBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSoftLightBlendFilter

object Filters {
    val list = arrayListOf(
        Pair(null, "Original"),
        Pair(GPUImageKuwaharaFilter(), "Kuwahara"),
        Pair(GPUImageEmbossFilter(), "Emboss"),
        Pair(GPUImageContrastFilter(), "Contrast"),
        Pair(GPUImageSoftLightBlendFilter(), "SoftLight"),
        Pair(GPUImageBrightnessFilter(), "Brightness"),
        Pair(GPUImageGammaFilter(), "Gamma"),
        Pair(GPUImageHazeFilter(), "Haze"),
        Pair(GPUImageLuminanceFilter(), "Luminance"),
        Pair(GPUImageMonochromeFilter(), "Monochrome"),
        Pair(GPUImagePixelationFilter(), "Pixelation"),
        Pair(GPUImageLightenBlendFilter(), "LightenBlend"),
    )
}
