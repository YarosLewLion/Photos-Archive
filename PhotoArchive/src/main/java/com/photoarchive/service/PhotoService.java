package com.photoarchive.service;

import com.photoarchive.entity.Photo;
import com.photoarchive.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    public void savePhoto(MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setName(file.getOriginalFilename());
        photo.setData(file.getBytes());
        photoRepository.save(photo);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public void deletePhotos(List<Long> photoIds) {
        photoRepository.deleteAllById(photoIds);
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }
}
