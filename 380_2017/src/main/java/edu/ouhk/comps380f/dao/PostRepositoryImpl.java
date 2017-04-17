package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Post;
import edu.ouhk.comps380f.model.PostUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private DataSource dataSource;
    private JdbcOperations jdbcOp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcOp = new JdbcTemplate(this.dataSource);
    }

    private static final class PostRowMapper implements RowMapper<Post> {

        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            Post post = new Post();
            post.setId(rs.getInt("id"));
            post.setCustomerName(rs.getString("customername"));
            post.setSubject(rs.getString("subject"));
            post.setBody(rs.getString("body"));
            post.setCategories(rs.getString("categories"));
            return post;
        }
    }
    

//    private static final String SQL_INSERT_TICKET
//            = "insert into post (id,customername,subject,body,categories) values (1,'p','f','d','lab')";
        private static final String SQL_INSERT_TICKET
            = "insert into post (customername,subject,body,categories) values (?,?,?,?)";
   // private static final String SQL_INSERT_ROLE
     //       = "insert into user_roles (username, role) values (?, ?)";

    public void create(Post post) {
        jdbcOp.update(SQL_INSERT_TICKET,
                post.getCustomerName(),
                post.getSubject(),               
                post.getBody(),
                post.getCategories());
//        for (String role : user.getRoles()) {
//            jdbcOp.update(SQL_INSERT_ROLE,
//                    user.getUsername(),
//                    role);             
    }


    private static final String SQL_SELECT_ALL_TICKET
            = "select id, customername, subject, body, categories from post";
  //  private static final String SQL_SELECT_ROLES
    //        = "select username, role from user_roles where username = ?";

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ALL_TICKET);

        for (Map<String, Object> row : rows) {
            Post post = new Post();
            
            int id = (int)row.get("id");
            post.setId(id);       
            
            String customername = (String)row.get("customername");
            post.setCustomerName(customername);  
            
            String subject = (String)row.get("subject");
            post.setSubject(subject);
            
            String body = (String)row.get("body");
            post.setBody(body);
            
            String categories = (String)row.get("categories");
            post.setCategories(categories);
            
            posts.add(post);
        }
        return posts;
    }
    private static final String SQL_SELECT_TICKET
            = "select * from post where id = ?";

    @Override
    public Post findById(int id){
      try{
      Post post = jdbcOp.queryForObject(SQL_SELECT_TICKET, new PostRowMapper(), id);
          return post;
      }catch(EmptyResultDataAccessException e) {
          return null;
      }
    }
    
    private static final String SQL_SELECT_MAX
            = "select * from post where id = (select MAX(id) from post)";
    @Override
    public int maxId() {
        Post idobject = jdbcOp.queryForObject(SQL_SELECT_MAX, new PostRowMapper());
        int id=(int)idobject.getId();
        return id;
    }
    private static final String SQL_SELECT_LAB
            = "select * from post where categories = 'lab'";
    private static final String SQL_SELECT_LECTURE
            = "select * from post where categories = 'lecture'";
    private static final String SQL_SELECT_OTHER
            = "select * from post where categories = 'other'";
    
    @Override
    public List<Post> findByCategories(String type) {
        List<Map<String, Object>> rows=null;
        List<Post> posts2 = new ArrayList<>();
        if(type=="lab"){
          rows = jdbcOp.queryForList(SQL_SELECT_LAB);
        }
        if(type=="lecture"){
          rows = jdbcOp.queryForList(SQL_SELECT_LECTURE);
        }
        if(type=="other"){
          rows = jdbcOp.queryForList(SQL_SELECT_OTHER);
        }
        
        for (Map<String, Object> row : rows) {
            Post post = new Post();
            
            int id = (int)row.get("id");
            post.setId(id);       
            
            String customername = (String)row.get("customername");
            post.setCustomerName(customername);  
            
            String subject = (String)row.get("subject");
            post.setSubject(subject);
            
            String body = (String)row.get("body");
            post.setBody(body);
            
            String categories = (String)row.get("categories");
            post.setCategories(categories);
            
            posts2.add(post);
        }
        return posts2;
    }
//    private static final String SQL_DELETE_USER
//            = "delete from users where username = ?";
//    private static final String SQL_DELETE_ROLES
//            = "delete from user_roles where username = ?";
//
//    @Override
//    public void deleteByUsername(String username) {
//        jdbcOp.update(SQL_DELETE_ROLES, username);
//        jdbcOp.update(SQL_DELETE_USER, username);
//    }

    private static final String SQL_DELETE_TICKET
            = "delete  from post where id = ?";

    @Override
    public void deleteById(int id) {
        jdbcOp.update(SQL_DELETE_TICKET, id);
        
    }
    
    private static final String SQL_UPDATE_TICKET
            = "update post set subject = ?, body = ? where id = ?";
    
    @Override
    public void update(Post post) {
        jdbcOp.update(SQL_UPDATE_TICKET, post.getSubject(), post.getBody(), post.getId());
    }

}
