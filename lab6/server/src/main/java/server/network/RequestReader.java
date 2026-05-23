package server.network;

import common.network.MessageFragment;
import common.network.Request;
import common.network.util.SerializationUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Reads request datagrams and deserializes complete messages.
 */
public final class RequestReader {

    /**
     * Deserializes one datagram into a message fragment.
     *
     * @param buffer buffer ready for reading
     * @return message fragment
     * @throws IOException if bytes cannot be read
     * @throws ClassNotFoundException if class is missing
     */
    public MessageFragment parseFragment(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return SerializationUtil.fromBytes(data);
    }

    /**
     * Deserializes a complete reassembled request.
     *
     * @param data complete serialized request bytes
     * @return request
     * @throws IOException if deserialization fails
     * @throws ClassNotFoundException if class is missing
     */
    public Request parseMessage(byte[] data) throws IOException, ClassNotFoundException {
        return SerializationUtil.fromBytes(data);
    }
}
