package com.liang.tank;
import java.awt.*;
import java.awt.Image;
import java.awt.Toolkit;

public class Explode {
	
	int x, y;
	private boolean live = true;
	
	private TankClient tc;
	
	/*获取默认工具包*/
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] imgs = {
		/*获取爆炸图像：用到了反射 */
		tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
	};
	
	//int[] diameter = {4,7,12,18,26,32,49,30,14,6};
	int step = 0;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == imgs.length) {
			live = false;
			step = 0;
			return;
		}
		/*绘制指定图像中当前可用的图像*/ 
		g.drawImage(imgs[step], x, y, null);
		
		step++;
	}
}
