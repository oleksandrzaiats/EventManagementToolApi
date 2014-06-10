package com.zayats.model;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable, Comparable<Event> {

    private int id;
    private String name;
    private String description;
    private Date date;
    private String address;
    private User owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User ownerId) {
        this.owner = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (!address.equals(event.address)) return false;
        if (!date.equals(event.date)) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (!name.equals(event.name)) return false;
        if (!owner.equals(event.owner)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + date.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + owner.hashCode();
        return result;
    }

    @Override
    public int compareTo(Event o) {
        long diff = this.getDate().getTime() - o.getDate().getTime();
        if(diff > 0) {
            return 1;
        } else if(diff == 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
