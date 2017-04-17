package edu.ouhk.comps380f.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {

    private long id;
    private String pollSubject;
    private Map<String, Integer> map = new HashMap<String, Integer>();
    private List<String> userPolled = new ArrayList<>();

    public List<String> getUserPolled() {
        return userPolled;
    }

    public String getPollSubject() {
        return pollSubject;
    }

    public void setPollSubject(String pollSubject) {
        this.pollSubject = pollSubject;
    }

    public void setUserPolled(List<String> userPolled) {
        this.userPolled = userPolled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

}
