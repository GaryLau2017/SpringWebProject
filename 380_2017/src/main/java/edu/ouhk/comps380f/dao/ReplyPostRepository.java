package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.ReplyPost;
import java.util.List;

public interface ReplyPostRepository {
    public void create(ReplyPost post);
    public List<ReplyPost> findAll();
    public List<ReplyPost> findParts(int postId);
    public ReplyPost findById(int id);
    public int maxId();
    public void deleteById(int id );
}
