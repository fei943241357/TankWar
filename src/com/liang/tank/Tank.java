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
	/*持有对方引用,和后面重载构造方法配合使用*/
	TankClient tc;
	/*区分敌我坦克标志*/
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
	/*记录上一位置*/
	private int oldX,oldY;
	
	/*随机数产生器*/
	private static Random r = new Random();
	
	/*坦克方向标志*/
	private boolean bL = false, bU = false, bD = false, bR = false;
	
	
	
	
	private  Direction dir = Direction.STOP;/*坦克方向*/
	private  Direction ptDir = Direction.D;/*坦克炮筒方向*/
	
	/*记录每一辆坦克（敌人）当前移动的步数*/
	private int step = r.nextInt(12) + 3;
	
	/*获取默认工具包*/
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] tankImages = null;
	private static  Map<String, Image> imgs = new HashMap<String, Image>();
	
	/*静态代码区：当这个类load之后，最先执行这段代码。最适合给一些变量做初始化工作*/
	static  {
		tankImages = new Image[] {
		/*获取图像：用到了反射 */
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
		tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
		};
		/*把图片放到Map中*/
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

	

	/*重载构造方法*/
	public Tank(int x, int y, Direction dir,boolean good, TankClient tc) {
		this(x,y,good);
		this.dir = dir;
	    this.tc = tc;
	}
	
	public void draw(Graphics g)  {
		/*如果坦克死了就不画*/
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		/*如果是好坦克，画出血条*/
		if(good) bb.draw(g);
		
		/*根据炮筒方向，画炮筒*/
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
		
		/*没移动之前，记录现在的x,y位置*/
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
			Direction[] dirs = Direction.values();/*将方向转换成一个数组*/
			/*移动了一个随机步数之后：重新生成一个随机数步数，改变一次方向*/
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);/*产生一个从0到8（方向的个数）的整数*/
				dir = dirs[rn];
			}
			step--;
			/*敌方坦克打出子弹：设置一个随机数，如果它大于38，则发出一个子弹*/
			if(r.nextInt(40) > 38) this.fire();
		}
	}
	
	/*按下键*/
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
	
	/*释放键*/
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		/*抬起CTRL键释放一颗子弹:目的是防止子弹发出来太快（如果按下CTRL键发出来子弹，子弹出来太快）*/
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
		/*按了A键打出超级炮弹*/
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
	
	/*坦克发射出一颗子弹，返回值为子弹：所以在坦克类里面构造这个方法*/
	public Missile fire() {
		/*如果死了，就不能发出子弹*/
		if(!live) return null;
		
		/*子弹从中间打出来*/
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good, ptDir,this.tc);
		tc.missiles.add(m);/*创建出了一个类，就存起来，放在List接口中*/
		return m;
	}
	/*按照指定方向打出一发炮弹*/
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
			fire(dirs[i]);/*朝8个方向各打一发*/
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
	 * @param w被撞的墙
	 * @return撞上了返回true,否则返回false
	 */
	public boolean collidesWithWall(Wall w){
		if(this.getRect().intersects(w.getRect())){
			this.stay();/*撞墙之后tank停住了：让x,y等于原来的oldX,oldY,此时坦克是没撞着墙的；然后方向随机改变，坦克就又跑了*/
			return true;
		}
		return false;
	}
	
	/*检测和其他的坦克相撞：如果撞到了，大家都停住*/
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
	/*画生命血条*/
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

