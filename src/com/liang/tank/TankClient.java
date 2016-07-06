package com.liang.tank;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;


/**
 * 这个类的作用是坦克游戏的主窗口
 * @author liangyongfei
 *
 */
public class TankClient extends Frame {
	/**
	 * 整个坦克游戏的宽度
	 */
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	Wall w1 = new Wall(300,200,20,150,this), w2 = new Wall(300,100,300,20,this);
	
	Tank myTank = new Tank(50, 50,Direction.STOP, true, this);
	List<Tank> tanks = new ArrayList<Tank>();
	//Tank enemyTank = new Tank(100, 100, false, this);
	
	/*用容器来装炮弹:用到了泛型*/
	List<Missile> missiles = new ArrayList<Missile>();
	//Missile m = null;
	List<Explode> explodes = new ArrayList<Explode>();
	//Explode e = new Explode(70,70,this);
	
	Blood b = new Blood();
	Image offScreenImage = null;/*使用双缓冲消除闪烁现象，可以理解为先在虚拟屏幕上把图像画出来，最后再一次性显示在真实屏幕上*/
	
	/*Graphics 可以理解为画图像的画笔*/
	public void paint (Graphics g) {
		/*指明子弹-爆炸-坦克的数量以及坦克的生命值*/
		g.drawString("missiles count: " + missiles.size(), 10, 50);
		g.drawString("explodes count: " + explodes.size(), 10, 70);
		g.drawString("tanks count: " + tanks.size(), 10, 90);
		g.drawString("tanks  life: " + myTank.getLife(), 10, 110);
		
		if(tanks.size() <= 0) {
			int rePaintTankCount = Integer.parseInt(PropertyMgr.getProperty("rePaintTankCount"));
			for(int i = 0; i < rePaintTankCount; i++) {
				tanks.add(new Tank(50+40*(i+1), 50, Direction.D, false, this)); 
			}
		}
		//if(m != null) m.draw(g);
		for(int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			//m.hitTank(enemyTank);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
			/*if(!m.isbLive()) missiles.remove(m);
			else m.draw(g);*/
		}
		
		for(int i = 0; i < explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//e.draw(g);
		
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		/*画坦克*/
		myTank.draw(g);
		myTank.eatBlood(b);
		//enemyTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}
	
	public void update (Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);/*创建一张图片，作为虚拟图片，设置其大小跟真实图片大小一样*/
		}
		Graphics goffScreen = offScreenImage.getGraphics();/*获得虚拟图片的画笔*/
		
		/*刷新背景墙：防止球下落变成一条线，前面球的轨迹还在，没有消失。刷新之后，以前的轨迹就没有了*/
		Color c = goffScreen.getColor();
		goffScreen.setColor(Color.BLACK);
		goffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		goffScreen.setColor(c);
		
		paint(goffScreen);/*调用paint()方法，在虚拟图片上画出所有图像*/
		g.drawImage(offScreenImage,0, 0, null);/*然后在真实的图片上画出来*/
		
	}
	/**
	 * 本方法显示坦克主窗口
	 */
	public void lanuchFrame () {
		
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		for(int i = 0; i < initTankCount; i++) {
			tanks.add(new Tank(50+40*(i+1), 50, Direction.D, false, this)); 
		}
		
		//this.setLocation(400, 300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}   
		});
		this.setResizable(false);
		this.setBackground(Color.BLACK);/*背景色*/
		
		this.addKeyListener(new KeyMonitor());/*增加键盘监听器对象*/
		
		setVisible(true);
		new Thread (new PaintThread()).start(); 
	} 
	
	public static void main(String[] args) {
		
		TankClient tc = new TankClient();
		tc.lanuchFrame();
	}
	
	/*内部线程类，专门用于重画TankClient这个类*/
	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();/*首先调用update方法，update方法再调用paint（）方法：重画TankClient类中的paint()方法*/
				try {
					Thread.sleep(100);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/*键盘监听类*/
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
	}
	
	
}


