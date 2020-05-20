package p1;
 import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;
 
public class RoundBorder implements Border {
	private Color color;
 
	public RoundBorder(Color color) {// �в����Ĺ��췽��
		this.color = color;
	}
 
	public RoundBorder() {// �޲ι��췽��
		this.color = Color.BLACK;
		// ���ʵ����ʱ��û�д�ֵ
		// Ĭ���Ǻ�ɫ�߿�
	}
 
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, 0, 0);
	}
 
	public boolean isBorderOpaque() {
		return false;
	}
 
	// ʵ��Border�����ࣩ����
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 15, 15);
	}
}