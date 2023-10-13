package com.created.team201.data.mapper

import com.created.domain.model.AppUpdateInformation
import com.created.team201.data.remote.response.AppUpdateInformationResponseDto

fun AppUpdateInformationResponseDto.toDomain(): AppUpdateInformation =
    AppUpdateInformation(
        shouldUpdate = shouldUpdate,
        message = message,
    )
