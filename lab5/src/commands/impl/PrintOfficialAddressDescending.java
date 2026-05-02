package commands.impl;

import commands.CommandAction;
import commands.CommandOutcome;
import core.ApplicationContext;
import model.Address;
import model.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * Выводит значения поля officialAddress в порядке убывания.
 */
public class PrintOfficialAddressDescending implements CommandAction {
    /**
     * Создает команду вывода адресов по убыванию.
     */
    public PrintOfficialAddressDescending() {
    }

    @Override
    public String keyword() {
        return "print_field_descending_official_address";
    }

    @Override
    public String summary() {
        return "вывести officialAddress по убыванию";
    }

    @Override
    public CommandOutcome run(String args, ApplicationContext context) {
        if (args != null && !args.isEmpty()) {
            return new CommandOutcome(false,
                    "Команда 'print_field_descending_official_address' не принимает аргументы.", false);
        }
        List<Organization> all = context.repository().list();
        if (all.isEmpty()) {
            return new CommandOutcome(true, "Коллекция пуста.", false);
        }
        List<Address> addresses = new ArrayList<>();
        for (Organization org : all) {
            addresses.add(org.getOfficialAddress());
        }
        addresses.sort((a, b) -> {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null) {
                return 1;
            }
            if (b == null) {
                return -1;
            }
            return b.toSortableString().compareTo(a.toSortableString());
        });
        StringBuilder sb = new StringBuilder();
        for (Address address : addresses) {
            sb.append(address).append(System.lineSeparator());
        }
        return new CommandOutcome(true, sb.toString().trim(), false);
    }
}
