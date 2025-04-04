package com.photoarchive.controller;

import com.photoarchive.entity.Photo;
import com.photoarchive.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            photoService.savePhoto(file);
        }
        return "redirect:/";
    }

    @GetMapping("/list")
    public String listPhotos(Model model) {
        List<Photo> photos = photoService.getAllPhotos();
        model.addAttribute("photos", photos);
        return "list";
    }

    @PostMapping("/delete")
    public String deletePhotos(@RequestParam("photoIds") List<Long> photoIds) {
        if (photoIds != null && !photoIds.isEmpty()) {
            photoService.deletePhotos(photoIds);
        }
        return "redirect:/list";
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadPhotos() throws Exception {
        List<Photo> photos = photoService.getAllPhotos();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Photo photo : photos) {
                ZipEntry entry = new ZipEntry(photo.getName());
                entry.setSize(photo.getData().length);
                zos.putNextEntry(entry);
                zos.write(photo.getData());
                zos.closeEntry();
            }
        }

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=photos.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(baos.size())
                .body(resource);
    }
}
