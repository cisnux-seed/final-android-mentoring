package dev.cisnux.dicodingmentoring.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import dev.cisnux.dicodingmentoring.AuthenticatedUser
import java.io.InputStream
import java.io.OutputStream

object AuthenticatedUserSerializer : Serializer<AuthenticatedUser> {
    override val defaultValue: AuthenticatedUser
        get() = AuthenticatedUser.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthenticatedUser = try {
        AuthenticatedUser.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: AuthenticatedUser, output: OutputStream) = t.writeTo(output)
}