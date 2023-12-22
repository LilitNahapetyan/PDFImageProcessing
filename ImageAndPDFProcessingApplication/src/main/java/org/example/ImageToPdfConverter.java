package org.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Converts an image to a PDF, allowing users to add a signature to the image.
 */
public class ImageToPdfConverter {

    /**
     * Main method to execute the image to PDF conversion process.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Read the image from the resource
            BufferedImage image = ImageIO.read(ImageToPdfConverter.class.getResourceAsStream("/logo.jpg"));

            // Prompt the user to input a signature
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your signature (2 or 3 characters): ");
            String userSignature = scanner.nextLine().toUpperCase();

            // Add user-input signature to the image
            addSignatureToImage(image, userSignature);

            // Prompt the user to input the path where the PDF file should be saved
            String outputPath = promptForValidPath(scanner);

            // Save the modified image as PDF with the specified output path
            saveImageAsPdf(image, outputPath);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to enter a valid path for saving the PDF file.
     *
     * @param scanner Scanner object for user input.
     * @return Valid path entered by the user.
     */
    private static String promptForValidPath(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter the path where you want to save the PDF file (including the file name): ");
                String outputPath = scanner.nextLine();

                // Try creating an output stream to validate the path
                new FileOutputStream(outputPath).close();

                // If successful, return the valid path
                return outputPath;
            } catch (IOException e) {
                System.out.println("Invalid path. Please enter a valid path.");
            }
        }
    }

    /**
     * Adds a signature to the center of the image.
     *
     * @param image Image to which the signature is added.
     * @param text  Signature text.
     */
    private static void addSignatureToImage(BufferedImage image, String text) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        Graphics2D g2d = image.createGraphics();

        // Set font and color as needed
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);

        // Get the FontMetrics to calculate the width of the text
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);

        // Calculate the X and Y coordinates to center the text
        int textX = (imageWidth - textWidth) / 2;
        int textY = (imageHeight - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        // Draw the text
        g2d.drawString(text, textX, textY);

        g2d.dispose();
    }

    /**
     * Saves the modified image as a PDF file.
     *
     * @param image      Modified image to be saved.
     * @param outputPath Path where the PDF file should be saved.
     * @throws DocumentException If an error occurs during PDF document creation.
     * @throws IOException       If an I/O error occurs.
     */
    private static void saveImageAsPdf(BufferedImage image, String outputPath) throws DocumentException, IOException {
        // Create a new document
        Document document = new Document();

        // Set the page size to match the image dimensions
        document.setPageSize(new Rectangle(image.getWidth(), image.getHeight()));

        // Specify the output file
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        // Convert BufferedImage to iText Image
        Image pdfImage = Image.getInstance(toByteArray(image));

        // Add the image to the PDF document
        document.add(pdfImage);

        document.close();
    }

    /**
     * Converts a BufferedImage to a byte array.
     *
     * @param image BufferedImage to be converted.
     * @return Byte array representation of the image.
     * @throws IOException If an I/O error occurs.
     */
    private static byte[] toByteArray(BufferedImage image) throws IOException {
        // Convert BufferedImage to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
