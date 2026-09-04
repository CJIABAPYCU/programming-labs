package common.network;

import common.auth.Credentials;
import common.network.payload.RequestPayload;

import java.io.Serializable;
import java.util.UUID;

public final class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long requestId;
    private final CommandType type;
    private final RequestPayload payload;
    private final Credentials credentials;

    public Request(CommandType type, RequestPayload payload) {
        this(generateId(), type, payload, null);
    }

    public Request(long requestId, CommandType type, RequestPayload payload) {
        this(requestId, type, payload, null);
    }

    public Request(long requestId, CommandType type, RequestPayload payload, Credentials credentials) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
        this.credentials = credentials;
    }

    public Request withCredentials(Credentials creds) {
        return new Request(requestId, type, payload, creds);
    }

    public long getRequestId() { return requestId; }
    public CommandType getType() { return type; }
    public RequestPayload getPayload() { return payload; }
    public Credentials getCredentials() { return credentials; }

    private static long generateId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
