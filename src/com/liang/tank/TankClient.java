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
 * ������������̹����Ϸ��������
 * @author liangyongfei
 *
 */
public class TankClient extends Frame {
	/**
	 * ����̹����Ϸ�Ŀ��
	 */
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	Wall w1 = new Wall(300,200,20,150,this), w2 = new Wall(300,100,300,20,this);
	
	Tank myTank = new Tank(50, 50,Direction.STOP, true, this);
	List<Tank> tanks = new ArrayList<Tank>();
	//Tank enemyTank = new Tank(100, 100, false, this);
	
	/*��������װ�ڵ�:�õ��˷���*/
	List<Missile> missiles = new ArrayList<Missile>();
	//Missile m = null;
	List<Explode> explodes = new ArrayList<Explode>();
	//Explode e = new Explode(70,70,this);
	
	Blood b = new Blood();
	Image offScreenImage = null;/*ʹ��˫����������˸���󣬿������Ϊ����������Ļ�ϰ�ͼ�񻭳����������һ������ʾ����ʵ��Ļ��*/
	
	/*Graphics �������Ϊ��ͼ��Ļ���*/
	public void paint (Graphics g) {
		/*ָ���ӵ�-��ը-̹�˵������Լ�̹�˵�����ֵ*/
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
		/*��̹��*/
		myTank.draw(g);
		myTank.eatBlood(b);
		//enemyTank.draw(g);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}
	
	public void update (Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);/*����һ��ͼƬ����Ϊ����ͼƬ���������С����ʵͼƬ��Сһ��*/
		}
		Graphics goffScreen = offScreenImage.getGraphics();/*�������ͼƬ�Ļ���*/
		
		/*ˢ�±���ǽ����ֹ��������һ���ߣ�ǰ����Ĺ켣���ڣ�û����ʧ��ˢ��֮����ǰ�Ĺ켣��û����*/
		Color c = goffScreen.getColor();
		goffScreen.setColor(Color.BLACK);
		goffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		goffScreen.setColor(c);
		
		paint(goffScreen);/*����paint()������������ͼƬ�ϻ�������ͼ��*/
		g.drawImage(offScreenImage,0, 0, null);/*Ȼ������ʵ��ͼƬ�ϻ�����*/
		
	}
	/**
	 * ��������ʾ̹��������
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
		this.setBackground(Color.BLACK);/*����ɫ*/
		
		this.addKeyListener(new KeyMonitor());/*���Ӽ��̼���������*/
		
		setVisible(true);
		new Thread (new PaintThread()).start(); 
	} 
	
	public static void main(String[] args) {
		
		TankClient tc = new TankClient();
		tc.lanuchFrame();
	}
	
	/*�ڲ��߳��࣬ר�������ػ�TankClient�����*/
	private class PaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();/*���ȵ���update������update�����ٵ���paint�����������ػ�TankClient���е�paint()����*/
				try {
					Thread.sleep(100);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/*���̼�����*/
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


