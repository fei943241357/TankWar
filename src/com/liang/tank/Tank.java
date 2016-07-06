package com.liang.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;


public class Tank {
	
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	/*���жԷ�����,�ͺ������ع��췽�����ʹ��*/
	TankClient tc;
	/*���ֵ���̹�˱�־*/
	private boolean good = true;
	
	private boolean live = true;
	
	private BloodBar bb = new BloodBar();
	private int life = 100;
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}

	private int x,y;
	/*��¼��һλ��*/
	private int oldX,oldY;
	
	/*�����������*/
	private static Random r = new Random();
	
	/*̹�˷����־*/
	private boolean bL = false, bU = false, bD = false, bR = false;
	
	
	
	
	private  Direction dir = Direction.STOP;/*̹�˷���*/
	private  Direction ptDir = Direction.D;/*̹����Ͳ����*/
	
	/*��¼ÿһ��̹�ˣ����ˣ���ǰ�ƶ��Ĳ���*/
	private int step = r.nextInt(12) + 3;
	
	/*��ȡĬ�Ϲ��߰�*/
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] tankImages = null;
	private static  Map<String, Image> imgs = new HashMap<String, Image>();
	
	/*��̬���������������load֮������ִ����δ��롣���ʺϸ�һЩ��������ʼ������*/
	static  {
		tankImages = new Image[] {
		/*��ȡͼ���õ��˷��� */
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
		};
		/*��ͼƬ�ŵ�Map��*/
		imgs.put("L", tankImages[0]);
		imgs.put("LU", tankImages[1]);
		imgs.put("U", tankImages[2]);
		imgs.put("RU", tankImages[3]);
		imgs.put("R", tankImages[4]);
		imgs.put("RD", tankImages[5]);
		imgs.put("D", tankImages[6]);
		imgs.put("LD", tankImages[7]);
	}
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}
	
	public boolean isGood() {
		return good;
	}

	

	/*���ع��췽��*/
	public Tank(int x, int y, Direction dir,boolean good, TankClient tc) {
		this(x,y,good);
		this.dir = dir;
	    this.tc = tc;
	}
	
	public void draw(Graphics g)  {
		/*���̹�����˾Ͳ���*/
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		/*����Ǻ�̹�ˣ�����Ѫ��*/
		if(good) bb.draw(g);
		
		/*������Ͳ���򣬻���Ͳ*/
		switch(ptDir) {
		case L:
			g.drawImage(imgs.get("L"),x, y,null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"),x, y,null);
			break;
		case U:
			g.drawImage(imgs.get("U"),x, y,null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"),x, y,null);
			break;
		case R:
			g.drawImage(imgs.get("R"),x, y,null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"),x, y,null);
			break;
		case D:
			g.drawImage(imgs.get("D"),x, y,null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"),x, y,null);
			break;
		}
		move();
	}
	
	void move () {
		
		/*û�ƶ�֮ǰ����¼���ڵ�x,yλ��*/
		this.oldX = x;
		this.oldY = y;
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP ){
			this.ptDir = this.dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		
		if(!good) {
			Direction[] dirs = Direction.values();/*������ת����һ������*/
			/*�ƶ���һ���������֮����������һ��������������ı�һ�η���*/
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);/*����һ����0��8������ĸ�����������*/
				dir = dirs[rn];
			}
			step--;
			/*�з�̹�˴���ӵ�������һ������������������38���򷢳�һ���ӵ�*/
			if(r.nextInt(40) > 38) this.fire();
		}
	}
	
	/*���¼�*/
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if(!this.live) {
				this.live  = true;
				this.life  = 100;
			}
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		
		locateDirection();
	}
	
	/*�ͷż�*/
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		/*̧��CTRL���ͷ�һ���ӵ�:Ŀ���Ƿ�ֹ�ӵ�������̫�죨�������CTRL���������ӵ����ӵ�����̫�죩*/
		case KeyEvent.VK_CONTROL:
		    fire();
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		/*����A����������ڵ�*/
		case KeyEvent.VK_A:
			superFire();;
			break;
		}
		
		locateDirection();
	}
	
	void locateDirection() {
		if(bL && !bR && !bU && !bD) dir = Direction.L;
		else if(bL && !bR &&  bU && !bD) dir = Direction.LU;
		else if(!bL && !bR && bU && !bD) dir = Direction.U;
		else if(!bL && bR &&  bU && !bD) dir = Direction.RU;
		else if(!bL && bR && !bU && !bD) dir = Direction.R;
		else if(!bL && bR && !bU &&  bD) dir = Direction.RD;
		else if(!bL && !bR && !bU && bD) dir = Direction.D;
		else if(bL  && !bR && !bU && bD) dir = Direction.LD;
		else if(!bL &&!bR && !bU && !bD) dir = Direction.STOP;
	}
	
	/*̹�˷����һ���ӵ�������ֵΪ�ӵ���������̹�������湹���������*/
	public Missile fire() {
		/*������ˣ��Ͳ��ܷ����ӵ�*/
		if(!live) return null;
		
		/*�ӵ����м�����*/
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good, ptDir,this.tc);
		tc.missiles.add(m);/*��������һ���࣬�ʹ�����������List�ӿ���*/
		return m;
	}
	/*����ָ��������һ���ڵ�*/
	public Missile fire (Direction dir) {
		if(!live) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good, dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i = 0; i < 8; i++) {
			fire(dirs[i]);/*��8���������һ��*/
		}
	}
	public Rectangle  getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public void stay() {
		this.x = this.oldX;
		this.y = this.oldY;
	}
	/**
	 * 
	 * @param w��ײ��ǽ
	 * @returnײ���˷���true,���򷵻�false
	 */
	public boolean collidesWithWall(Wall w){
		if(this.getRect().intersects(w.getRect())){
			this.stay();/*ײǽ֮��tankͣס�ˣ���x,y����ԭ����oldX,oldY,��ʱ̹����ûײ��ǽ�ģ�Ȼ��������ı䣬̹�˾�������*/
			return true;
		}
		return false;
	}
	
	/*����������̹����ײ�����ײ���ˣ���Ҷ�ͣס*/
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for(int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live &&this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
					t.stay();
					this.stay();
					return true;
				}
			}
		}
		return false;
	}
	/*������Ѫ��*/
	private class BloodBar{
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w = WIDTH * life/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}
	
	public boolean eatBlood(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}
}

