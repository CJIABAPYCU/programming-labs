package server.command;

import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.core.ServerContext;

public interface ServerCommand {

    CommandType type();

    Response execute(Request request, ServerContext context);
}
