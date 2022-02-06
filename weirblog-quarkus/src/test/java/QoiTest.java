//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//
//import me.saharnooby.qoi.QOIImage;
//import me.saharnooby.qoi.QOIUtil;
//import me.saharnooby.qoi.QOIUtilAWT;
//
//public class QoiTest {
//
//	public static void main(String[] args) {
//		try {
//			// Convert PNG to QOI
//			BufferedImage image = ImageIO.read(new File("/Users/weir/Desktop/0BEFD36FCFAB20487D4B627FC04B6A86.png"));
//			QOIImage qoi = QOIUtilAWT.createFromBufferedImage(image);
//			QOIUtil.writeImage(qoi, new File("1111.qoi"));
//			
//			// Convert QOI to PNG
////			QOIImage secondImage = QOIUtil.readFile(new File("second-image.qoi"));
////			BufferedImage convertedImage = QOIUtilAWT.convertToBufferedImage(secondImage);
////			ImageIO.write(convertedImage, "PNG", new File("second-image.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//}
