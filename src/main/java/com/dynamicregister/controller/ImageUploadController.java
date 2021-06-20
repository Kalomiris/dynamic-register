package com.dynamicregister.controller;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadController.class);

    @PostMapping("/upload")
    public ResponseEntity.BodyBuilder uploadImage(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        String strippedText = null;
        try {
//            FileUtils.writeByteArrayToFile(new File("/home/giannis/IdeaProjects/DynamicRegister/src/main/resources/data.png"), convertToByteArray(imageData));
//            File image = new File("/home/giannis/IdeaProjects/DynamicRegister/src/main/resources/data.png");
//            Tesseract tesseract = new Tesseract();
//            tesseract.setOcrEngineMode(1);
//            tesseract.setLanguage("ell");
//            tesseract.setDatapath("/usr/local/share/tessdata");
//            result = tesseract.doOCR(image);
//            image.delete();

            // Load file into PDFBox class
            PDDocument document = Loader.loadPDF(file.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            strippedText = stripper.getText(document);

            // Check text exists into the file
            if (strippedText.trim().isEmpty()){
                strippedText = extractTextFromScannedDocument(document);
            }

        } catch (TesseractException e) {
            e.printStackTrace();
        }
        LOGGER.info(strippedText);

        return ResponseEntity.status(HttpStatus.OK);
    }

    public static byte[] convertToByteArray(String data) {
        String encodingPrefix = "base64,";
        int contentStartIndex = data.indexOf(encodingPrefix) + encodingPrefix.length();
        return Base64.getDecoder().decode((data.substring(contentStartIndex)));

    }

    private String extractTextFromScannedDocument(PDDocument document)
            throws IOException, TesseractException {

        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        _tesseract.setDatapath("/usr/share/tessdata/");
        _tesseract.setLanguage("ell"); // choose your language

        for (int page = 0; page < document.getNumberOfPages(); page++)
        {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // Create a temp image file
            File temp = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bim, "png", temp);

            String result = _tesseract.doOCR(temp);
            out.append(result);

            // Delete temp file
            temp.delete();

        }

        return out.toString();

    }
}
