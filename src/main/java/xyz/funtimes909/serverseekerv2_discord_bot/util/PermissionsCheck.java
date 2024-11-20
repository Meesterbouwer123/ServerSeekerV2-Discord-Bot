package xyz.funtimes909.serverseekerv2_discord_bot.util;

import xyz.funtimes909.serverseekerv2_discord_bot.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PermissionsCheck {
    public static boolean blacklistCheck(String userId) {
        try (BufferedReader blacklist = new BufferedReader(new FileReader("blacklist.txt"))) {
            String line;
            while ((line = blacklist.readLine()) != null) {
                if (userId.equals(Main.ownerId)) return false;
                if (userId.equals(line)) return true;
            }
        } catch (IOException e) {
            Main.logger.error("Failed to read blacklist.txt");
        }
        return false;
    }

    public static boolean trustedUsersCheck(String userId) {
        try (BufferedReader trustedUsers = new BufferedReader(new FileReader("trusted_users.txt"))) {
            String line;
            while ((line = trustedUsers.readLine()) != null) {
                if (userId.equals(line)) return true;
            }
        } catch (IOException e) {
            Main.logger.error("Failed to read trusted_users.txt");
        }
        return false;
    }

    public static boolean ownerCheck(String userId) {
        return Main.ownerId.equals(userId);
    }
}