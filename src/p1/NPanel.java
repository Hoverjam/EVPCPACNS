package p1;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
 
public class NPanel extends JPanel {
	
	String url="src\\icon\\white.jpg";
	public NPanel(String url){
		super();
		this.url = url;
	}
	public NPanel(){
		super();
	}
	@Override
	public void paintComponent(Graphics gs) {
		Graphics2D g = (Graphics2D) gs;
		super.paintComponent(g);
 
		InputStream in;
		try {
			in = new FileInputStream(url);
			// ������ͼƬ
			// Image image =
			// Toolkit.getDefaultToolkit().getImage(getClass().getResource("  "));
			BufferedImage image = ImageIO.read(in);
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),
					this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}