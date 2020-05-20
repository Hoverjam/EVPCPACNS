package p1;
import java.awt.AlphaComposite;  
import java.awt.Color;  
import java.awt.Dimension;  
import java.awt.GradientPaint;  
import java.awt.Graphics;  
import java.awt.Graphics2D;  
import java.awt.Rectangle;  
import java.awt.RenderingHints;  
  
import javax.swing.ImageIcon;  
import javax.swing.JButton;  
import javax.swing.JComponent;  
import javax.swing.JScrollBar;  
import javax.swing.plaf.basic.BasicScrollBarUI;  
  
// 自定义滚动条UI 

public class NScrollBar extends BasicScrollBarUI {  
  
    @Override  
    protected void configureScrollBarColors() {  
        // 把手  
        // thumbColor = Color.GRAY;  
        // 滑道  
        trackColor = Color.gray;  
        setThumbBounds(0, 0, 5, 5);  
        // trackHighlightColor = Color.GREEN;  
    }  
  
    /** 
     * 设置滚动条的宽度 
     */  
    @Override  
    public Dimension getPreferredSize(JComponent c) {  
        // TODO Auto-generated method stub  
        c.setPreferredSize(new Dimension(5, 5));  
        return super.getPreferredSize(c);  
    }  
  
    // 重绘滑块的滑动区域背景  
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {  
        Graphics2D g2 = (Graphics2D) g;  
        GradientPaint gp = null;  
        //判断滚动条是垂直的 还是水平的  
        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {  
            //设置画笔  
            gp = new GradientPaint(0, 0, new Color(255,255,255),  
                    trackBounds.width, 0, new Color(255,255,255));  
        }  
        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {  
            gp = new GradientPaint(0, 0, new Color(255,255,255),  
                    trackBounds.height, 0, new Color(255,255,255));  
        } 
  
        g2.setPaint(gp);  
        //填充Track  
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width,  
                trackBounds.height);  
        //绘制Track的边框  
        if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT)  
            this.paintDecreaseHighlight(g);  
        if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT)  
            this.paintIncreaseHighlight(g);  
    }  
  
    @Override  
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {  
        // 把绘制区的x，y点坐标定义为坐标系的原点  
        g.translate(thumbBounds.x, thumbBounds.y);  
        // 消除锯齿  
        Graphics2D g2 = (Graphics2D) g;  
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,  
                RenderingHints.VALUE_ANTIALIAS_ON);  
        g2.addRenderingHints(rh);  
        // 填充圆角矩形  
        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
        	g.setColor(new Color(15,143,234)); 
        	g2.fillRoundRect(0, 0, 5, thumbBounds.height - 1, 5,7);
        }
        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL)
        {
        	g.setColor(new Color(255,255,0)); 
        	g2.fillRoundRect(0, 0, thumbBounds.width - 1, 5, 5,7);  
        }
    }  
  
    /** 
     * 创建滚动条上方的按钮 
     */  
    @Override  
    protected JButton createIncreaseButton(int orientation) {  
        JButton button = new JButton();  
        button.setBorderPainted(false);  
        button.setContentAreaFilled(false);  
        button.setBorder(null);  
        return button;  
    }  
  
    /** 
     * 创建滚动条下方的按钮 
     */  
    @Override  
    protected JButton createDecreaseButton(int orientation) {  
        JButton button = new JButton();  
        button.setBorderPainted(false);  
        button.setContentAreaFilled(false);  
        button.setFocusable(false);  
        button.setBorder(null);  
        return button;  
    }  
  
      
  
}  