package client.media;

import communication.DataFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by zhi huang on 2015/11/1.
 * Retur
 */
public class VideoFrameReceiver implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private BufferedImage image;
    private JFrame frame = new JFrame();

    public VideoFrameReceiver(Socket socket) throws IOException, ClassNotFoundException {
        this.socket = socket;
        frame.getContentPane().add(new JPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Received Video");
        frame.setLocation(0, 0);
        frame.setVisible(true);
    }

    private void paintFrame() {
        frame.setSize(image.getWidth(), image.getHeight() + 30);
        frame.getContentPane().add(new VideoPanel(image));
        frame.setVisible(true);
    }

    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                DataFrame df = (DataFrame) objectInputStream.readObject();
                image = ImageIO.read(new ByteArrayInputStream(df.getBytes()));
                paintFrame();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
