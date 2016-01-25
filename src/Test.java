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
public class Test extends JFrame {

	JButton invert_button, slow_gamma_button, fast_gamma_button, correlate_button, equal_button;
	JLabel image_icon;
	BufferedImage image;
	JSlider val_slider; //slider example
	
	JSlider gamma_slider;


    /*
        This function sets up the GUI and reads an image
    */
	public void Example() throws IOException {
        // File name is hardcoded here - much nicer to have a dialog
		File image_file = new File("/Users/Nathan/Dropbox/Uni Year 2/Semester 2/CS-255/Balls.jpg");
        
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
        
        // Gamma button
		slow_gamma_button = new JButton("Slow Gamma");
		container.add(slow_gamma_button);
		fast_gamma_button = new JButton("Fast Gamma");
		container.add(fast_gamma_button);
        
        // Correlate button
		correlate_button = new JButton("Correlate");
		container.add(correlate_button);
		
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
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);

		for (j=0; j<h; j++) {
			for (i=0; i<w; i++) {
				for (c=0; c<3; c++) {
					data[c+3*i+3*j*w]=(byte) (Math.pow((double) (data[c+3*i+3*j*w] & 255)/255.0, 1/2.8) * 255); //Gamma Equation in Java
					//gamma is hardcoded as 2.8
				} // colour loop
			} // column loop
		} // row loop
		
		return image;
		
	}
	
	public BufferedImage FastGamma(BufferedImage image) {
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
		byte[] data = GetImageData(image);
            //Obtain pointer to data for fast processing

		return image;
	}
    
	public BufferedImage BlueFade(BufferedImage image) {
            //Get image dimensions, and declare loop variables
		int w=image.getWidth(), h=image.getHeight(), i, j, c;
            //Obtain pointer to data for fast processing.8
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
 
		Test e = new Test();
		e.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		e.Example();
	}
	
}