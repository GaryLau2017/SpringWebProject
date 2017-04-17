package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.PostUser;
import java.util.List;

public interface PostUserRepository {
    public void create(PostUser user);
    public List<PostUser> findAll();
    public PostUser findByUsername(String username);
    public void editByUsername(String username, String role);
    public void deleteByUsername(String username);
}
