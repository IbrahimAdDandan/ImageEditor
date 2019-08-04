package imageeditor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GUI extends JPanel implements MouseListener, MouseMotionListener {

    private int xpos;
    private int ypos;
    File file;
    String imagePath;
    BufferedImage image;
    BufferedImage originalImage;
    BufferedImage notZoomed, anotherImage;
    JLabel picLabel;
    JMenuBar menubar;
    JMenu menu, proccessing;
    JMenuItem miNew, miSave, miClose, greyScale, ushortGrey, threeByteGBR, byteGrey,
            intRGB, intARGB, redHued, negativeColor, sepia;
    Color color = Color.BLACK;
    ActionListener close, open, save, chooseingColor, zoomingIn, zoomingOut,
            drawing, erasing, greyScaleAL, ushortGreyAL, threeByteGBRAL,
            byteGreyAL, intRGBAL, intARGBAL, redHuedAL, negativeColorAL, sepiaAL, compositeImageAL;
    Thread t;
    JButton zoomin, zoomout, chooseColor, draw, erase, compositeImage;
    boolean isDrawing = true;

    public GUI() {
        super();
        this.imagePath = "/home/ibrahim/Pictures/";
        addMouseListener(this);
        addMouseMotionListener(this);
        setLayout(new FlowLayout());
        ImageProcessor imageProccessor = new ImageProcessor();
        this.picLabel = new JLabel();
        this.file = new File(this.imagePath + "Black_Name.png");
        try {
            this.image = ImageIO.read(this.file);
            this.originalImage = ImageIO.read(this.file);
            this.notZoomed = ImageIO.read(this.file);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        this.close = (ActionEvent ae) -> {
            System.exit(0);
        }; // End close
        this.open = (ActionEvent ae) -> {
            Component frame = null;
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(this.file);
            chooser.setSelectedFile(this.file);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.OPEN_DIALOG) {
                this.file = chooser.getSelectedFile();
                try {
                    //                t = new Thread(() -> {
                    this.image = ImageIO.read(this.file);
                    this.originalImage = ImageIO.read(this.file);
                    this.notZoomed = ImageIO.read(this.file);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                repaint();
//                t.start();
            }
        }; // End open
        this.save = (ActionEvent ae) -> {
            try {
                ImageIO.write(this.image, "jpg", new File(this.imagePath + "edited.jpg"));
                System.out.println("File Saved!...");
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }; // End save
        this.chooseingColor = (ActionEvent ae) -> {
            this.color = JColorChooser.showDialog(null, "Choose a color", this.color);
        };
        this.zoomingIn = (ActionEvent ae) -> {
            this.image = this.resizeImage(this.image, this.image.getWidth() + 20, this.image.getHeight() + 20);
            this.originalImage = this.resizeImage(this.originalImage, this.image.getWidth() + 20, this.image.getHeight() + 20);
            repaint();
        };
        this.drawing = (ActionEvent ae) -> {
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            this.draw.setEnabled(false);
            this.erase.setEnabled(true);
            this.isDrawing = true;
        };
        this.erasing = (ActionEvent ae) -> {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.draw.setEnabled(true);
            this.erase.setEnabled(false);
            this.isDrawing = false;
        };
        this.zoomingOut = (ActionEvent ae) -> {
            int newWidth = (this.image.getWidth() - 20) > 20 ? (this.image.getWidth() - 20) : this.image.getWidth();
            int newHeight = (this.image.getHeight() - 20) > 20 ? (this.image.getHeight() - 20) : this.image.getHeight();
            this.image = this.resizeImage(this.image, newWidth, newHeight);
            this.originalImage = this.resizeImage(this.originalImage, newWidth, newHeight);
            repaint();
        };
        this.greyScaleAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.greyScale(this.image);
                repaint();
            });
            t.start();
            t = null;
        };
        this.ushortGreyAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.convert(this.image, BufferedImage.TYPE_USHORT_GRAY);
                repaint();
            });
            t.start();
            t = null;
        };
        this.threeByteGBRAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.convert(this.image, BufferedImage.TYPE_3BYTE_BGR);
                repaint();
            });
            t.start();
            t = null;
        };
        this.byteGreyAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.convert(this.image, BufferedImage.TYPE_BYTE_GRAY);
                repaint();
            });
            t.start();
            t = null;
        };
        this.intRGBAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.convert(this.image, BufferedImage.TYPE_INT_RGB);
                repaint();
            });
            t.start();
            t = null;
        };
        this.intARGBAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.convert(this.image, BufferedImage.TYPE_INT_ARGB);
                repaint();
            });
            t.start();
            t = null;
        };
        this.redHuedAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.redHued(this.image);
                repaint();
            });
            t.start();
            t = null;
        };
        this.negativeColorAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.negativeColor(this.image);
                repaint();
            });
            t.start();
            t = null;
        };
        this.sepiaAL = (ActionEvent ae) -> {
            t = new Thread(() -> {
                this.originalImage = this.image = imageProccessor.sepia(this.image);
                repaint();

            });
            t.start();
            t = null;
        };
        this.compositeImageAL = (ActionEvent ae) -> {
            Component frame = null;
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(this.file);
            chooser.setSelectedFile(this.file);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.OPEN_DIALOG) {
                try {
                    this.anotherImage = ImageIO.read(chooser.getSelectedFile());
                    BufferedImage temp = new BufferedImage(this.anotherImage.getWidth(), this.anotherImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D anotherG2d = (Graphics2D) temp.getGraphics();
                    anotherG2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    anotherG2d.drawImage(this.anotherImage, 0, 0, null);
                    Graphics2D g2d = (Graphics2D) this.image.getGraphics();
                    g2d.drawImage(temp, 0, 0, null);
                    anotherG2d.dispose();
                    g2d.dispose();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                repaint();
            }
        };
        this.menubar = new JMenuBar();
        this.menu = new JMenu("File");
        this.miNew = new JMenuItem("New");
        this.miNew.addActionListener(this.open);
        this.menu.add(miNew);
        this.menu.addSeparator();
        this.miSave = new JMenuItem("Save As");
        this.miSave.addActionListener(this.save);
        this.menu.add(miSave);
        this.menu.addSeparator();
        this.miClose = new JMenuItem("Close");
        this.miClose.addActionListener(this.close);
        this.menu.add(miClose);
        this.chooseColor = new JButton("Choose color");
        this.chooseColor.addActionListener(this.chooseingColor);

        this.menubar.add(this.menu);
        this.menubar.add(this.chooseColor);
        this.zoomin = new JButton("Zoom in");
        this.zoomin.addActionListener(this.zoomingIn);
        this.menubar.add(this.zoomin);
        this.zoomout = new JButton("Zoom out");
        this.zoomout.addActionListener(this.zoomingOut);
        this.menubar.add(this.zoomout);
        this.draw = new JButton("Draw");
        this.draw.addActionListener(this.drawing);
        this.menubar.add(this.draw);
        this.erase = new JButton("Erase");
        this.erase.addActionListener(this.erasing);
        this.menubar.add(this.erase);
        this.draw.setEnabled(false);
        this.erase.setEnabled(true);
        this.proccessing = new JMenu("Proccess Image");
        this.greyScale = new JMenuItem("Grey Scale");
        this.greyScale.addActionListener(this.greyScaleAL);
        this.proccessing.add(this.greyScale);
        this.ushortGrey = new JMenuItem("UShort Grey");
        this.ushortGrey.addActionListener(this.ushortGreyAL);
        this.proccessing.add(this.ushortGrey);
        this.threeByteGBR = new JMenuItem("3 Byte GBR");
        this.threeByteGBR.addActionListener(this.threeByteGBRAL);
        this.proccessing.add(this.threeByteGBR);
        this.byteGrey = new JMenuItem("Byte Grey");
        this.byteGrey.addActionListener(this.byteGreyAL);
        this.proccessing.add(this.byteGrey);
        this.intRGB = new JMenuItem("Int RGB");
        this.intRGB.addActionListener(this.intRGBAL);
        this.proccessing.add(this.intRGB);
        this.intARGB = new JMenuItem("Int ARGB");
        this.intARGB.addActionListener(this.intARGBAL);
        this.proccessing.add(this.intARGB);
        this.redHued = new JMenuItem("Red Hued");
        this.redHued.addActionListener(this.redHuedAL);
        this.proccessing.add(this.redHued);
        this.negativeColor = new JMenuItem("Negative");
        this.negativeColor.addActionListener(this.negativeColorAL);
        this.proccessing.add(this.negativeColor);
        this.sepia = new JMenuItem("Sepia");
        this.sepia.addActionListener(this.sepiaAL);
        this.proccessing.add(this.sepia);
        this.menubar.add(this.proccessing);
        this.compositeImage = new JButton("Composite Image");
        this.compositeImage.addActionListener(this.compositeImageAL);
        this.menubar.add(this.compositeImage);
        add(this.menubar);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    public JMenuBar getMenu() {
        return this.menubar;
    }

    @Override
    public void paintComponent(Graphics g) {

        this.picLabel.setIcon(new ImageIcon(this.image));
        this.picLabel.setBounds(0, 50, this.image.getWidth(), this.image.getHeight());
        add(this.picLabel);
//                System.out.println("Repainted!...");
        super.paintComponent(g);

    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        this.xpos = me.getX();
        this.ypos = me.getY() - 50;

        if (this.xpos > 0 && this.ypos > 0 && this.xpos < this.image.getWidth() && this.ypos < this.image.getHeight()) {
            if (this.isDrawing) {
                this.image.setRGB(this.xpos, this.ypos, this.color.getRGB());
            } else {
                this.image.setRGB(this.xpos, this.ypos, this.originalImage.getRGB(this.xpos, this.ypos));
            }
            System.out.println("Pressed" + "  x: " + this.xpos + "  y: " + this.ypos);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        this.xpos = me.getX();
        this.ypos = me.getY() - 50;

        if (this.xpos > 0 && this.ypos > 0 && this.xpos < this.image.getWidth() && this.ypos < this.image.getHeight()) {
            if (this.isDrawing) {
                this.image.setRGB(this.xpos, this.ypos, this.color.getRGB());
            } else {
                this.image.setRGB(this.xpos, this.ypos, this.originalImage.getRGB(this.xpos, this.ypos));
            }
            System.out.println("Mouse Dragged" + "  x: " + this.xpos + "  y: " + this.ypos);
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {

    }

    public BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, this.notZoomed.getType());
        Graphics2D g = bufferedImage.createGraphics();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(width / (double) this.notZoomed.getWidth(), height / (double) this.notZoomed.getHeight());
        g.drawImage(this.notZoomed, scaleTransform, null);
        g.dispose();

        return bufferedImage;
    }

}
