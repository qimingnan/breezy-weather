package wangdaye.com.geometricweather.weather.json.mf

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import wangdaye.com.geometricweather.common.serializer.DateSerializer
import java.util.Date

@Serializable
data class MfRainResult(
    @SerialName("update_time") @Serializable(DateSerializer::class) val updateTime: Date?,
    val properties: MfRainProperties?
)