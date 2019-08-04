package imageeditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageProcessor {

    /**
     *
     * @param img
     * @return
     *
     * Avg = (R+G+B)/3
     */
    public BufferedImage greyScale(BufferedImage img) {
        BufferedImage image = img;
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                int avg = (r + g + b) / 3;
                p = (a << 24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }

    /**
     *
     * @param img
     * @return
     *
     * R = 255 – R G = 255 – G B = 255 – B
     */
    public BufferedImage negativeColor(BufferedImage img) {
        BufferedImage image = img;
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = image.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, p);
            }
        }
        return image;
    }

    /**
     *
     * @param image
     * @return
     *
     * R: NO CHANGE G: Set to 0 B: Set to 0
     */
    public BufferedImage redHued(BufferedImage image) {
        BufferedImage img = image;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                p = (a << 24) | (r << 16) | (0 << 8) | 0;
                img.setRGB(x, y, p);
            }
        }
        return img;
    }

    /**
     *
     * @param image
     * @return
     *
     * newRed = 0.393*R + 0.769*G + 0.189*B
     *
     * newGreen = 0.349*R + 0.686*G + 0.168*B
     *
     * newBlue = 0.272*R + 0.534*G + 0.131*B
     */
    public BufferedImage sepia(BufferedImage image) {
        BufferedImage img = image;
        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int R = (p >> 16) & 0xff;
                int G = (p >> 8) & 0xff;
                int B = p & 0xff;
                int newRed = (int) (0.393 * R + 0.769 * G + 0.189 * B);
                int newGreen = (int) (0.349 * R + 0.686 * G + 0.168 * B);
                int newBlue = (int) (0.272 * R + 0.534 * G + 0.131 * B);
                if (newRed > 255) {
                    R = 255;
                } else {
                    R = newRed;
                }
                if (newGreen > 255) {
                    G = 255;
                } else {
                    G = newGreen;
                }
                if (newBlue > 255) {
                    B = 255;
                } else {
                    B = newBlue;
                }
                p = (a << 24) | (R << 16) | (G << 8) | B;
                img.setRGB(x, y, p);
            }
        }
        return img;
    }

    public BufferedImage cloneBufferedImage(BufferedImage image) {
        return new BufferedImage(image.getColorModel(), image.getRaster(),
                image.getColorModel().isAlphaPremultiplied(), null);
    }

    public BufferedImage convert(BufferedImage image, int imgType) {
        BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), imgType);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return img;
    }

}
