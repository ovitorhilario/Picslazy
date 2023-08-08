package com.vitorhilarioapps.picslazy.ui.home.editphoto

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorhilarioapps.picslazy.common.singletons.Filters
import com.vitorhilarioapps.picslazy.domain.use_case.editphoto.ApplyFilterUseCase
import com.vitorhilarioapps.picslazy.domain.use_case.editphoto.GetPreviewBitmapUseCase
import com.vitorhilarioapps.picslazy.domain.use_case.editphoto.SaveImageUseCase
import com.vitorhilarioapps.picslazy.domain.use_case.editphoto.UriToBitmapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPhotoViewModel @Inject constructor(
    private val uriToBitmapUseCase: UriToBitmapUseCase,
    private val getPreviewBitmapUseCase: GetPreviewBitmapUseCase,
    private val applyFilterUseCase: ApplyFilterUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val _filtersState = MutableStateFlow(FiltersState())
    val filtersState: StateFlow<FiltersState> = _filtersState

    private val _photoState = MutableStateFlow(PhotoState())
    val photoState: StateFlow<PhotoState> = _photoState

    private val _saveState: MutableStateFlow<SaveState> = MutableStateFlow(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState

    private fun generatePreviews(originBitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val previewBitmap = getPreviewBitmap(originBitmap)
            val filteredBitmapList = arrayListOf<Pair<Bitmap, String>>()

            for (filterPair in Filters.list) {
                var filteredBitmap: Bitmap? = null
                val (filter, tittle) = filterPair

                filteredBitmap = if (filter != null) {
                    applyFilter(previewBitmap, filter)
                } else {
                    previewBitmap
                }

                filteredBitmapList.add(filteredBitmap to tittle)
            }

            _filtersState.value = _filtersState.value.copy(data = filteredBitmapList)
        }
    }

    fun setOriginUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val originBitmap = uriToBitmap(uri)
            _photoState.value = _photoState.value.copy(originBitmap = originBitmap, selection = 0)
            updatePhotoState()
            generatePreviews(originBitmap)
        }
    }

    fun updatePhotoState(newSelection: Int? = null, ) {
        val currentSelection = newSelection ?: (photoState.value.selection)

        viewModelScope.launch(Dispatchers.IO) {
            _photoState.value.originBitmap?.let { bitmap ->
                if (currentSelection > 0 && currentSelection < Filters.list.size) {
                    Filters.list[currentSelection].first?.let { filter ->
                        val filteredBitmap = applyFilter(bitmap, filter)
                        _photoState.value = _photoState.value.copy(selectedBitmap = filteredBitmap, selection = currentSelection)
                    }
                } else {
                    _photoState.value = _photoState.value.copy(selectedBitmap = bitmap, selection = currentSelection)
                }
            }
        }
    }

    suspend fun saveSelectedBitmap(): Boolean {
        _saveState.value = SaveState.Loading
        val bitmap = _photoState.value.selectedBitmap
        var success = true

        val saveJob = viewModelScope.launch(Dispatchers.IO) {
            success = bitmap?.let { saveImageUseCase(it) } ?: false
        }

        saveJob.join()
        _saveState.value = SaveState.Idle

        return success
    }

    private suspend fun uriToBitmap(uri: Uri): Bitmap {
        return uriToBitmapUseCase(uri)
    }

    private suspend fun getPreviewBitmap(bitmap: Bitmap): Bitmap {
        return getPreviewBitmapUseCase(bitmap)
    }

    private suspend fun applyFilter(bitmap: Bitmap, filter: GPUImageFilter): Bitmap {
        return applyFilterUseCase(bitmap, filter)
    }

    data class PhotoState(
        var originBitmap: Bitmap? = null,
        var selection: Int = 0,
        var selectedBitmap: Bitmap? = null
    )

    data class FiltersState(
        var data: ArrayList<Pair<Bitmap, String>> = arrayListOf()
    )

    sealed class SaveState {
        object Idle: SaveState()
        object Loading: SaveState()
    }
}
