package edu.ouhk.comps380f.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReplyPost {

    private int id;
    private int refPostid;
    private String replyName;
    private String replybody;
    private Map<String, Attachment> attachments = new LinkedHashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public int getRefPostid() {
        return refPostid;
    }

    public void setRefPostid(int refPostid) {
        this.refPostid = refPostid;
    }

    public String getReplybody() {
        return replybody;
    }

    public void setReplybody(String replybody) {
        this.replybody = replybody;
    }

    public Attachment getAttachment(String name) {
        return this.attachments.get(name);
    }

    public Collection<Attachment> getAttachments() {
        return this.attachments.values();
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments() {
        return this.attachments.size();
    }

    public boolean hasAttachment(String name) {
        return this.attachments.containsKey(name);
    }

    public Attachment deleteAttachment(String name) {
        return this.attachments.remove(name);
    }

}
