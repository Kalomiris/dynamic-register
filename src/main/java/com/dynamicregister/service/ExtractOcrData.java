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
        tess.setLanguage("ell"); // choose your language

        File temp = File.createTempFile("tempfile_", ".png");
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(file.getBytes())), "png", temp);

        String result = tess.doOCR(temp);
        out.append(result);

        // Delete temp file
        temp.delete();

        return out.toString();

    }
}
