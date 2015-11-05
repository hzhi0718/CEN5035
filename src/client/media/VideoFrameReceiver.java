package client.media;

import communication.DataFrame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.im.InputSubset;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by huang zhi on 2015/11/1.
 */
public class VideoFrameReceiver extends JPanel implements Runnable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private BufferedImage image;
    private JFrame frame = new JFrame();

    public VideoFrameReceiver(Socket socket) throws IOException, ClassNotFoundException {
        System.out.println("start");
        this.socket = socket;
        frame.getContentPane().add(new JPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Received Video");
        frame.setLocation(0, 0);
        frame.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    private void paintFrame(Mat mat) {
        image = MatToBufferedImage(mat);
        frame.setSize(image.getWidth(), image.getHeight() + 30);
        this.frame.getContentPane().add(new JPanel());

    }

    private void paintFrame() {
        frame.setSize(image.getWidth(), image.getHeight() + 30);
        frame.getContentPane().add(new JPanel());
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
                for (int i = 0; i < 20; i++) {
                    System.out.print(df.getBytes()[i]+" ");
                }
                System.out.println();
                ByteArrayInputStream bais = new ByteArrayInputStream(df.getBytes());
                image = ImageIO.read(bais);
                paintFrame();
                Thread.sleep(500);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
}
