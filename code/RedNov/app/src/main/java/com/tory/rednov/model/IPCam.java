package com.tory.rednov.model;

public class IPCam {
    private User user;
    private IPSettings ipSettings;

    public void setUser(User user) {
        this.user.name = user.name;
        this.user.password = user.password;
    }

    public User getUser() {
        return this.user;
    }

    public void setIpSettings (IPSettings ipSettings) {
        this.ipSettings.port = ipSettings.port;
        this.ipSettings.ip = ipSettings.ip;
    }

    public IPSettings getIpSettings() {
        return this.ipSettings;
    }

    //User
    class User {
        private String name;
        private String password;

        public User(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    //IPSettings
    class IPSettings {
        private String ip;
        private int port;

        public IPSettings(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }



}
