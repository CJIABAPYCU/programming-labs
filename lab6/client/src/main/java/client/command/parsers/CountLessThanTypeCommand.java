package client.command.parsers;

import client.command.ClientCommand;
import client.command.ClientCommandContext;
import client.command.ClientCommandResult;
import common.model.OrganizationType;
import common.network.CommandType;
import common.network.Request;
import common.network.payload.TypePayload;

import java.util.Arrays;
import java.util.Locale;

/**
 * Client command count_less_than_type type.
 */
public final class CountLessThanTypeCommand implements ClientCommand {

    @Override public String keyword() { return "count_less_than_type"; }
    @Override public String summary() { return "вывести количество элементов, type которых меньше заданного"; }

    @Override
    public ClientCommandResult execute(String args, ClientCommandContext ctx) {
        if (args == null || args.isBlank()) {
            return ClientCommandResult.error("Использование: count_less_than_type <type>");
        }
        try {
            OrganizationType type = OrganizationType.valueOf(args.trim().toUpperCase(Locale.ROOT));
            return CommandSupport.simpleRequest(ctx,
                    new Request(CommandType.COUNT_LESS_THAN_TYPE, new TypePayload(type)));
        } catch (IllegalArgumentException e) {
            return ClientCommandResult.error("Недопустимый тип. Допустимые: "
                    + Arrays.toString(OrganizationType.values()));
        }
    }
}
