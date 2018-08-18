package com.emmaguy.castradio.data

enum class CastState(val id: Int) {
    CONNECTED(4),
    CONNECTING(3),
    NOT_CONNECTED(2),
    NO_DEVICES_AVAILABLE(1);

    companion object {
        fun fromId(id: Int): CastState {
            values().forEach { if (it.id == id) return it }

            throw RuntimeException("Failed to get CastState from id " + id)
        }
    }
}