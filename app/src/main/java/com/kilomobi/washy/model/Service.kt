package com.kilomobi.washy.model

import android.content.Context
import com.google.android.material.chip.Chip
import com.kilomobi.washy.R

// Enum would imply performance issue
class Service {
    companion object {
        private val COFFEE: Pair<String, Int> = Pair("coffee",
            R.drawable.ic_coffee
        )
        private val WIFI: Pair<String, Int> = Pair("wifi",
            R.drawable.ic_signal_wifi
        )
        private val ELECTRIC_PLUG: Pair<String, Int> = Pair("electric_plug",
            R.drawable.ic_ev_station
        )
        private val BIKE: Pair<String, Int> = Pair("bike",
            R.drawable.ic_motorcycle
        )
        private val HAND_WASH: Pair<String, Int> = Pair("hand_wash",
            R.drawable.ic_hand_water
        )
        private val HEADLIGHT: Pair<String, Int> = Pair("headlight",
            R.drawable.ic_car_light_dimmed
        )
        private val TRANSPORT: Pair<String, Int> = Pair("transport",
            R.drawable.ic_train_car
        )
        private val ECO_FRIENDLY: Pair<String, Int> = Pair("eco_friendly",
            R.drawable.ic_leaf
        )
        private val CERAMIC: Pair<String, Int> = Pair("ceramic",
            R.drawable.ic_car_convertible
        )
        private val INTERIOR: Pair<String, Int> = Pair("interior",
            R.drawable.ic_car_seat
        )
        private val EXTERIOR: Pair<String, Int> = Pair("exterior",
            R.drawable.ic_car_convertible
        )
        private val WINDSHIELD: Pair<String, Int> = Pair("windshield",
            R.drawable.ic_car_windshield_outline
        )

        private val serviceList = listOf(
            COFFEE,
            WIFI,
            ELECTRIC_PLUG,
            BIKE,
            HAND_WASH,
            HEADLIGHT,
            TRANSPORT,
            ECO_FRIENDLY,
            CERAMIC,
            INTERIOR,
            EXTERIOR,
            WINDSHIELD
        )

        fun servicesToDatabase(context: Context, service: ArrayList<Chip>): ArrayList<String> {
            val arrayService = ArrayList<String>()

            val headlight = context.getString(R.string.chip_headlight)
            val interior = context.getString(R.string.chip_interior)
            val exterior = context.getString(R.string.chip_exterior)
            val windshield = context.getString(R.string.chip_windshield)
            val electricPlug = context.getString(R.string.chip_electric_plug)
            val transport = context.getString(R.string.chip_transport)
            val cb = context.getString(R.string.chip_cb)
            val coffee = context.getString(R.string.chip_coffee)
            val wifi = context.getString(R.string.chip_wifi)
            val ceramic = context.getString(R.string.chip_ceramic)
            val handWash = context.getString(R.string.chip_hand_wash)
            val leaf = context.getString(R.string.chip_eco)
            val motorcycle = context.getString(R.string.chip_motorbike)

            for (s in service) {
                when(s.text) {
                    headlight -> arrayService.add(context.getString(R.string.chip_db_headlight))
                    interior -> arrayService.add(context.getString(R.string.chip_db_interior))
                    exterior -> arrayService.add(context.getString(R.string.chip_db_exterior))
                    windshield -> arrayService.add(context.getString(R.string.chip_db_windshield))
                    electricPlug -> arrayService.add(context.getString(R.string.chip_db_electric_plug))
                    transport -> arrayService.add(context.getString(R.string.chip_db_transport))
                    cb -> arrayService.add(context.getString(R.string.chip_db_cb))
                    coffee -> arrayService.add(context.getString(R.string.chip_db_coffee))
                    wifi -> arrayService.add(context.getString(R.string.chip_db_wifi))
                    ceramic -> arrayService.add(context.getString(R.string.chip_db_ceramic))
                    handWash -> arrayService.add(context.getString(R.string.chip_db_hand_wash))
                    leaf -> arrayService.add(context.getString(R.string.chip_db_eco))
                    motorcycle -> arrayService.add(context.getString(R.string.chip_db_motorbike))
                }
            }

            return arrayService
        }

        fun retrieveImage(service: String): Int {
            return serviceList.find { pair -> pair.first == service }!!.second
        }
    }
}