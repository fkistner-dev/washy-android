package com.kilomobi.washy.model

import com.kilomobi.washy.R

// Enum would imply performance issue
class Service {
    companion object {
        val COFFEE: Pair<String, Int> = Pair("coffee",
            R.drawable.ic_coffee
        )
        val WIFI: Pair<String, Int> = Pair("wifi",
            R.drawable.ic_signal_wifi
        )
        val ELECTRIC_PLUG: Pair<String, Int> = Pair("electric_plug",
            R.drawable.ic_ev_station
        )
        val BIKE: Pair<String, Int> = Pair("bike",
            R.drawable.ic_motorcycle
        )
        val HAND_WASH: Pair<String, Int> = Pair("hand_wash",
            R.drawable.ic_hand_water
        )
        val HEADLIGHT: Pair<String, Int> = Pair("headlight",
            R.drawable.ic_car_light_dimmed
        )
        val TRANSPORT: Pair<String, Int> = Pair("transportation",
            R.drawable.ic_train_car
        )
        val ECO_FRIENDLY: Pair<String, Int> = Pair("eco_friendly",
            R.drawable.ic_leaf
        )
        val CERAMIC: Pair<String, Int> = Pair("ceramic",
            R.drawable.ic_car_convertible
        )

        val serviceList = listOf(
            COFFEE,
            WIFI,
            ELECTRIC_PLUG,
            BIKE,
            HAND_WASH,
            HEADLIGHT,
            TRANSPORT,
            ECO_FRIENDLY,
            CERAMIC
        )

        fun servicesToString(vararg service: String): String? {
            val sb = StringBuilder()

            for ((i, arg) in service.withIndex()) {
                sb.append(arg)
                if (i == service.count() - 1) {
                } else {
                    sb.append(",")
                }
            }
            return sb.toString()
        }

        fun servicesToList(services: String): List<String> {
            return services.split(Merchant.SERVICES_DELIMITER)
        }

        fun retrieveImage(service: String): Int {
            return serviceList.find { pair -> pair.first == service }!!.second
        }
    }
}