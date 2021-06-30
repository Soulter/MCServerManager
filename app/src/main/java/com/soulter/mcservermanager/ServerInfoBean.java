package com.soulter.mcservermanager;

public class ServerInfoBean {

    public String key;
    public String serverIP;
    public int port;
    public String serverName;
    public String faviconDb64;


    public ServerInfoBean(String key, String serverIP, int port, String serverName, String faviconDb64){
        this.key = key;
        this.serverIP = serverIP;
        this.port = port;
        this.serverName = serverName;
        this.faviconDb64 = faviconDb64;

    }

    public int getPort() {
        return port;
    }

    public String getFaviconDb64() {
        return faviconDb64;
    }

    public String getKey() {
        return key;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getServerName() {
        return serverName;
    }
}
