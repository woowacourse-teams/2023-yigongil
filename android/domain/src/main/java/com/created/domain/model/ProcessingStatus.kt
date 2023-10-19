package com.created.domain.model

enum class ProcessingStatus {
    RECRUITING,
    PROCESSING,
    ;

    companion object {
        fun valueOf(index: Int): ProcessingStatus {
            return ProcessingStatus.values().find { processingStatus ->
                processingStatus.ordinal == index
            } ?: throw IllegalArgumentException("존재 하지 않는 진행 상태입니다")
        }
    }
}
