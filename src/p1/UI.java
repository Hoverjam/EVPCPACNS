package p1;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class UI {
	private static int BOARD_SIZE = 6;
	private final int MAP_SIZE_X = 380;
	private final int MAP_SIZE_Y = 410;
	private JFrame f = new JFrame("电动汽车续航电能配置规划与充电导航软件");
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private JLabel b = new JLabel("起点：");
	private JLabel e = new JLabel("终点：");
	private JTextField begin = new JTextField(4);
	private JTextField end = new JTextField(4);
	static JCheckBox crowd = new JCheckBox("是否拥堵",new ImageIcon("src\\icon\\wrong.png"));
	//private JButton guide = new JButton("开始导航");
	private NButton guide = new NButton("src\\icon\\off.png","src\\icon\\over.png","src\\icon\\pressed.png");
	JProgressBar processBar = new JProgressBar();  
	public static JTextArea ta = new JTextArea(18,40);
	private JScrollPane js = new JScrollPane(ta);
	BufferedImage Map = new BufferedImage(MAP_SIZE_X,MAP_SIZE_Y,BufferedImage.TYPE_INT_RGB);
	Graphics g = Map.getGraphics();
	private MapCanvas mapArea = new MapCanvas();
	static int pos_x[]= {100,220,280,160,40,340,280,40,100,160,280,340,40,160,220,340,40,100,160,220,280,340,40,100,220,280,40,100,160,220,280,340,100,160,220,340};
	static int pos_y[]= {100,40,100,220,340,220,340,40,40,40,40,40,100,100,100,100,160,160,160,160,160,160,220,220,220,220,280,280,280,280,280,280,340,340,340,340};
	public void init()throws Exception
	{

		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
	    ImageIcon img = new ImageIcon("res\\white.jpg");//这是背景图片
	    JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
	    f.getLayeredPane().add(imgLabel, Integer.MIN_VALUE);//将背景标签添加到JFrame的LayeredPane面板里。
	    imgLabel.setBounds(0,0,img.getIconWidth(), img.getIconHeight());//设置背景标签的位置
	    JPanel jp = (JPanel) f.getContentPane();
	    JRootPane jrp = (JRootPane) f.getRootPane();
	    jp.setOpaque(false);
	    jrp.setOpaque(false);
	    
		drawMap();
		f.setLayout(gb);
		NPanel top = new NPanel();
		top.add(mapArea);
		NPanel middle = new NPanel();
		guide.addActionListener(e->guideAction());
		guide.addKeyListener(new EnterAdapter());
		begin.addKeyListener(new EnterAdapter());
		end.addKeyListener(new EnterAdapter());
		crowd.addKeyListener(new EnterAdapter());
		
		crowd.addItemListener(new NCheckBoxlistener()) ;
		
		begin.setHorizontalAlignment(SwingConstants.CENTER);
		end.setHorizontalAlignment(SwingConstants.CENTER);
		//begin.setBorder(new RoundBorder(new Color(15,143,234)));
		
		guide.setBorderPainted(false); // 消除边框
		guide.setContentAreaFilled(false); // 消除内容域，即中间的那一块
		crowd.setBorderPainted(false); // 消除边框
		crowd.setContentAreaFilled(false); // 消除内容域，即中间的那一块
		
		js.getVerticalScrollBar().setUI(new NScrollBar());
		js.getHorizontalScrollBar().setUI(new NScrollBar());
		js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		js.setBorder(new EmptyBorder(0, 0, 0, 0));

		middle.add(b);
		middle.add(begin);
		middle.add(e);
		middle.add(end);
		middle.add(crowd);
		middle.add(guide);
        //processBar.setStringPainted(true);
	    //processBar.setBackground(Color.lightGray);
	    NPanel buttom = new NPanel();
	    buttom.add(js);
	    gbc.gridwidth = GridBagConstraints.REMAINDER;
	    gb.setConstraints(top, gbc);
	    f.add(top);
	    gb.setConstraints(middle,gbc);
	    f.add(middle);
	    //gbc.weightx = 2;
	    //gb.setConstraints(processBar,gbc);
		//f.add(processBar);
		f.add(buttom);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void guideAction() {
		try{if(Integer.valueOf(begin.getText()).intValue()<=7||Integer.valueOf(begin.getText()).intValue()>=35||Integer.valueOf(end.getText()).intValue()<=7||Integer.valueOf(end.getText()).intValue()>=35||Integer.valueOf(begin.getText()).intValue()==Integer.valueOf(end.getText()).intValue()) {
			ta.append("输入的起点和终点不符合要求\n");
			breakLine();
			}
		else {
			Charge.main(Integer.valueOf(begin.getText()).intValue(),Integer.valueOf(end.getText()).intValue());
			drawPath();
			drawYongdu(crowd.isSelected());
		}}catch(NumberFormatException exc) {
			ta.append("请在起点和终点文本框输入正确的起点或终点\n");
			breakLine();
		}
	}
	public void drawMap() {
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, MAP_SIZE_X, MAP_SIZE_Y);
		g.setColor(new Color(255,0,0));
		g.fillOval((43-3), (359-3), 6, 6);
		g.drawString("充电站，其余角点为路网节点", 53, 363);
		//g.setColor(new Color(0,0,255));
		//g.drawString("蓝线", 40, 383);
		//g.setColor(new Color(0,255,0));
		//g.drawString("绿线", 40, 403);
		g.setColor(new Color(0,0,0));
		mapArea.setPreferredSize(new Dimension(MAP_SIZE_X, MAP_SIZE_Y));
		for(int i=0; i<BOARD_SIZE; i++)
		{
			g.drawLine(40, i*60+40, 340, i*60+40);
			g.drawLine(i*60+40, 40, i*60+40, 340);
		}
		for(int i=0;i<Charge.D;i++)
			g.drawString(i+" ", (pos_x[i]+4), (pos_y[i]-4));
		//g.drawString("为充电站，其余为路网节点", 65, 363);
		//g.drawString("为起点到充电站导航路线", 65, 383);
		//g.drawString("为充电站到终点导航路线", 65, 403);
		g.setColor(new Color(255,0,0));
		for(int i=0;i<Charge.C;i++)
			g.fillOval((pos_x[i]-3), (pos_y[i]-3), 6, 6);
		mapArea.repaint();
	}
	public void drawYongdu(Boolean b) {
		if(b==true) {
			g.setColor(new Color(255,38,0));
			for(int i = 0;i<Charge.D;i++)
				for(int j = 0;j<Charge.D;j++)
					if(Charge.yongdu_edge[i][j]==2) {
						g.drawLine(pos_x[i], pos_y[i], pos_x[j], pos_y[j]);
						g.drawLine(pos_x[i]-1, pos_y[i]-1, pos_x[j]-1, pos_y[j]-1);
					}
			g.drawLine(265, 378, 288, 378);
			g.drawLine(265, 377, 288, 377);
			g.drawString("非常拥堵", 293, 383);
			g.setColor(new Color(255,187,0));
			for(int i = 0;i<Charge.D;i++)
				for(int j = 0;j<Charge.D;j++)
					if(Charge.yongdu_edge[i][j]==1) {
						g.drawLine(pos_x[i], pos_y[i], pos_x[j], pos_y[j]);
						g.drawLine(pos_x[i]-1, pos_y[i]-1, pos_x[j]-1, pos_y[j]-1);
					}
			g.drawString("拥堵", 293, 403);
			g.drawLine(265, 398, 288, 398);
			g.drawLine(265, 397, 288, 397);
			mapArea.repaint();
		}
	}
	public void drawPath() {
		g.clearRect(0,0, MAP_SIZE_X,MAP_SIZE_Y);
		drawMap();
		g.setColor(new Color(0,0,255));
		g.drawString("起点",(pos_x[Integer.valueOf(begin.getText()).intValue()]-25),(pos_y[Integer.valueOf(begin.getText()).intValue()]-5));
		g.drawLine((pos_x[Integer.valueOf(begin.getText()).intValue()]-1),(pos_y[Integer.valueOf(begin.getText()).intValue()]-1),(pos_x[Charge.begtocharge[0]]-1),( pos_y[Charge.begtocharge[0]]-1));
		for(int i=1;i<(Charge.D);i++){
			if(Charge.begtocharge[i]==Charge.INF)
				continue;
			g.drawLine((pos_x[Charge.begtocharge[i-1]]-1), (pos_y[Charge.begtocharge[i-1]]-1),(pos_x[Charge.begtocharge[i]]-1),( pos_y[Charge.begtocharge[i]]-1));
		}
		g.drawLine(40, 378, 62, 378);
		g.drawLine(40, 377, 62, 377);
		g.drawString("起点到充电站导航路线", 67, 383);
		
		g.setColor(new Color(0,255,0));
		g.drawString("终点",(pos_x[Integer.valueOf(end.getText()).intValue()]-25),(pos_y[Integer.valueOf(end.getText()).intValue()]-5));
		g.drawLine((pos_x[Charge.g[0]]+1),(pos_y[Charge.g[0]]+1),(pos_x[Charge.chargetodes[0]]+1), (pos_y[Charge.chargetodes[0]]+1));
		for(int i=1;i<(Charge.D);i++) {
			if(Charge.chargetodes[i]==Charge.INF)
				continue;
			g.drawLine((pos_x[Charge.chargetodes[i-1]]+1),( pos_y[Charge.chargetodes[i-1]]+1),(pos_x[Charge.chargetodes[i]]+1),( pos_y[Charge.chargetodes[i]]+1));
		}
		g.drawLine(40, 398, 62, 398);
		g.drawLine(40, 397, 62, 397);
		g.drawString("充电站到终点导航路线", 67, 403);
		mapArea.repaint();
	}
	
	public static void breakLine() {
			int i = 1+ (new Random().nextInt(10));
			switch (i){
			case 1:ta.append("·······································～(￣▽￣～)~·······································\n"); break;
			case 2:ta.append("··········································(≧∇≦)/··········································\n"); break;
			case 3:ta.append("···········································(ง ˙o˙)ว··········································\n"); break;
			case 4:ta.append("·········································*<(¦Q[▓▓·········································\n"); break;
			case 5:ta.append("·········································_(:з」∠)_·········································\n"); break;
			case 6:ta.append("···········································(›´ω`‹ )···········································\n"); break;
			case 7:ta.append("··········································Σ(ﾟдﾟ；)··········································\n"); break;
			case 8:ta.append("··········································(⊙△⊙)？·········································\n"); break;
			case 9:ta.append("········································щ(゜ロ゜щ)········································\n"); break;
			case 10:ta.append("········································┌( ⊙_⊙)┘········································\n"); break;
			}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new UI().init();
	}
	
	
	/*class WinListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	}*/
	
	class EnterAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent k) {
		if(k.getKeyChar()==KeyEvent.VK_ENTER)
			guideAction();
		}
	}
	class MapCanvas extends Canvas
	{
		public void paint (Graphics g)
		{
			g.drawImage(Map,0,0,null);
		}
	}
	class NCheckBoxlistener implements ItemListener{
		private String right = "src\\icon\\right.png" ;
		private String wrong = "src\\icon\\wrong.png" ;
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			JCheckBox jcb = (JCheckBox)e.getItem() ;
		      {
		         if(jcb.isSelected())
		         {
		            jcb.setIcon(new ImageIcon(right)) ;
		         }else jcb.setIcon(new ImageIcon(wrong)) ;
		      }
		}
	}
}
