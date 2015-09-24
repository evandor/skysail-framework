//package io.skysail.client.testsupport;
//
//import java.awt.*;
//import java.awt.datatransfer.Transferable;
//import java.awt.event.KeyEvent;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.net.*;
//
//import javax.imageio.ImageIO;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class BrowserUtils {
//
//    private Desktop desktop;
//
//    public BrowserUtils() {
//        desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
//        if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) {
//            log.error("desktop object is not supported!");
//        }
//    }
//
//    public void screenShot(String url) throws Exception {
//        openWebpage(new URL(url));
//        try {
//            Thread.sleep(2000 * 2);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Robot robot = new Robot();
//
//        robot.keyPress(KeyEvent.VK_ALT);
//        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
//        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
//        robot.keyRelease(KeyEvent.VK_ALT);
//
//        try {
//            Thread.sleep(1000 * 2);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
//        //RenderedImage capture = (RenderedImage) t.getTransferData(DataFlavor.imageFlavor);
//        //ImageIO.write(capture, "png", new File("generated/output.png"));
//
//        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//        ImageIO.write(image, "png", new File("generated/output.png"));
//    }
//
//    private void openWebpage(URL url) {
//        try {
//            openWebpage(url.toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void openWebpage(URI uri) {
//        try {
//            desktop.browse(uri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
