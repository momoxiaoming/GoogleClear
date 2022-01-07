package com.mckj.sceneslib.manager.network

/**
 * Describe:Wifi连接信息
 *
 * Created By yangb on 2020/10/15
 */
data class ConnectInfo(
    val networkType: NetworkType,
    val name: String = "",
    val ip: String = "",
    val speed: String = "",
    val mac: String = "",
    val level: Int = -100
) {

    enum class NetworkType {
        /**
         * 未连接
         */
        NOT_CONNECTED,
        WIFI,
        MOBILE_2G,
        MOBILE_3G,
        MOBILE_4G,
        MOBILE_5G,

        /**
         * 未知网络类型
         */
        UNKNOWN
    }

    override fun equals(other: Any?): Boolean {
        if (other is ConnectInfo) {
            return networkType == other.networkType
                    && name == other.name
                    && ip == other.ip
                    && speed == other.speed
                    && mac == other.mac
                    && level == other.level
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = networkType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + ip.hashCode()
        result = 31 * result + speed.hashCode()
        result = 31 * result + mac.hashCode()
        result = 31 * result + level
        return result
    }

}
