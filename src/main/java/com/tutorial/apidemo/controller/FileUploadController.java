package com.tutorial.apidemo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.tutorial.apidemo.model.ResponseObject;
import com.tutorial.apidemo.services.IStorageService;

@Controller
@RequestMapping(path = "/api/v1/FileUpload")
public class FileUploadController {

    @Autowired
    private IStorageService storageService;
    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file){
        try {
            //save file to a foder => use a service
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Upload file successfully", generatedFileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed",e.getMessage() ,"")
            );
        }
    }

    @GetMapping("/file/{fileName:.+}")
    //ce31f55232f34f4e8e762135a202d961.png
    public ResponseEntity<byte[]> ReadDetailFile(@PathVariable String fileName){
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytes);
        } catch (Exception e) {
              return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadfile(){
        try {
            //convert fileName to url(send request "ReadDetailFile")
            List<String> urls = storageService.loadAll().map(path -> {
                String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "ReadDetailFile", path.getFileName().toString()).build().toUri().toString();
                return urlPath;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(new ResponseObject("ok", "Upload file successfully", urls));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseObject("failed", "Upload file successfully", ""));
        }
    }
}
