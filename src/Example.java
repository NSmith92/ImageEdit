import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.Math.*;
import java.util.Scanner;

// OK this is not best practice - maybe you'd like to extend
// the BufferedImage class for the image processing operations?
// I won't give extra marks for that though.
public class Example extends JFrame {

	JButton invert_button, slow_gamma_button, fast_gamma_button, correlate_button, equal_button, contrast_button, histogram_button; //Creating the Buttons
	JLabel image_icon;
	BufferedImage image;
	JSlider val_slider; //slider example
	
    /*
        This function sets up the GUI and reads an image
    */
	public void Example() throws IOException {
        // File name is hardcoded here - much nicer to have a dialog
		File image_file = new File("raytrace.jpg");
        
        // Open the file and read it into a BufferedImage
		image = ImageIO.read(image_file);

        // Set up the simple GUI
        // First the container:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());
        
        // Then our image (as a label icon)
		image_icon=new JLabel(new ImageIcon(image));
		container.add(image_icon);
 
        // Then the invert button
		invert_button = new JButton("Invert");
		container.add(invert_button);

        // Then the equalize button
		equal_button = new JButton("Equalize");
		container.add(equal_button);
        
        // Gamma buttons
		slow_gamma_button = new JButton("Slow Gamma");
		container.add(slow_gamma_button);
		fast_gamma_button = new JButton("Fast Gamma");
		container.add(fast_gamma_button);
        
        // Correlate button
		correlate_button = new JButton("Correlate");
		container.add(correlate_button);
		
		//Contrast Stretching button
		contrast_button = new JButton("Contrast Stretching");
		container.add(contrast_button);
		
		//Historgram example
		histogram_button = new JButton("Histogram");
		container.add(histogram_button);
		
		//Slider example
		val_slider = new JSlider(0,100);
		container.add(val_slider);
		//Add labels
		val_slider.setMajorTickSpacing(50);
		val_slider.setMinorTickSpacing(10);
		val_slider.setPaintTicks(true);
		val_slider.setPaintLabels(true);
		//see
		//http://docs.oracle.com/javase/7/docs/api/javax/swing/JSlider.html
		//for documentation (e.g. how to get the value, how to display vertically if you want)
        		

        // Now all the handlers
		GUIEventHandler handler = new GUIEventHandler();

        // Button handlers
		invert_button.addActionListener(handler);
		slow_gamma_button.addActionListener(handler);
		fast_gamma_button.addActionListener(handler);
		correlate_button.addActionListener(handler);
		equal_button.addActionListener(handler);
		contrast_button.addActionListener(handler);
		histogram_button.addActionListener(handler);
		val_slider.addChangeListener(handler);
        // ... and display everything
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
    }
    
    /*
        This is the event handler for the application
    */
	private class GUIEventHandler implements ActionListener, ChangeListener {
   		//Change handler (e.g. for sliders)
		public void stateChanged(ChangeEvent e) {
			System.out.println(val_slider.getValue());
			//you could pass the value to another function to change something
            //then update the image
		}

		public void actionPerformed(ActionEvent event) {
			if (event.getSource()==invert_button) {
                        // Call image processing function
				image=Invert(image);
        
                        // Update image
				image_icon.setIcon(new ImageIcon(image));
				} else if (event.getSource()==slow_gamma_button) {
                        // Call image processing function
					image=SlowGamma(image);
        
                        // Update image
					image_icon.setIcon(new ImageIcon(image));
				} else if (event.getSource()==fast_gamma_button) {
                        // Call image processing function
					image=FastGamma(image);
        
                        // Update image
					image_icon.setIcon(new ImageIcon(image));
				} else if (event.getSource()==correlate_button) {
                        // Call image processing function
					image=BlueFade(image);
        
                        // Update image
					image_icon.setIcon(new ImageIcon(image));
				} else if (event.getSource()==equal_button) {
						//Call function
					image=Equalise(image);
						
                        // Update image
					image_icon.setIcon(new ImageIcon(image));
				} else if(event.getSource()==contrast_button){
					image=ContrastStretching(image);
					image_icon.setIcon(new ImageIcon(image));
				} else if(event.getSource()==histogram_button){
					image=Histograms(image);
					image_icon.setIcon(new ImageIcon(image));
				}
			}
		}

    /*
        This function will return a pointer to an array
        of bytes which represent the image data in memory.
        Using such a pointer allows fast access to the image
        data for processing (rather than getting/setting
        individual pixels)
    */
	public static byte[] GetImageData(BufferedImage image) {
		WritableRaster WR=image.getRaster();
		DataBuffer DB=WR.getDataBuffer();
		if (DB.getDataType() != DataBuffer.TYPE_BYTE)
		throw new IllegalStateException("That's not of type byte");
          
		return ((DataBufferByte) DB).getData();
		
	}

	public BufferedImage Equalise(BufferedImage image) {
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
            //Obtain pointer to data for fast processing
		byte[] data = GetImageData(image);
		int[] histogram;

		return image;
	}

    /*
        This function shows how to carry out an operation on an image.
        It obtains the dimensions of the image, and then loops through
        the image carrying out the invert.
    */
	public BufferedImage Invert(BufferedImage image) {
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
            //Obtain pointer to data for fast processing
		byte[] data = GetImageData(image);
            
            //Shows how to loop through each pixel and colour
            //Try to always use j for loops in y, and i for loops in x
            //as this makes the code more readable
		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					data[c+3*i+3*j*w]=(byte) (255-(data[c+3*i+3*j*w]&255));
				} // colour loop
			} // column loop
		} // row loop
	
		return image;
	}

	public BufferedImage SlowGamma(BufferedImage image) {
	
	
	String gammavalue = JOptionPane.showInputDialog("Please enter a Gamma Value above 0"); //Ask for user Input
	double gamma= Double.parseDouble(gammavalue);
	while(gamma < 0){
		gammavalue = JOptionPane.showInputDialog("Please enter a Gamma Value above 0");
		gamma= Double.parseDouble(gammavalue);
	}
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);

		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					data[c+3*i+3*j*w]=(byte) (Math.pow((double) (data[c+3*i+3*j*w] & 255)/255.0, 1/gamma) * 255); //Gamma Equation in Java
					//gamma is hardcoded as 2.8
				} // colour loop
			} // column loop
		} // row loop
		
		return image;
		
	}
	
	public BufferedImage FastGamma(BufferedImage image) {
            //Get image dimensions, and declare loop variables
			
		double colour = 255;
		
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);
            //Obtain pointer to data for fast processing
			

	String gammavalue = JOptionPane.showInputDialog("Please enter a Gamma Value above 0"); //Ask for user Input
	double gamma= Double.parseDouble(gammavalue);
	while(gamma < 0){
		gammavalue = JOptionPane.showInputDialog("Please enter a Gamma Value above 0");
		gamma= Double.parseDouble(gammavalue);
		}
		double fastGamma = gamma; //initiating the fast gamma 
		int[] fastGammaTable = new int[256]; //Creating the lookup Table
		// 256 is the amount of values/operations.
		for(i=0; i<fastGammaTable.length; i++){ //While is is less than 256, loop through each colour.
			fastGammaTable[i] = (int)(colour*(Math.pow((double) i/(double) colour, 1/gamma)));
			}
			//mapping through the lookup table
			//Creating an array to store the gamma value rahter than calculate every time

		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					data[c+3*i+3*j*w]=(byte) fastGammaTable[data[c+3*i+3*j*w]&255]; //Gamma Equation in Java
					//gamma is hardcoded as 2.8https://www.facebook.com/addgroup?ref=bookmarks
				} // colour loop
			} // column loop
		} // row loop


		return image;
	}
	
	public BufferedImage ContrastStretching(BufferedImage image){
		//Get image dimensions, and declare loop variables
		
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);
		
		String r1value = JOptionPane.showInputDialog("Please enter a value for R1"); //Ask for user Input
		double r1 = Double.parseDouble(r1value);
		String r2value = JOptionPane.showInputDialog("Please enter a value for R2"); 
		double r2 = Double.parseDouble(r2value);
		String s1value = JOptionPane.showInputDialog("Please enter a value for S1"); 
		double s1 = Double.parseDouble(s1value);
		String s2value = JOptionPane.showInputDialog("Please enter a value for S2");
		double s2 = Double.parseDouble(s2value);
		
		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) { //Contrast Stretching Equation
					if((data[c+3*i+3*j*w]&255) < r1){
						data[c+3*i+3*j*w]=(byte) ((s1/r1)*(data[c+3*i+3*j*w]&255));
					}else if(r1<= (data[c+3*i+3*j*w]&255) && (data[c+3*i+3*j*w]&255)<=r2) {
						data[c+3*i+3*j*w]=(byte) ((((s2-s1)/(r2-r1))*(data[c+3*i+3*j*w]&255) -r1) + s1);
					}else if((data[c+3*i+3*j*w]&255)>= r2){
						data[c+3*i+3*j*w]=(byte) (((255-s2)/(255-r2))*((data[c+3*i+3*j*w]&255) -r2) + s2);
					}
				} // colour loop
			} // column loop
		} // row loop
		
		return image;
	}
	
	public BufferedImage Histograms(BufferedImage image){	
		
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);
		
		int [][] histogram;
		
		histogram = new int[256][3];
		
		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					histogram[data[c+3*+i+3*j*w]&255][c]++;	
				}
			}	
		}		
		
		for(i=0; i<256; i++){
			System.out.println(i+ "r=" + histogram[i][2] +
			"g=" + histogram[i][1] +
			"b=" + histogram[i][0]);
		}
		
		return image;
	}
		
	public BufferedImage BlueFade(BufferedImage image) {
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
            //Obtain pointer to data for fast processing
		byte[] data = GetImageData(image);
		int int_image[][][];
		double t;
            
		int_image = new int[h][w][3];
            
            // Copy byte data to new image taking care to treat bytes as unsigned
		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					int_image[j][i][c]=data[c+3*i+3*j*w]&255;
				} // colour loop
			} // column loop
		} // row loop
            
            // Now carry out processing on this different data typed image (e.g. correlation or "bluefade"
		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				int_image[j][i][0]=255*j/h; //BLUE
				int_image[j][i][1]=0; //GREEN
				int_image[j][i][2]=0; //RED
			} // column loop
		} // row loop
            
            // Now copy the processed image back to the original
		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					data[c+3*i+3*j*w]=(byte) int_image[j][i][c];
				} // colour loop
			} // column loop
		} // row loop
            

		return image;
	}


	public static void main(String[] args) throws IOException {
 
		Example e = new Example();
		e.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		e.Example();
	}
	
}