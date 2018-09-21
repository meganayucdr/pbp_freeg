package com.mtz.testwarna;

public class Username {
    private String name, username, password, email, location, location_id, photo_uri;

    public Username() {
    }

    public Username(String name, String username, String password, String email, String location, String location_id, String photo_uri) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.location_id = location_id;
        this.photo_uri = photo_uri;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
