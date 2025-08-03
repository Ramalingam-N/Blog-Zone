package com.blog.controller;

import com.blog.entity.*;
import com.blog.security.SecurityUtils;
import com.blog.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final CommentService commentService;
    private final SavedService savedService;
    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;

    public PostController(PostService postService,
                          UserService userService,
                          FileStorageService fileStorageService,
                          CommentService commentService,
                          SavedService savedService,
                          PostLikeService postLikeService,
                          CommentLikeService commentLikeService){
        this.postService = postService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.commentService = commentService;
        this.savedService = savedService;
        this.postLikeService = postLikeService;
        this.commentLikeService = commentLikeService;
    }
    @GetMapping("/post/{url}")
    public String postView(@PathVariable String url, Model model){
        String email = SecurityUtils.getCurrentUser().getUsername();
        Post post = postService.findByUrl(url);
        User user = userService.findByEmail(email);
        Comment commentEntity = new Comment();
        if(post.getUser().getEmail().equals(user.getEmail())) {
            model.addAttribute("follow", "none");
        } else {
            boolean flag = false;
            for(User u : user.getFollowing()) {
                if(u.getEmail().equals(post.getUser().getEmail())) {
                    model.addAttribute("follow", "Following");
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                model.addAttribute("follow", "Follow");
            }
        }


        Set<Comment> comments = commentService.findCommentsByPostId(post.getId());
        String formatDate = postService.formatDate(post.getUpdatedOn());
        for(Comment comment : comments){
            comment.setFormattedDate(postService.formatDate(comment.getCreatedOn()));
        }
        if(comments.size() == 0){
            model.addAttribute("check", "No Comments Yet...");
        }
        else {
            model.addAttribute("check", "");
            model.addAttribute("comments", comments);
        }

        if(savedService.isSavedExists(user, post))
            model.addAttribute("savedImg", "/img/saved.png");
        else
            model.addAttribute("savedImg", "/img/save.png");

        if(postLikeService.isLikeExists(user, post))
            model.addAttribute("likeImg", "/img/liked.png");
        else
            model.addAttribute("likeImg", "/img/like.png");

        List<String> list = new ArrayList<>();
        for (Comment comment : comments) {
            if (commentLikeService.isCommentLikeExists(user, comment))
                list.add("/img/liked.png");
            else
                list.add("/img/like.png");
        }

        List<Integer> delete = new ArrayList<>();
        for (Comment c : comments){
            if(c.getUser().getEmail().equals(SecurityUtils.getCurrentUser().getUsername())) {
                delete.add(17);
            }
            else {
                delete.add(0);
            }
        }
        model.addAttribute("height", delete);

        model.addAttribute("commentLikeImg", list);
        model.addAttribute("postCreated", formatDate);
        model.addAttribute("post", post);
        model.addAttribute("user", user);
        model.addAttribute("commentEntity", commentEntity);

        return "post";
    }

    @PostMapping("/post-comment")
    public String uploadComment(@ModelAttribute("commentEntity") Comment comment,
                                @RequestParam("url") String url,
                                Model model) throws InterruptedException {
        Post post = postService.findByUrl(url);
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        comment.setUser(user);
        comment.setPost(post);
        commentService.saveComment(comment);
        Set<Comment> comments = commentService.findCommentsByPostId(post.getId());
        for(Comment c : comments){
            c.setFormattedDate(postService.formatDate(c.getCreatedOn()));
        }
        model.addAttribute("comments", comments);
        model.addAttribute("check", false);
        model.addAttribute("count", post.getComments().size());
        List<String> list = new ArrayList<>();
        for (Comment c : comments) {
            if (commentLikeService.isCommentLikeExists(user, c))
                list.add("/img/liked.png");
            else {
                list.add("/img/like.png");
            }
        }
        model.addAttribute("commentLikeImg", list);

        List<Integer> delete = new ArrayList<>();
        for (Comment c : comments){
            if(Objects.equals(c.getUser().getEmail(), SecurityUtils.getCurrentUser().getUsername())) {
                delete.add(17);
            }
            else {
                delete.add(0);
            }
        }
        model.addAttribute("height", delete);

        Thread.sleep(1000);
        return "fragments/comments :: commentList";
    }

    @PostMapping("/delete-comment/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, Model model) throws InterruptedException {
        Post post = postService.findByCommentId(commentId);
        commentService.deleteById(commentId);
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        Set<Comment> comments = commentService.findCommentsByPostId(post.getId());
        for(Comment c : comments){
            c.setFormattedDate(postService.formatDate(c.getCreatedOn()));
        }
        model.addAttribute("comments", comments);
        if(comments.isEmpty())
            model.addAttribute("check", "No Comments Yet...");
        model.addAttribute("count", post.getComments().size());
        List<String> list = new ArrayList<>();
        for (Comment c : comments) {
            if (commentLikeService.isCommentLikeExists(user, c))
                list.add("/img/liked.png");
            else {
                list.add("/img/like.png");
            }
        }
        model.addAttribute("commentLikeImg", list);

        List<Integer> delete = new ArrayList<>();
        for (Comment c : comments){
            if(Objects.equals(c.getUser().getEmail(), SecurityUtils.getCurrentUser().getUsername())) {
                delete.add(17);
            }
            else {
                delete.add(0);
            }
        }
        model.addAttribute("height", delete);

        Thread.sleep(1000);
        return "fragments/comments :: commentList";
    }

    @GetMapping("/write")
    public String writePostPage(Model model){
        Post post = new Post();
        model.addAttribute("post", post);
        return "write-post";
    }
    @PostMapping("/write")
    public String writePostUpload(@ModelAttribute("post") Post post, @RequestParam("image1") MultipartFile imageFile) throws IOException, InterruptedException {
        String url = postService.generateUrl();
        post.setUrl(url);
        String imagePath = fileStorageService.storeFile(imageFile, url);
        post.setImage(postService.formatImagePath(imagePath));
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        post.setUser(user);
        postService.savePost(post);
        Thread.sleep(5000);
        return "redirect:/post/"+url;
    }

    @PostMapping("/save-post/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> savePost(@PathVariable("postId") Long postId){
        Map<String, Object> response = new HashMap<>();
        Saved saved = new Saved();
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        Post post = postService.findPostById(postId);
        saved.setPost(post);
        saved.setUser(user);
        savedService.save(saved);
        response.put("savedCount", postService.countSaved(post));
        if(savedService.isSavedExists(user, post))
            response.put("savedImgSrc", "saved");
        else
            response.put("savedImgSrc", "save");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like-post/{postId}")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable("postId") Long postId){
        Map<String, Object> response = new HashMap<>();
        PostLike postLike = new PostLike();
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        Post post = postService.findPostById(postId);
        postLike.setPost(post);
        postLike.setUser(user);
        postLikeService.save(postLike);
        response.put("likeCount", postService.countLike(post));
        if(postLikeService.isLikeExists(user, post))
            response.put("likeImgSrc", "liked");
        else
            response.put("likeImgSrc", "like");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like-comment/{commentId}")
    public ResponseEntity<Map<String, Object>> likeComment(@PathVariable("commentId") Long commentId){
        Map<String, Object> response = new HashMap<>();
        CommentLike commentLike = new CommentLike();
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        Comment comment = commentService.findCommentById(commentId);
        commentLike.setComment(comment);
        commentLike.setUser(user);
        commentLikeService.save(commentLike);
        response.put("commentLikeCount", commentService.countCommentLike(comment));
        if(commentLikeService.isCommentLikeExists(user, comment))
            response.put("commentLikeImgSrc", "liked");
        else
            response.put("commentLikeImgSrc", "like");
        return ResponseEntity.ok(response);
    }



    @GetMapping("/my-posts/{userName}")
    public String viewMyPosts(@PathVariable("userName") String userName, Model model){
        List<Post> myPosts = postService.findAllPostsByUserName(userName);
        model.addAttribute("posts", myPosts);
        List<String> formatDate = new ArrayList<>();
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        for(Post post: myPosts) {
            formatDate.add(postService.formatDate(post.getUpdatedOn()));
            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");

        }
        model.addAttribute("isLiked",isLiked);
        model.addAttribute("isSaved",isSaved);
        model.addAttribute("postDate", formatDate);
        return "my-post";
    }

    @GetMapping("/saved-posts/{userName}")
    public String savedPosts(@PathVariable("userName") String userName, Model model){
        List<Post> savedPosts = postService.findAllSavedPostByUserName(userName);
        model.addAttribute("posts", savedPosts);
        List<String> formatDate = new ArrayList<>();
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        for(Post post: savedPosts) {
            formatDate.add(postService.formatDate(post.getUpdatedOn()));
            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");

        }
        model.addAttribute("isLiked",isLiked);
        model.addAttribute("isSaved",isSaved);
        model.addAttribute("postDate", formatDate);
        return "saved-post";
    }

    @GetMapping("/view-posts/{userName}")
    public String viewPosts(@PathVariable("userName") String userName, Model model){
        List<Post> myPosts = postService.findAllPostsByUserName(userName);
        model.addAttribute("posts", myPosts);
        List<String> formatDate = new ArrayList<>();
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        for(Post post: myPosts) {
            formatDate.add(postService.formatDate(post.getUpdatedOn()));
            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");

        }
        model.addAttribute("isLiked",isLiked);
        model.addAttribute("isSaved",isSaved);
        model.addAttribute("postDate", formatDate);
        return "view-posts";
    }

    @GetMapping("/edit-post/{postId}")
    public String editPost(@PathVariable("postId") Long id, Model model){
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/edit/{id}")
    public String saveEditedPost(@PathVariable("id") Long id, @ModelAttribute("post") Post post, @RequestParam("image1") MultipartFile imageFile) throws IOException, InterruptedException {
        Post existingPost = postService.findPostById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setDescription(post.getDescription());
        existingPost.setContent(post.getContent());
        existingPost.setUrl(post.getUrl());
        Thread.sleep(7000);
        String imagePath = fileStorageService.storeFile(imageFile, post.getUrl());
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        existingPost.setUser(user);
        postService.savePost(existingPost);
        return "redirect:/post/" + existingPost.getUrl();
    }

    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable("id") Long id){
        fileStorageService.deleteImage(postService.findPostById(id).getImage());
        postService.deleteById(id);
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        return "redirect:/my-posts/"+user.getUserName();
    }
    @GetMapping("")
    public String homePage(Model model){
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<Post> allPosts = postService.allPosts();
        model.addAttribute("posts", allPosts);
        List<String> formatDates = new ArrayList<>();
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        List<String> isFollowing = new ArrayList<>();
        for(Post post : allPosts){
            formatDates.add(postService.formatDate(post.getCreatedOn()));

            if(post.getUser().getEmail().equals(email)){
                isFollowing.add("none");
            }
            else{
                if(post.getUser().getFollowers().contains(user))
                    isFollowing.add("Following");
                else
                    isFollowing.add("Follow");
            }

            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");

        }
        model.addAttribute("formatDate", formatDates);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isSaved", isSaved);
        model.addAttribute("isFollowing", isFollowing);

        return "home";
    }

    @PostMapping("/forYou-posts")
    public String forYouPosts(Model model){
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<Post> allPosts = postService.allPosts();
        model.addAttribute("posts", allPosts);
        List<String> formatDates = new ArrayList<>();
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        List<String> isFollowing = new ArrayList<>();
        for(Post post : allPosts){
            formatDates.add(postService.formatDate(post.getCreatedOn()));

            if(post.getUser().getEmail().equals(email)){
                isFollowing.add("none");
            }
            else{
                if(post.getUser().getFollowers().contains(user))
                    isFollowing.add("Following");
                else
                    isFollowing.add("Follow");
            }

            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");

        }
        model.addAttribute("formatDate", formatDates);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isSaved", isSaved);
        model.addAttribute("isFollowing", isFollowing);
        return "fragments/home-posts :: homePosts";
    }

    @PostMapping("/following-posts")
    public String followingPosts(Model model){
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<Post> allPosts = postService.allPostsByFollowingUser(user.getFollowing());
        model.addAttribute("posts", allPosts);
        List<String> formatDates = new ArrayList<>();
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        List<String> isFollowing = new ArrayList<>();
        for(Post post : allPosts){
            formatDates.add(postService.formatDate(post.getCreatedOn()));

            if(post.getUser().getEmail().equals(email)){
                isFollowing.add("none");
            }
            else{
                if(post.getUser().getFollowers().contains(user))
                    isFollowing.add("Following");
                else
                    isFollowing.add("Follow");
            }

            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");

        }
        model.addAttribute("formatDate", formatDates);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isSaved", isSaved);
        model.addAttribute("isFollowing", isFollowing);
        return "fragments/home-posts :: homePosts";
    }

    @PostMapping("/searchPost")
    public String searchPost(@RequestParam("search") String search, Model model) throws Exception {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<Post> tempAllPosts;
        String name = "";
        if(search.charAt(0) == '2'){
            name = search.substring(1).toLowerCase();
            tempAllPosts = postService.allPostsByFollowingUser(user.getFollowing());
        }
        else{
            name = search.substring(1).toLowerCase();
            tempAllPosts = postService.allPosts();
        }
        List<Post> allPosts = new ArrayList<>();
        for(Post p : tempAllPosts){
            if(p.getTitle().toLowerCase().contains(name))
                allPosts.add(p);
        }

        model.addAttribute("posts", allPosts);
        List<String> formatDates = new ArrayList<>();
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        List<String> isFollowing = new ArrayList<>();
        for(Post post : allPosts){
            formatDates.add(postService.formatDate(post.getCreatedOn()));

            if(post.getUser().getEmail().equals(email)){
                isFollowing.add("none");
            }
            else{
                if(post.getUser().getFollowers().contains(user))
                    isFollowing.add("Following");
                else
                    isFollowing.add("Follow");
            }

            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");
        }
        model.addAttribute("formatDate", formatDates);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isSaved", isSaved);
        model.addAttribute("isFollowing", isFollowing);
        return "fragments/home-posts :: homePosts";
    }

    @PostMapping("/saved-search-form")
    public String savedSearchForm(@RequestParam("search") String search, Model model) throws Exception {
        System.out.println("Search: ------------------------------------ "+search);
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        List<Post> tempAllPosts = postService.findAllSavedPostByUserName(user.getUserName());
        List<Post> allPosts = new ArrayList<>();
        for(Post p : tempAllPosts){
            if(p.getTitle().toLowerCase().contains(search.toLowerCase()))
                allPosts.add(p);
        }

        System.out.println(allPosts.toString());
        model.addAttribute("posts", allPosts);
        List<String> formatDates = new ArrayList<>();
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        for(Post post : allPosts){
            formatDates.add(postService.formatDate(post.getCreatedOn()));

            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");
        }
        model.addAttribute("postDate", formatDates);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isSaved", isSaved);
        return "fragments/saved-posts-fragment :: savedPosts";
    }

    @PostMapping("/myPosts-search-form")
    public String myPostsSearchForm(@RequestParam("search") String search, Model model) throws Exception {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        Set<Post> tempAllPosts = userService.findByEmail(email).getMyPosts();
        List<Post> allPosts = new ArrayList<>();
        for(Post p : tempAllPosts){
            if(p.getTitle().toLowerCase().contains(search.toLowerCase()))
                allPosts.add(p);
        }

        System.out.println(allPosts.toString());
        model.addAttribute("posts", allPosts);
        List<String> formatDates = new ArrayList<>();
        List<String> isLiked = new ArrayList<>();
        List<String> isSaved = new ArrayList<>();
        for(Post post : allPosts){
            formatDates.add(postService.formatDate(post.getCreatedOn()));

            if(postLikeService.isLikeExists(user, post))
                isLiked.add("/img/liked.png");
            else
                isLiked.add("/img/like.png");

            if(savedService.isSavedExists(user, post))
                isSaved.add("/img/saved.png");
            else
                isSaved.add("/img/save.png");
        }
        model.addAttribute("postDate", formatDates);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("isSaved", isSaved);
        return "fragments/myposts-posts-fragments :: myPosts";
    }
}
