package com.tutorial.apidemo.services;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
@Service  //de java spring hieu day la thang service
public class imageStorageService implements IStorageService {
    

    private final Path storageFolder = Paths.get("uploads");

    //constructor
    public imageStorageService() {
        try{
            Files.createDirectories(storageFolder);
        } catch (IOException e){
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }

    
    private boolean isImageFile(MultipartFile file){
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png", "jpg", "jpeg", "gif"})
            .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("RUN SERVICE");
            if(file.isEmpty()){                
                throw new RuntimeException("Failed to store empty file!");
            }
            if (!isImageFile(file)){
                throw new RuntimeException("You can only upload image file!");
            }
            float fileSize = file.getSize() / 1_000_000;
            if(fileSize > 0.5f){
                throw new RuntimeException("File must be <= 5MB!!!");
            }
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationPath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if(!destinationPath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside current directory!");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING); 
            }
            return generatedFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store empty file!", e);
        }
    }



    @Override
    public void deleteAllFile() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            //List all files in storageFolder
            return Files.walk(this.storageFolder, 1).filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._")).map(this.storageFolder::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load stored files!", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        // TODO Auto-generated method stub
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException(
                    "Could not read file: " + fileName
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

}
