package xyz.funtimes909.serverseekerv2_discord_bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.funtimes909.serverseekerv2_discord_bot.Main;
import xyz.funtimes909.serverseekerv2_discord_bot.util.PermissionsCheck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Blacklist {
    public static void blacklist(SlashCommandInteractionEvent event) {
        String id = event.getUser().getId();
        String user = event.getOption("user").getAsString();
        String username = event.getOption("user").getAsUser().getName();

        if (!PermissionsCheck.ownerCheck(id) && !PermissionsCheck.trustedUsersCheck(id)) {
            event.reply("Sorry! You are not authorized to run this command!").setEphemeral(true).queue();
            return;
        }

        if (user.equals(Main.ownerId)) {
            event.reply("So you think you're smart huh?").queue();
            return;
        }

        if (event.getOption("operation").getAsString().equalsIgnoreCase("add")) {

            if (PermissionsCheck.blacklist.contains(user)) {
                event.reply("<@" + user + "> is already blacklisted!").setEphemeral(true).queue();
                return;
            }
            PermissionsCheck.blacklist.add(user);

            try (FileWriter file = new FileWriter("blacklist.txt", true)) {
                file.write(user + "\n");
                Main.logger.info("Adding {} to the blacklist (Requested by trusted user {})", username, event.getUser().getName());
                event.reply("Added <@" + user + "> to the blacklist!").setEphemeral(true).queue();
            } catch (IOException e) {
                Main.logger.error("blacklist.txt malformed or not found!", e);
            }
        }

        if (event.getOption("operation").getAsString().equalsIgnoreCase("remove")) {

            if (!PermissionsCheck.blacklist.contains(user)) {
                event.reply("<@" + user + "> is not blacklisted!").setEphemeral(true).queue();
                return;
            }
            PermissionsCheck.blacklist.remove(id);

            try {
                List<String> lines = Files.readAllLines(Paths.get("blacklist.txt"));
                for (String line : lines) {
                    System.out.println(line);
                    if (line.equals(user)) {
                        lines.remove(user);
                    }
                        System.out.println(line);
                }

                Files.write(Paths.get("blacklist.txt"), lines);

                Main.logger.info("Removing {} from the blacklist (Requested by trusted user {})", username, event.getUser().getName());
                event.reply("Removed <@" + user + "> from the blacklist!").setEphemeral(true).queue();
            } catch (IOException e) {
                Main.logger.error("blacklist.txt malformed or not found!", e);
            }
        }
    }
}