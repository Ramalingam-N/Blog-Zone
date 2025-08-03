package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.entity.Saved;
import com.blog.entity.User;
import com.blog.repository.SavedRepository;
import com.blog.service.SavedService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SavedServiceImpl implements SavedService {
    SavedRepository savedRepository;
    public SavedServiceImpl(SavedRepository savedRepository){
        this.savedRepository = savedRepository;
    }
    @Override
    public void save(Saved saved) {
        Optional<Saved> existingSaved = savedRepository.findByUserIdAndPostId(saved.getUser().getId(), saved.getPost().getId());
        if(existingSaved.isEmpty())
            savedRepository.save(saved);
        else
            savedRepository.delete(existingSaved.get());
    }

    @Override
    public boolean isSavedExists(User user, Post post) {
        return !savedRepository.findByUserIdAndPostId(user.getId(), post.getId()).isEmpty();
    }
}
