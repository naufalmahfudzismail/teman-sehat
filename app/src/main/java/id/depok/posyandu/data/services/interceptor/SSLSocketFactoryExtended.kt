package id.depok.posyandu.data.services.interceptor

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.*
import kotlin.collections.ArrayList

open class SSLSocketFactoryExtended : SSLSocketFactory() {

    private val mSSLContext: SSLContext by lazy {
        SSLContext.getInstance("TLS")
    }
    private var ciphers: Array<String> = arrayOf()
    private var protocols: Array<String> = arrayOf()

    @Throws(IOException::class)
    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        val factory: SSLSocketFactory = mSSLContext.socketFactory
        val ss: SSLSocket = factory.createSocket(s, host, port, autoClose) as SSLSocket
        ss.enabledProtocols = protocols
        ss.enabledCipherSuites = supportedCipherSuites
        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress?,
        port: Int,
        localAddress: InetAddress?,
        localPort: Int,
    ): Socket {
        val factory: SSLSocketFactory = mSSLContext.getSocketFactory()
        val ss: SSLSocket =
            factory.createSocket(address, port, localAddress, localPort) as SSLSocket
        ss.enabledProtocols = protocols
        ss.enabledCipherSuites = supportedCipherSuites
        return ss
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return ciphers
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return ciphers
    }


    @Throws(IOException::class)
    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        val factory: SSLSocketFactory = mSSLContext.socketFactory
        val ss: SSLSocket = factory.createSocket(host, port, localHost, localPort) as SSLSocket
        ss.enabledProtocols = protocols
        ss.enabledCipherSuites = supportedCipherSuites
        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress?, port: Int): Socket {
        val factory: SSLSocketFactory = mSSLContext.socketFactory
        val ss: SSLSocket = factory.createSocket(host, port) as SSLSocket
        ss.enabledProtocols = protocols
        ss.enabledCipherSuites = supportedCipherSuites
        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(host: String?, port: Int): Socket {
        val factory: SSLSocketFactory = mSSLContext.socketFactory
        val ss: SSLSocket = factory.createSocket(host, port) as SSLSocket
        ss.enabledProtocols = protocols
        ss.enabledCipherSuites = supportedCipherSuites
        return ss
    }

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun initSSLSocketFactoryEx(
    ) {
        mSSLContext.init(null, null, null)
        protocols = getProtocolList()
        ciphers = getCipherList()
    }

    private fun getProtocolList(): Array<String> {
        val protocols = arrayOf("TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3")
        var availableProtocols: Array<String?>? = null
        var socket: SSLSocket? = null
        try {
            val factory: SSLSocketFactory = mSSLContext.socketFactory
            socket = factory.createSocket() as SSLSocket
            availableProtocols = socket.supportedProtocols
        } catch (e: Exception) {
            return arrayOf("TLSv1")
        } finally {
            if (socket != null) try {
                socket.close()
            } catch (e: IOException) {
            }
        }
        val resultList: MutableList<String> = ArrayList()
        for (i in protocols.indices) {
            availableProtocols?.let {
                val idx: Int = Arrays.binarySearch(availableProtocols, protocols[i])
                if (idx >= 0) resultList.add(protocols[i])
            }
        }
        return resultList.toTypedArray()
    }

    private fun getCipherList(): Array<String> {
        val resultList: MutableList<String> = ArrayList()
        val factory: SSLSocketFactory = mSSLContext.socketFactory
        for (s in factory.supportedCipherSuites) {
            resultList.add(s)
        }
        return resultList.toTypedArray()
    }

    init {
        initSSLSocketFactoryEx()
    }

}