package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Attachment;
import java.util.List;

public interface AttachmentRepository {
    public void createPostAttachment(Attachment attach);
    public void createReplyAttachment(Attachment attach);
    public List<Attachment> findAll();
    public void deleteByName(String name);
    public Attachment findByName(String name);
    public Attachment findByRid(int Rid);
    
    
}
