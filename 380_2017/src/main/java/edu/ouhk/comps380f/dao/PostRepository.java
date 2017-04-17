package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Post;
import java.util.List;

public interface PostRepository {
    public void create(Post post);
    public List<Post> findAll();
    public Post findById(int id);
    public int maxId();
    public List<Post> findByCategories(String type);
    public void deleteById(int id);
    public void update(Post post);

}

