package com.kilomobi.washy.model

import android.content.Context
import com.google.android.material.chip.Chip
import com.kilomobi.washy.R

// Enum would imply performance issue
class Service {
    companion object {
        private val COFFEE: ChipModel = ChipModel("coffee",
            R.drawable.ic_coffee_outline,
            R.string.chip_coffee
        )
        private val WIFI: ChipModel = ChipModel("wifi",
            R.drawable.ic_wifi,
            R.string.chip_wifi
        )
        private val ELECTRIC_PLUG: ChipModel = ChipModel("ev_plug",
            R.drawable.ic_ev_station,
            R.string.chip_electric_plug
        )
        private val CREDIT_CARD: ChipModel = ChipModel("credit_card",
            R.drawable.ic_credit_card,
            R.string.chip_cb
        )
        private val BIKE: ChipModel = ChipModel("bike",
            R.drawable.ic_motorcycle,
            R.string.chip_motorbike
        )
        private val HAND_WASH: ChipModel = ChipModel("hand_wash",
            R.drawable.ic_hand_water,
            R.string.chip_hand_wash
        )
        private val HEADLIGHT: ChipModel = ChipModel("headlight",
            R.drawable.ic_car_light_dimmed,
            R.string.chip_headlight
        )
        private val TRANSPORT: ChipModel = ChipModel("transport",
            R.drawable.ic_train_car,
            R.string.chip_transport
        )
        private val ECO_FRIENDLY: ChipModel = ChipModel("eco_friendly",
            R.drawable.ic_leaf,
            R.string.chip_eco
        )
        private val CERAMIC: ChipModel = ChipModel("ceramic",
            R.drawable.ic_car_convertible,
            R.string.chip_ceramic
        )
        private val INTERIOR: ChipModel = ChipModel("interior",
            R.drawable.ic_car_seat,
            R.string.chip_interior
        )
        private val EXTERIOR: ChipModel = ChipModel("exterior",
            R.drawable.ic_car_convertible,
            R.string.chip_exterior
        )
        private val WINDSHIELD: ChipModel = ChipModel("windshield",
            R.drawable.ic_car_windshield_outline,
            R.string.chip_windshield
        )

        private val serviceList = listOf(
            COFFEE,
            CREDIT_CARD,
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
            val ecoFriendly = context.getString(R.string.chip_eco)
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
                    ecoFriendly -> arrayService.add(context.getString(R.string.chip_db_eco))
                    motorcycle -> arrayService.add(context.getString(R.string.chip_db_motorbike))
                }
            }

            return arrayService
        }

        fun retrieveImage(service: String): Int? {
            return serviceList.find { chipModel -> chipModel.id == service }?.image
        }

        fun retrieveText(service: String): Int? {
            return serviceList.find { chipModel -> chipModel.id == service }?.text
        }
    }
}