package xyz.funtimes909.serverseekerv2_discord_bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.funtimes909.serverseekerv2_discord_bot.Main;
import xyz.funtimes909.serverseekerv2_discord_bot.util.PermissionsManager;

import java.io.FileWriter;
import java.io.IOException;

public class Blacklist {
    public static void blacklist(SlashCommandInteractionEvent event) {
        String id = event.getUser().getId();
        String user = event.getOption("user").getAsString();
        String username = event.getOption("user").getAsUser().getName();

        if (!PermissionsManager.ownerCheck(id) && !PermissionsManager.trustedUsersCheck(id)) {
            event.getHook().sendMessage("Sorry! You are not authorized to run this command!").setEphemeral(true).queue();
            return;
        }

        if (user.equals(Main.ownerId)) {
            event.getHook().sendMessage("So you think you're smart huh?").setEphemeral(true).queue();
            return;
        }

        if (PermissionsManager.trustedUsers.contains(user)) {
            event.getHook().sendMessage("You can't blacklist a trusted user!").setEphemeral(true).queue();
            return;
        }

        if (event.getOption("operation").getAsString().equalsIgnoreCase("add")) {
            if (PermissionsManager.blacklist.contains(user)) {
                event.getHook().sendMessage("<@" + user + "> is already blacklisted!").setEphemeral(true).queue();
                return;
            }
            PermissionsManager.blacklist.add(user);

            try (FileWriter file = new FileWriter("blacklist.txt", true)) {
                file.write(user + "\n");
                Main.logger.info("Adding {} to the blacklist (Requested by trusted user {})", username, event.getUser().getName());
                event.getHook().sendMessage("Added <@" + user + "> to the blacklist!").queue();
            } catch (IOException e) {
                Main.logger.error("blacklist.txt malformed or not found!", e);
            }
        }

        if (event.getOption("operation").getAsString().equalsIgnoreCase("remove")) {
            if (!PermissionsManager.blacklist.contains(user)) {
                event.getHook().sendMessage("<@" + user + "> is not blacklisted!").setEphemeral(true).queue();
                return;
            }
            PermissionsManager.blacklist.remove(id);
        }
    }
}