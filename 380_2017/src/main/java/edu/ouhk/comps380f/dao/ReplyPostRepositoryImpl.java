package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.ReplyPost;
import edu.ouhk.comps380f.model.Post;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyPostRepositoryImpl implements ReplyPostRepository {

    private DataSource dataSource;
    private JdbcOperations jdbcOp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcOp = new JdbcTemplate(this.dataSource);
    }

    private static final class ReplyPostRowMapper implements RowMapper<ReplyPost> {

        @Override
        public ReplyPost mapRow(ResultSet rs, int i) throws SQLException {
            ReplyPost replypost = new ReplyPost();
            replypost.setId(rs.getInt("id"));
            replypost.setRefPostid(rs.getInt("refId"));
            replypost.setReplybody(rs.getString("body"));
            return replypost;
        }
    }

//    private static final String SQL_INSERT_TICKET
//            = "insert into post (id,customername,subject,body,categories) values (1,'p','f','d','lab')";
        private static final String SQL_INSERT_REPLYTICKET
            = "insert into Reply (replyname,refid,body) values (?,?,?)";
   // private static final String SQL_INSERT_ROLE
     //       = "insert into user_roles (username, role) values (?, ?)";

    public void create(ReplyPost post) {
        jdbcOp.update(SQL_INSERT_REPLYTICKET,
                post.getReplyName(),
                post.getRefPostid(),
                post.getReplybody());   
    }


    private static final String SQL_SELECT_ALL_REPLYTICKET
            = "select id,replyname,refid,body from REPLY";
  //  private static final String SQL_SELECT_ROLES
    //        = "select username, role from user_roles where username = ?";

    @Override
    public List<ReplyPost> findAll() {
        List<ReplyPost> replyPosts = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_ALL_REPLYTICKET);
        
        for (Map<String, Object> row : rows) {
            ReplyPost replyPost = new ReplyPost();
            int id = (int)row.get("ID");
            replyPost.setId(id);       
            
            String replyname = (String)row.get("REPLYNAME");
            replyPost.setReplyName(replyname);
            replyPosts.add(replyPost);
            
            int refId = (int)row.get("REFID");
            replyPost.setRefPostid(refId);       
                             
            String replyBody = (String)row.get("BODY");
            replyPost.setReplybody(replyBody);
            replyPosts.add(replyPost);

        }
        return replyPosts;
    }
    private static final String SQL_SELECT_PART_REPLYTICKET = "select id,replyname, refId, body from reply WHERE refId = ?";
     @Override
    public List<ReplyPost> findParts(int postId) {
        List<ReplyPost> replyPosts = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcOp.queryForList(SQL_SELECT_PART_REPLYTICKET,postId);

        for (Map<String, Object> row : rows) {
            ReplyPost replyPost = new ReplyPost();
            
            int id = (int)row.get("id");
            replyPost.setId(id);       
            
            String replyname = (String)row.get("REPLYNAME");
            replyPost.setReplyName(replyname);
            
            
            int refId = (int)row.get("refId");
            replyPost.setRefPostid(refId);       
                             
            String replyBody = (String)row.get("body");
            replyPost.setReplybody(replyBody);
            
            replyPosts.add(replyPost);
        }
        return replyPosts;
    }
    
    private static final String SQL_SELECT_REPLYTICKET
            = "select * from REPLY where id = ?";

    @Override
    public ReplyPost findById(int id) {
        ReplyPost post = jdbcOp.queryForObject(SQL_SELECT_REPLYTICKET, new ReplyPostRowMapper(), id);
        return post;
    }
    
    
    private static final String SQL_SELECT_MAX
            = "select * from reply where id = (select MAX(id) from reply)";
    @Override
    public int maxId() {
        ReplyPost idobject = jdbcOp.queryForObject(SQL_SELECT_MAX, new ReplyPostRowMapper());
        int id=(int)idobject.getId();
        return id;
    }

    private static final String SQL_DELETE_TICKET
            = "delete from REPLY where id = ?";
    
    @Override
    public void deleteById(int id) {
       
        jdbcOp.update(SQL_DELETE_TICKET, id);
    }

}
