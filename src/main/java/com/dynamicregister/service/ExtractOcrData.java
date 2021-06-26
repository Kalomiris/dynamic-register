package com.dynamicregister.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ExtractOcrData {

    public String extractTextFromScannedDocument(MultipartFile file) throws IOException, TesseractException {

        StringBuilder out = new StringBuilder();

        ITesseract tess = new Tesseract();
        tess.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        tess.setLanguage("eng"); // choose your language

        File temp = File.createTempFile("tempfile_", ".png");
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(file.getBytes())), "png", temp);

        String result = tess.doOCR(temp);
        String firstname = result.split("\n")[2].replace(" ", "");
        String lastname = result.split("\n")[4].replace(" ", "");;
        out.append(firstname.toUpperCase()).append("\n").append(lastname.toUpperCase());

        // Delete temp file
        temp.delete();

        return out.toString();

    }
}
