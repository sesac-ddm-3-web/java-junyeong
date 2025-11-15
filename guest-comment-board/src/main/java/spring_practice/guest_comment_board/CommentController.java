package spring_practice.guest_comment_board;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {
    List<Comment> commentList = new ArrayList<>();

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public List<Comment> getComments() {
        return commentList;
    };

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public String addComments(@RequestBody Comment comment) {
        commentList.add(comment);
        return "ok";
    };

}
