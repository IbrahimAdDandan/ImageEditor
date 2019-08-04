package imageeditor;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class ImageEditor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setBounds(500, 100, 760, 500);
            f.add(new GUI());
            
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
