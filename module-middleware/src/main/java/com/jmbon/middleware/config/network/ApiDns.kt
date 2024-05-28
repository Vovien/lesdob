package com.jmbon.middleware.config.network

import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*


class ApiDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        return if (hostname.isEmpty()) {
            throw UnknownHostException("hostname == null")
        } else {
            try {
                val mInetAddressesList: MutableList<InetAddress> = ArrayList()
                val mInetAddresses = InetAddress.getAllByName(hostname)
                for (address in mInetAddresses) {
                    if (address is Inet4Address) {
                        mInetAddressesList.add(0, address)
                    } else {
                        mInetAddressesList.add(address)
                    }
                }
                mInetAddressesList
            } catch (ex: NullPointerException) {
                val unknownHostException = UnknownHostException("Broken system behaviour")
                unknownHostException.initCause(ex)
                throw unknownHostException
            }
        }
    }
}