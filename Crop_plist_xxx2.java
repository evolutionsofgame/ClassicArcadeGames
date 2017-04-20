import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Crop_plist_xxx2 {
	static String namefile = "bullet";
	public static void main(String[] args) {
		ArrayList<String> textdata = new ArrayList<String>();
		

		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("D:\\assets\\" + namefile + "\\" + namefile + ".plist"));

			while ((sCurrentLine = br.readLine()) != null) {
				textdata.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		int i;
		for (i=0;i<textdata.size();i++)
		{
			if (textdata.get(i).contains("<key>frame</key>"))
			{
				String name = null;
				if (textdata.get(i-2).trim().contains(".png"))
				{
				name = textdata.get(i-2).trim().replace("<key>", "").replace(".png</key>", "");
				}
				else
				{
					name = textdata.get(i-2).trim().replace("<key>", "").replace("</key>", "");
				}
				String[] getint = textdata.get(i+1).trim().replace("<string>{{", "").replace("}}</string>", "").replace("},{", ",").split(",");
				String[] getintEx = textdata.get(i+9).trim().replace("<string>{", "").replace("}</string>", "").split(",");
				
				int X = Integer.parseInt(getint[0].replace(".0", ""));
				int Y = Integer.parseInt(getint[1].replace(".0", ""));
				
				if (textdata.get(i+5).contains("false"))
				{
					int W = Integer.parseInt(getint[2]);
					int H = Integer.parseInt(getint[3]);
					int WEX = Integer.parseInt(getintEx[0]);
					int HEX = Integer.parseInt(getintEx[1]);
					cropImage(name,X,Y,W,H, WEX , HEX, false);
				}
				else
				{
					int W = Integer.parseInt(getint[3]);
					int H = Integer.parseInt(getint[2]);
					int WEX = Integer.parseInt(getintEx[1]);
					int HEX = Integer.parseInt(getintEx[0]);
					cropImage(name,X,Y,W,H, WEX , HEX, true);
				}	
			}
		}

	}
	public static void cropImage(String name, int x, int y, int w, int h, int wEx, int hEx, boolean canRotate)
	{
		try {
			File outputfile;
			BufferedImage originalImgage = ImageIO.read(new File("D:\\assets\\" + namefile + "\\" + namefile + ".png"));

			BufferedImage SubImgage = originalImgage.getSubimage(x, y, w, h);
			if (name.contains("/"))
			{
				if (name.split("/").length==3)
				{
					File dir = new File("D:\\assets\\" + namefile + "\\" + name.split("/")[0]+ "/" + name.split("/")[1]);
					dir.mkdir();
					outputfile = new File(dir + "/" + name.split("/")[2] + ".png");
				}
				else
				{
				File dir = new File("D:\\assets\\" + namefile + "\\" + name.split("/")[0]);
				dir.mkdir();
				outputfile = new File(dir + "/" + name.split("/")[1] + ".png");
				}
			}
			else
			{
			outputfile = new File("D:\\assets\\" + namefile + "\\" + name + ".png");
			}
			//ImageIO.write(SubImgage, "png", outputfile);
			ImageIO.write(resizeImage(SubImgage, wEx, hEx, canRotate), "png", outputfile);
			System.out.println("Cropped Image: " + name +" Dimension: "+SubImgage.getWidth()+"x"+SubImgage.getHeight());


		} catch ( Exception e) {
			e.printStackTrace();
		}

	}
	private static BufferedImage resizeImage(BufferedImage originalImage,Integer img_width, Integer img_height, boolean canRotate)
	{
		BufferedImage resizedImage;
		resizedImage = new BufferedImage(img_width, img_height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		//g.drawImage(originalImage, 0, 0, img_width, img_height, null);

		g.drawImage(originalImage, null, 0, 0);
		g.dispose();
		if (canRotate)
		{
			resizedImage = rotate90ToLeft(resizedImage);
		}

		return resizedImage;
	}
	
	public static BufferedImage rotate90ToRight( BufferedImage inputImage ){
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		BufferedImage returnImage = new BufferedImage( height, width , inputImage.getType()  );

		for( int x = 0; x < width; x++ ) {
			for( int y = 0; y < height; y++ ) {
				returnImage.setRGB(height - y -1, x, inputImage.getRGB( x, y  )  );
	//Again check the Picture for better understanding
			}
		}
		return returnImage;
	}
	
	public static BufferedImage rotate90ToLeft( BufferedImage inputImage ){
		//The most of code is same as before
			int width = inputImage.getWidth();
			int height = inputImage.getHeight();
			BufferedImage returnImage = new BufferedImage( height, width , inputImage.getType()  );
		//We have to change the width and height because when you rotate the image by 90 degree, the
		//width is height and height is width <img src='http://forum.codecall.net/public/style_emoticons/<#EMO_DIR#>/smile.png' class='bbc_emoticon' alt=':)' />

			for( int x = 0; x < width; x++ ) {
				for( int y = 0; y < height; y++ ) {
					returnImage.setRGB(y, width - x - 1, inputImage.getRGB( x, y  )  );
		//Again check the Picture for better understanding
				}
				}
			return returnImage;

		}
	
	public static BufferedImage rotate180( BufferedImage inputImage ) {
		//We use BufferedImage because it’s provide methods for pixel manipulation
			int width = inputImage.getWidth(); //the Width of the original image
			int height = inputImage.getHeight();//the Height of the original image

			BufferedImage returnImage = new BufferedImage( width, height, inputImage.getType()  );
		//we created new BufferedImage, which we will return in the end of the program
		//it set up it to the same width and height as in original image
		// inputImage.getType() return the type of image ( if it is in RBG, ARGB, etc. )

			for( int x = 0; x < width; x++ ) {
				for( int y = 0; y < height; y++ ) {
					returnImage.setRGB( width - x - 1, height - y - 1, inputImage.getRGB( x, y  )  );
				}
			}
		//so we used two loops for getting information from the whole inputImage
		//then we use method setRGB by whitch we sort the pixel of the return image
		//the first two parametres is the X and Y location of the pixel in returnImage and the last one is the //source pixel on the inputImage
		//why we put width – x – 1 and height –y – 1 is hard to explain for me, but when you rotate image by //180degree the pixel with location [0, 0] will be in [ width, height ]. The -1 is for not to go out of
		//Array size ( remember you always start from 0 so the last index is lower by 1 in the width or height
		//I enclose Picture for better imagination  ... hope it help you
			return returnImage;
		//and the last return the rotated image

		}
	
}
