package nomadrealms.tool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheetGenerator {

    private static class ImageEntry {
        File file;
        BufferedImage image;

        ImageEntry(File file, BufferedImage image) {
            this.file = file;
            this.image = image;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java SpriteSheetGenerator <outputPath> <inputPath1> [inputPath2] ...");
            System.out.println("Note: <outputPath> should not include extension.");
            return;
        }

        String outputPath = args[0];
        List<File> imageFiles = new ArrayList<>();

        for (int i = 1; i < args.length; i++) {
            File file = new File(args[i]);
            if (file.isDirectory()) {
                File[] files = file.listFiles((dir, name) -> {
                    String lower = name.toLowerCase();
                    return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
                });
                if (files != null) {
                    for (File f : files) {
                        imageFiles.add(f);
                    }
                }
            } else if (file.isFile()) {
                imageFiles.add(file);
            }
        }

        if (imageFiles.isEmpty()) {
            System.out.println("No images found.");
            return;
        }

        List<ImageEntry> entries = new ArrayList<>();
        for (File f : imageFiles) {
            BufferedImage img = ImageIO.read(f);
            if (img != null) {
                entries.add(new ImageEntry(f, img));
            } else {
                System.err.println("Could not read image: " + f.getAbsolutePath());
            }
        }

        if (entries.isEmpty()) {
            System.out.println("No valid images could be loaded.");
            return;
        }

        int n = entries.size();
        int columns = (int) Math.ceil(Math.sqrt(n));
        int rows = (int) Math.ceil((double) n / columns);

        int padding = 2;
        int maxWidth = 0;
        int maxHeight = 0;

        for (ImageEntry entry : entries) {
            maxWidth = Math.max(maxWidth, entry.image.getWidth());
            maxHeight = Math.max(maxHeight, entry.image.getHeight());
        }

        int cellWidth = maxWidth + padding * 2;
        int cellHeight = maxHeight + padding * 2;

        int sheetWidth = columns * cellWidth;
        int sheetHeight = rows * cellHeight;

        BufferedImage spriteSheet = new BufferedImage(sheetWidth, sheetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = spriteSheet.createGraphics();

        try {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputPath + ".txt"), StandardCharsets.UTF_8)) {
                for (int i = 0; i < entries.size(); i++) {
                    ImageEntry entry = entries.get(i);
                    int col = i % columns;
                    int row = i / columns;

                    int x = col * cellWidth + padding;
                    int y = row * cellHeight + padding;

                    g.drawImage(entry.image, x, y, null);

                    String fileName = entry.file.getName();
                    int lastDot = fileName.lastIndexOf('.');
                    String name = (lastDot == -1) ? fileName : fileName.substring(0, lastDot);
                    writer.write(name + " " + x + " " + y + " " + entry.image.getWidth() + " " + entry.image.getHeight() + "\n");
                }
            }
        } finally {
            g.dispose();
        }

        ImageIO.write(spriteSheet, "png", new File(outputPath + ".png"));
        System.out.println("Sprite sheet created: " + outputPath + ".png");
        System.out.println("Metadata created: " + outputPath + ".txt");
    }
}
