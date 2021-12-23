package com.Eyannee.demons.Controller;

import com.Eyannee.demons.entity.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Controller
@RequestMapping("/jdbc")
public class DataBaseController {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/user")
    @ResponseBody
    public List<Book> list(ModelMap map) {
        String sql = "SELECT * FROM book";
        List<Book> userList = jdbcTemplate.query(sql, new RowMapper<Book>() {
            Book MyBook = null;
            @Override
            public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyBook = new Book();
                MyBook.setBno(rs.getString("Bno"));
                MyBook.setCategory(rs.getString("Category"));
                MyBook.setTitle(rs.getString("Title"));
                MyBook.setPress(rs.getString("Press"));
                MyBook.setYear(rs.getInt("year"));
                MyBook.setAuthor(rs.getString("author"));
                MyBook.setPrice(rs.getFloat("price"));
                MyBook.setTotal(rs.getInt("total"));
                MyBook.setStock(rs.getInt("stock"));
                return MyBook;
            }
        });
        for (Book user : userList) {

            System.out.println(user.toString());
        }
        return userList;

    }

    @RequestMapping("/userList")
    public String userList(ModelMap map) {
        String sql = "SELECT * FROM book";
        List<Book> userList = jdbcTemplate.query(sql, new RowMapper<Book>() {
            Book MyBook = null;
            @Override
            public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                MyBook = new Book();
                MyBook.setBno(rs.getString("Bno"));
                MyBook.setCategory(rs.getString("Category"));
                MyBook.setTitle(rs.getString("Title"));
                MyBook.setPress(rs.getString("Press"));
                MyBook.setYear(rs.getInt("year"));
                MyBook.setAuthor(rs.getString("author"));
                MyBook.setPrice(rs.getFloat("price"));
                MyBook.setTotal(rs.getInt("total"));
                MyBook.setStock(rs.getInt("stock"));
                return MyBook;
            }
        });
        map.addAttribute("users", userList);
        return "demons";
    }
}
