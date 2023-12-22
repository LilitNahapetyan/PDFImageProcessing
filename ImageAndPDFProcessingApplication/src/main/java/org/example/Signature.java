package org.example;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Signature {

    private static final float SIGNATURE_FONT_SIZE = 12f;
    private static final String STAMPS_DIRECTORY = "/src/main/resources/img.png/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your short signature (2 or 3 characters): ");
        String signature = scanner.nextLine();

        System.out.print("Choose a stamp (1 or 2): ");
        int stampNumber = scanner.nextInt();

        try {
            createPDF(signature, "stamp" + stampNumber + ".png");
            System.out.println("PDF created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createPDF(String signature, String stampImageName) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        document.addPage(page);

        PDImageXObject stampImage = loadImage(stampImageName, document);
        float stampWidth = stampImage.getWidth();
        float stampHeight = stampImage.getHeight();

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

        // Stamp Image
        contentStream.drawImage(stampImage, 0, 0, stampWidth, stampHeight);

        // Signature Text
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, SIGNATURE_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(10, 10);
        contentStream.showText(signature);
        contentStream.endText();

        contentStream.close();

        String outputFilePath = "output.pdf"; // Output file path
        document.save(outputFilePath);
        document.close();
    }

    private static PDImageXObject loadImage(String imageName, PDDocument document) throws IOException {
        try (InputStream inputStream = Signature.class.getResourceAsStream(STAMPS_DIRECTORY + imageName)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Stamp image not found: " + imageName);
            }
            return PDImageXObject.createFromByteArray(document, IOUtils.toByteArray(inputStream), imageName);
        }
    }

}

