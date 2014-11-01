import javax.swing.JFrame;

import components.GraphicsFrame;
/**
 * Main Program for running the bouncing ball as a standalone application.
 */
public class Main {
   // Entry main program
   public static void main(String[] args) {
      // Run UI in the Event Dispatcher Thread (EDT), instead of Main thread
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JFrame frame = new JFrame("AI Project");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GraphicsFrame(640, 280)); // JPanel
            frame.pack();            // Preferred size of BallWorld
            frame.setResizable(false);
            frame.setVisible(true);  // Show it
         }
      });
   }
}
