package fr.liste_wanted.ui.events;

public class Event {

    private String name;
    private long startTime;
    private long endTime;
    private String place;
    private String description;

    public Event(String name, long startTime, long endTime, String place, String description) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getPlace() {
        return place;
    }

    public String getDescription() {
        return description;
    }
}
