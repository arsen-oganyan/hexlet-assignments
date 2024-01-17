package exercise.controller.users;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api/users")
public class PostsController {

    private List<Post> posts = Data.getPosts();

    @GetMapping("/{id}/posts")
    public List<Post> index(@PathVariable int id, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {

        var postData = posts.stream()
                .filter(p -> p.getUserId() == id)
                .toList();
        return postData;
    }

    @PostMapping("/{id}/posts") // Создание страницы
    public ResponseEntity<Post> create(@PathVariable int id, @RequestBody Post post) {

        var in_post = post;
        in_post.setUserId(id);
        in_post.setSlug(post.getSlug());
        in_post.setTitle(post.getTitle());
        in_post.setBody(post.getBody());

        posts.add(in_post);
        return ResponseEntity.status(201).body(post);
    }
}
// END
