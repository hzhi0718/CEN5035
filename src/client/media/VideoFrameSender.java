package client.media;

import communication.DataFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by huang zhi on 2015/11/1.
 */
public class VideoFrameSender implements Runnable  {

    private ObjectOutputStream objectOutputStream;
    private Socket socket;

    public VideoFrameSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        camera.read(frame);
        JFrame jFrame = new JFrame();
        BufferedImage originalImage = MatToBufferedImage(frame);
        jFrame.setSize(originalImage.getWidth(), originalImage.getHeight() + 30);
        jFrame.setVisible(true);

        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {
            while(true){

                if (camera.read(frame)){
                    try {
                        BufferedImage image =MatToBufferedImage(frame);

                        jFrame.getContentPane().add(new VideoWindow(image));
                        jFrame.setVisible(true);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write( originalImage, "png", baos );
                        baos.flush();
                        byte[] bytes = baos.toByteArray();
                        DataFrame df = new DataFrame(bytes);
                        objectOutputStream.writeObject(df);
                        objectOutputStream.flush();
                        Thread.sleep(100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        camera.release();
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

class VideoWindow extends JPanel {
    BufferedImage image;

    public VideoWindow (BufferedImage image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
