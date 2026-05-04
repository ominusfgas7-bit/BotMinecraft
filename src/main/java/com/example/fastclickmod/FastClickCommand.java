package com.example.fastclickmod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class FastClickCommand extends CommandBase {
    private final ClickMultiplierController controller;

    public FastClickCommand(ClickMultiplierController controller) {
        this.controller = controller;
    }

    @Override
    public String getCommandName() {
        return "fclk";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/fclk help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("fastclick");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            sendHelp(sender);
            return;
        }

        if ("enable".equalsIgnoreCase(args[0])) {
            controller.setEnabled(true);
            sender.addChatMessage(new ChatComponentText("[FCLK] Enabled."));
            return;
        }

        if ("disable".equalsIgnoreCase(args[0])) {
            controller.setEnabled(false);
            sender.addChatMessage(new ChatComponentText("[FCLK] Disabled."));
            return;
        }

        if (args.length == 4
                && "clicks".equalsIgnoreCase(args[0])
                && "set".equalsIgnoreCase(args[1])) {
            String side = args[2].toLowerCase();
            int value;

            try {
                value = parseInt(args[3]);
            } catch (NumberInvalidException ex) {
                sender.addChatMessage(new ChatComponentText("[FCLK] Invalid number: " + args[3]));
                return;
            }

            if (value < 0 || value > 50) {
                sender.addChatMessage(new ChatComponentText("[FCLK] Value must be between 0 and 50."));
                return;
            }

            if ("left".equals(side)) {
                controller.setExtraLeftClicks(value);
                sender.addChatMessage(new ChatComponentText("[FCLK] Left extra clicks set to " + value + "."));
                return;
            }

            if ("right".equals(side)) {
                controller.setExtraRightClicks(value);
                sender.addChatMessage(new ChatComponentText("[FCLK] Right extra clicks set to " + value + "."));
                return;
            }

            sender.addChatMessage(new ChatComponentText("[FCLK] Side must be left or right."));
            return;
        }

        sender.addChatMessage(new ChatComponentText("[FCLK] Unknown command. Use /fclk help"));
    }

    private void sendHelp(ICommandSender sender) {
        sender.addChatMessage(new ChatComponentText("[FCLK] Commands:"));
        sender.addChatMessage(new ChatComponentText("/fclk enable"));
        sender.addChatMessage(new ChatComponentText("/fclk disable"));
        sender.addChatMessage(new ChatComponentText("/fclk clicks set left <0-50>"));
        sender.addChatMessage(new ChatComponentText("/fclk clicks set right <0-50>"));
        sender.addChatMessage(new ChatComponentText("/fclk help"));
    }
}
