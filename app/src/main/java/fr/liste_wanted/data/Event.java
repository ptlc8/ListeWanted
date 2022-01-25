package fr.liste_wanted.data;

public class Event {

    private int id;
    private String name;
    private long startTime;
    private long endTime;
    private String place;
    private String description;

    public Event(int id, String name, long startTime, long endTime, String place, String description) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.description = description;
    }

    public int getId() {
        return id;
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
