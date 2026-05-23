package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import client.command.ClientNetworkHelper;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.EmptyPayload;
import common.network.payload.InfoPayload;

import java.time.format.DateTimeFormatter;

/**
 * Client command info.
 */
public final class InfoCommand implements ClientCommand {

    @Override public String keyword() { return "info"; }
    @Override public String summary() { return "вывести информацию о коллекции"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        ClientCommandResult noArgs = CommandSupport.noArgs(keyword(), args);
        if (noArgs != null) {
            return noArgs;
        }
        var outcome = ClientNetworkHelper.send(ctx, new Request(CommandType.INFO, EmptyPayload.INSTANCE));
        if (outcome.isNetworkError()) {
            return ClientCommandResult.error(outcome.errorText());
        }
        if (!outcome.response().isOk()) {
            return ClientCommandResult.error(outcome.response().getMessage());
        }
        InfoPayload info = (InfoPayload) outcome.response().getPayload();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String init = info.getInitTime() == null ? "не инициализировано" : formatter.format(info.getInitTime());
        String saved = info.getLastSaveTime() == null ? "не сохранялось" : formatter.format(info.getLastSaveTime());
        return ClientCommandResult.ok("Тип: " + info.getCollectionType() + System.lineSeparator()
                + "Время инициализации: " + init + System.lineSeparator()
                + "Время последнего сохранения: " + saved + System.lineSeparator()
                + "Размер: " + info.getSize());
    }
}
