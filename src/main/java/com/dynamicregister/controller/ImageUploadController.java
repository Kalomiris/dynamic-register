package com.dynamicregister.controller;

import com.dynamicregister.service.ExtractOcrData;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadController.class);

    private final ExtractOcrData extractOcrData;

    public ImageUploadController(ExtractOcrData extractOcrData) {
        this.extractOcrData = extractOcrData;
    }

    @PostMapping("/upload")
    public ResponseEntity.BodyBuilder uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String strippedText = null;
        try {
            strippedText = extractOcrData.extractTextFromScannedDocument(file);
            LOGGER.info(strippedText);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        LOGGER.info(strippedText);

        return ResponseEntity.status(HttpStatus.OK);
    }

}
