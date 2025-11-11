package com.bytespacegames.autogg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RegexManager {
    public static RegexManager INSTANCE;
    private final List<ServerConfig> servers = new ArrayList<>();
    public RegexManager() {
        INSTANCE = this;
        load();
    }
    public List<ServerConfig> getServers() {
        return servers;
    }
    public ServerConfig getConfigByIP(String ip) {
        for (ServerConfig config : servers) {
            if (config.ipMatch != null && ip.matches(config.ipMatch)) return config;
        }
        return null;
    }
    public void load() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("assets/autogg/regex.txt")) {
            if (stream == null) {
                System.out.println("regex.txt not found in resources/assets/autogg/");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            ServerConfig currentServer = null;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith(":")) {
                    // Start of a new server
                    String serverName = line.substring(1).trim();
                    currentServer = new ServerConfig("", ""); // placeholder, will set values later
                    servers.add(currentServer);
                } else if (line.startsWith("\"prefix\"")) {
                    if (currentServer != null) {
                        String prefix = line.split(":")[1].trim().replaceAll("^\"|\"$", "");
                        currentServer.messagePrefix = prefix;
                    }
                } else if (line.startsWith("\"criteria\"")) {
                    if (currentServer != null) {
                        String criteria = line.trim().substring(13,line.length()-1);
                        System.out.println("crt: " + criteria);
                        currentServer.ipMatch = criteria;
                    }
                } else if (line.startsWith("\"trigger\": ")) {
                    if (currentServer != null) {
                        String[] parts = line.split("\"trigger\": ", 2)[1].split(",", 2);
                        int type = Integer.parseInt(parts[0]);
                        String pattern = parts[1];
                        currentServer.addRegex(new Regex(type, pattern));
                    }
                }
            }

            //System.out.println("Loaded " + servers.size() + " servers with regex rules.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static class Regex {
        private int type;
        private String match;
        public Regex(int type, String match) {
            this.type = type;
            this.match = match;
        }
        public int getType() {
            return type;
        }
        public String getMatch() {
            return match;
        }
    }
    public static class ServerConfig {
        private String ipMatch;
        private String messagePrefix;
        private final List<Regex> regexes = new ArrayList<Regex>();
        public ServerConfig(String ipMatch, String messagePrefix) {
            this.ipMatch = ipMatch;
            this.messagePrefix = messagePrefix;
        }
        public void addRegex(Regex r) {
            regexes.add(r);
        }
        public List<Regex> getList() {
            return regexes;
        }

        public String getPrefix() {
            return messagePrefix;
        }
    }
}
