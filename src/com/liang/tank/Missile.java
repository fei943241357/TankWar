package com.liang.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
	
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	
	Direction dir;
	private boolean good;
	private TankClient tc;
	
private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] missileImages = null;
	private static  Map<String, Image> imgs = new HashMap<String, Image>();
	
	/*静态代码区：当这个类load之后，最先执行这段代码。最适合给一些变量做初始化工作*/
	static  {
		missileImages = new Image[] {
		/*获取图像：用到了反射 */
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileL.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileLU.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileU.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileRU.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileR.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileRD.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileD.gif")),
		tk.getImage(Missile.class.getClassLoader().getResource("images/MissileLD.gif"))
		};
		/*把图片放到Map中*/
		imgs.put("L", missileImages[0]);
		imgs.put("LU", missileImages[1]);
		imgs.put("U", missileImages[2]);
		imgs.put("RU", missileImages[3]);
		imgs.put("R", missileImages[4]);
		imgs.put("RD", missileImages[5]);
		imgs.put("D", missileImages[6]);
		imgs.put("LD", missileImages[7]);
	}
	
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		/*先判断子弹是不是活着，如果没活着，就不用画了*/
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		switch(dir) {
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
	
private void move() {
		
		
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
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}		
	}
	
	
	/*通过子弹的位置，返回一个矩形方块*/
	public Rectangle  getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public boolean hitTank(Tank t){
		/*碰撞检测:要满足这个坦克是活的前提*/
		if(this.live &&this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()){
				t.setLife(t.getLife()-20);
				if(t.getLife() <= 0) t.setLive(false);
				
			}else {
				t.setLive(false);
			}
			
			this.live = false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0; i < tanks.size(); i++) {
			if(hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.getRect().intersects(w.getRect())){
			this.live = false;
			return true;
		}
		return false;
		
	}
}
