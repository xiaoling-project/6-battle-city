package cn.vizdl.MyTankGame3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * 功能:坦克游戏3.0版 
 * 独自完成版
 * 1.画出坦克
 * 2.我的坦克可以上下左右移动(正方形的坦克方便改变坦克形态)
 * 3.我的坦克可以发射多个子弹(并具有一共只能在屏幕存在五个子弹的限制)
 * 4.我的坦克可以"杀死"敌军坦克
 * 5.敌军坦克可以发出子弹,并且能击杀我方坦克(如若击中的话)
 * 6.无论是我军还是敌军，坦克被击中后会发生爆炸(存在第一个坦克不爆炸问题,无论第一个是我方还是敌方坦克)
 * 7.使得敌军能够随机移动
 * 8.增加坦克碰撞检测,不允许坦克能重叠 (我方坦克暂时未实现)
 * 9.可以选择关卡(*)
 * 	思路 : 做一个空面板用于提示
 * 
 * 
 * BUG:
 * 1.死亡后仍然能发一颗子弹问题（可以设置为故事情节》 坦克亡魂不死心 要发出最后一个子弹才肯死亡？）(通过修改run解决)
 * 2.两个坦克相撞会消失一个,并且后续会发生指针错误(好像是因为三个坦克在一起,如若是两个就不会。
 * 		这是因为stop功能(取消stop,如若无处可走就往其他坦克身上撞,其他坦克会让路)
 * 
 * 问题要及时解决 否则很难再修改过来！
 * 
 * 修复:
 * 1.修复了子弹只有一个问题
 * @author Vizdl
 *
 */

//窗口类
public class MyTankGame4 extends JFrame{
	//对象指针初始化
	MyPanel mp = null;
	//构建函数初始化
	public MyTankGame4(){
		//指针初始化
		mp = new  MyPanel();
		
		//添加组件
		this.add(mp);
		
		//添加监听
		this.addKeyListener(mp); //将该窗口监听者设置为面板
		
		//启动画板线程（用于重绘）
		Thread t = new Thread (mp);
		t.start();
		
		//窗口设置
		this.setTitle("TankGame");
		this.setSize(415, 338);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//主函数
	public static void main (String[] args) {
		new MyTankGame4();
	}
}

//我的开始面板
class MyStartPanel extends JPanel {
	//重写paint方法
	public void paint (Graphics g){
		g.fillRect(0, 0, 400, 300);
	}
}


//画板组件
	//画板组件需要有监听功能 这样方便直接通过接收信息更改坦克对象的位置 方向 与颜色
	//画板线程
class MyPanel extends JPanel implements KeyListener,Runnable{
	//对象指针初始化
	MyTank mt = null;
		//敌方坦克不止一辆 所以得定义 敌军坦克数量 这一变量 并且 敌军坦克得使用链表来实现成为一个集合
	//定义敌人坦克组
	Vector<HostileTank> hts = new Vector<HostileTank>();
	//定义坦克数量
	int htNumber = 8;
	
	//构造函数初始化
	public MyPanel() {
		//对象名认取
		mt = new MyTank(100,200);
		
		for (int i = 0; i < htNumber; i++) {
			//创建一个敌方坦克对象
			HostileTank newHt = null;
			if (i < 8) {
				newHt = new HostileTank(i*50,0,HostileTank.DOWM,HostileTank.GREEN,3);
			}else {
				int temp = i % 6;
				newHt = new HostileTank(i*50,temp * 50,HostileTank.LEFT,HostileTank.YELLOW,3);
			}
			newHt.setHts(hts);
			newHt.setMt(mt);
			//启动敌军线程
			Thread t = new Thread(newHt);
			t.start();
			
			//将敌方坦克对象加入敌方组
			hts.add(newHt);
		}
	}
	
	//重写paint方法
		//paint是画图区域
	public void paint (Graphics g) {
		// 背景
		g.fillRect(0, 0, 1800, 300);

		// 我的坦克绘制
		if (mt.isAlive == true) {
			mt.drawTank(g);
		}
		
		//我方爆炸绘制
		if (!mt.isAlive && mt.bomb.isAlive) {
			//设置爆炸地点
			mt.bomb.setX(mt.getX());
			mt.bomb.setY(mt.getY());
			//画炸弹
			mt.bomb.drawBomb(g, this);
			if (mt.bomb.bloodValue >= 16) {
				mt.bomb.isAlive = false;
			}
		}

		// 敌方坦克绘制
		for (int i = 0; i < htNumber; i++) {
			HostileTank ht = hts.get(i);
			if (ht.isAlive) {
				ht.drawTank(g);
			}
			
			//坦克炸弹绘制
			else if (!ht.isAlive && ht.bomb.isAlive) {
				//设置爆炸地点
				ht.bomb.setX(ht.getX());
				ht.bomb.setY(ht.getY());
				
				ht.bomb.drawBomb(g, this);
				if (ht.bomb.bloodValue < 0){
					ht.bomb.isAlive = false;
				}
			}
			
			//绘制敌军子弹
			for (int k = 0; k < ht.Bullets.size(); k++) {
				Bullet bu = ht.Bullets.get(k);
				if (bu != null && bu.isAlive) {
					bu.drawBullet(g);
				}
				// 已"死"子弹清理
				if (bu.isAlive == false) {
					ht.Bullets.remove(bu);	//为什么这样不会发生数组角标越界问题？
				}
			}
		}

		// 通过遍历画我的子弹（多个）
		for (int i = 0; i < mt.Bullets.size(); i++) {
			Bullet bu = mt.Bullets.get(i);
			if (bu != null && bu.isAlive) {
				bu.drawBullet(g);
			}
			// 已"死"子弹清理
			if (bu.isAlive == false) {
				mt.Bullets.remove(bu);	//为什么这样不会发生数组角标越界问题？
			}
		}
	}
	
	//检测 这个子弹 与这个坦克 是否会发生碰撞 如果发生 则 子弹与坦克同归于尽
	public void hitTank (Tank tank,Bullet bullet) {
		//如若发生碰撞
		if (bullet.x >= tank.x && bullet.x < (tank.x + 30) && bullet.y >= tank.y && bullet.y <= (tank.y + 30)){
			//坦克死亡
			tank.isAlive = false;
			//子弹死亡
			bullet.isAlive = false;
		}
	}
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		// 更改信息
		if (arg0.getKeyCode() == KeyEvent.VK_W && mt.getY() > 1) {
			mt.setTankDirection(Tank.UP);
			mt.moveUp();
		} else if (arg0.getKeyCode() == KeyEvent.VK_S && mt.getY() < 269) {
			mt.setTankDirection(Tank.DOWM);
			mt.moveDown();
		} else if (arg0.getKeyCode() == KeyEvent.VK_A && mt.getX() > 1) {
			mt.setTankDirection(Tank.LEFT);
			mt.moveLeft();
		} else if (arg0.getKeyCode() == KeyEvent.VK_D && mt.getX() < 369) {
			mt.setTankDirection(Tank.RIGHT);
			mt.moveRight();
		}

		if (arg0.getKeyCode() == KeyEvent.VK_J && mt.isAlive) {
			if (mt.Bullets.size() <= 4) {
				mt.creatBullet();
			}
		}

		//刷新图画
		this.repaint();
	}

	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		while(true) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(100);
				//每隔一点时间就刷新
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//判断所有敌方坦克坦克和我方子弹是否发生有效碰撞
			for (int i = 0; i < htNumber; i++) {
				//取出坦克
				HostileTank ht = hts.get(i);
				//判断坦克是否有效
				if (ht.isAlive) {
					for (int j = 0; j < mt.Bullets.size(); j++) {
						//取出子弹
						Bullet bu = mt.Bullets.get(j);
						//判断子弹是否有效
						if (bu.isAlive) {
							this.hitTank(ht, bu);
						}
					}
				}
			}
			
			//判断我方坦克与敌方子弹是否会发生碰撞
			for (int i = 0; i < htNumber; i++) {
				//取出坦克
				HostileTank ht = hts.get(i);
				//即使敌方坦克无效,即使地方坦克子弹有效,那么就行
				for (int j = 0; j < ht.Bullets.size(); j++) {
					//取出敌方坦克的子弹
					Bullet bu = ht.Bullets.get(j);
					//判断子弹和我方坦克是否有效
					if (bu.isAlive && mt.isAlive) {
						this.hitTank(mt, bu);
//						System.out.println("OK");
					}
				}
			}
			
			this.repaint();
		}
	}

}


//所有坦克的共性放在Tank类内
class Tank{
	//常数
		//方向
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
		//颜色
			//我方坦克专用颜色
	public static final int RED = 10;
			//敌方坦克颜色
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;
	
	
	//坦克存活问题
	boolean isAlive = true;
	
	//每个坦克都带有爆炸效果的"Bomb"并且只能带一个
	Bomb bomb = null;
	
	//坦克坐标
	int x;
	int y;
	//坦克方向
	int tankDirection;
	//坦克颜色
	int tankColor;
	// 坦克速度
	int tankSpeed;

	// 坦克的子弹
	Vector<Bullet> Bullets = new Vector<Bullet>();

	// 创建子弹
	public void creatBullet() {
		// bu 不应该是类成员,不能被调用。
		Bullet bu = null;
		// 根据坦克方向不同，坦克子弹出现初始位置也不同
		switch (this.tankDirection) {
		case MyTank.UP:
			bu = new Bullet(this.x + 14, this.y - 11, this.tankColor, Bullet.NORMAL, this.tankDirection);
			break;
		case MyTank.DOWM:
			bu = new Bullet(this.x + 14, this.y + 41, this.tankColor, Bullet.NORMAL, this.tankDirection);
			break;
		case MyTank.LEFT:
			bu = new Bullet(this.x - 11, this.y + 14, this.tankColor, Bullet.NORMAL, this.tankDirection);
			break;
		case MyTank.RIGHT:
			bu = new Bullet(this.x + 41, this.y + 14, this.tankColor, Bullet.NORMAL, this.tankDirection);
			break;
		}
		Bullets.add(bu);
		Thread t = new Thread(bu);
		t.start();
	}

	// 画坦克 应该是静态类么？ 不应该 不同对象画不同坦克 没有坦克对象不允许画坦克 一个坦克对象画一个坦克？
	// 既然一个对象制造一个坦克 并且坦克对象内部拥有所有的参数，那么画坦克久不需要传入多余的参数！
	public void drawTank(Graphics g) {
		switch (this.tankColor) {
		case Tank.RED:
			g.setColor(Color.red);
			break;
		case Tank.BLUE:
			g.setColor(Color.blue);
			break;
		case Tank.YELLOW:
			g.setColor(Color.yellow);
			break;
		case Tank.WHITE:
			g.setColor(Color.WHITE);
			break;
		case Tank.GREEN:
			g.setColor(Color.GREEN);
			break;
		}
		switch (this.tankDirection) {
		//炮口向上
		case Tank.UP:
			//画左边的矩形 可以切割为多个矩形形成履带
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x, this.y+5, 5, 5, false);
			g.fill3DRect(this.x, this.y+10, 5, 5, false);
			g.fill3DRect(this.x, this.y+15, 5, 5, false);
			g.fill3DRect(this.x, this.y+20, 5, 5, false);
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			//画右边的矩形
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+5, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+10, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+15, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+20, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//画中间的正方体
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//画中间的圆
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//画中间的线
			g.drawLine(this.x+15, this.y+15, this.x+15, this.y-10);
			break;
		//炮口向下	
		case Tank.DOWM:
			//画左边的矩形 可以切割为多个矩形形成履带
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x, this.y+5, 5, 5, false);
			g.fill3DRect(this.x, this.y+10, 5, 5, false);
			g.fill3DRect(this.x, this.y+15, 5, 5, false);
			g.fill3DRect(this.x, this.y+20, 5, 5, false);
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			//画右边的矩形
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+5, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+10, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+15, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+20, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//画中间的正方体
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//画中间的圆 
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//画中间的线
			g.drawLine(this.x+15, this.y+15, this.x+15, this.y+40);
			break;
		case Tank.LEFT:
			//画上边的矩形 可以切割为多个矩形形成履带
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x+5, this.y, 5, 5, false);
			g.fill3DRect(this.x+10, this.y, 5, 5, false);
			g.fill3DRect(this.x+15, this.y, 5, 5, false);
			g.fill3DRect(this.x+20, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			//画下边的矩形
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+5, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+10, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+15, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+20, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//画中间的正方体
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//画中间的圆
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//画中间的线
			g.drawLine(this.x+15, this.y+15, this.x-10, this.y+15);
			break;
		case Tank.RIGHT:
			//画上边的矩形 可以切割为多个矩形形成履带
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x+5, this.y, 5, 5, false);
			g.fill3DRect(this.x+10, this.y, 5, 5, false);
			g.fill3DRect(this.x+15, this.y, 5, 5, false);
			g.fill3DRect(this.x+20, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			//画下边的矩形
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+5, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+10, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+15, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+20, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//画中间的正方体
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//画中间的圆
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//画中间的线
			g.drawLine(this.x+15, this.y+15, this.x+40, this.y+15);
			break;
		}
	}
	
	
	
	
	
	
	//构造函数初始化
		//不同的坦克 初始位置不同 初始颜色不同 初始方向不同
	public Tank (int x,int y,int tankDirection,int tankColor,int speed) {
		this.x = x;
		this.y = y;
		this.tankColor = tankColor;
		this.tankDirection = tankDirection;
		this.tankSpeed = speed;
		
		//爆炸
		bomb = new Bomb();
	}
	
	
	
	
	
	//get 与 set 方法
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTankDirection() {
		return tankDirection;
	}

	public void setTankDirection(int tankDirection) {
		this.tankDirection = tankDirection;
	}

	public int getTankColor() {
		return tankColor;
	}

	public void setTankColor(int tankColor) {
		this.tankColor = tankColor;
	}
	
	public int getTankSpeed() {
		return tankSpeed;
	}
	
	public void setTankSpeed(int tankSpeed) {
		this.tankSpeed = tankSpeed;
	}
	
	//坦克移动
			//坦克向上移动
	public void moveUp () {
		this.y -= this.tankSpeed;
	}
	public void moveDown() {
		this.y += this.tankSpeed;
	}
	public void moveLeft() {
		this.x -= this.tankSpeed;
	}
	public void moveRight() {
		this.x += this.tankSpeed;
	}
}

//需要增加的特性放在子类内
class HostileTank extends Tank implements Runnable{
	//视野长度
	public static final int VIEW_LENGTH = 10;
	
	//感知外界坦克
		//这里仅仅定义了链表指针并没有真的指向MyPanel里的敌军坦克链表
	Vector<HostileTank> hts = new Vector<HostileTank>();
	
	//感知我方坦克
	MyTank mt = null;
	
	//构造函数初始化
		//地方坦克 需要传入坐标 需要传入颜色 需要传入方向
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed) {
		super(x, y, tankDirection, tankColor,tankSpeed);
		// TODO Auto-generated constructor stub
	}
	//获取其他坦克信息
	public void setHts(Vector<HostileTank> hts) {
		this.hts = hts;
	}
	//获取我方坦克信息
	public void setMt (MyTank mt) {
		this.mt = mt;
	}
	
	/**
	 * 主动攻击算法:
	 * 	通过感知我方坦克,如若我方坦克在敌方坦克上下左右几个方位那么就返回对应方向值,否则返回-1.
	 * @return
	 */
	public int attack () {
		//上下方向
		if ( (this.x + 15) >= mt.x && (this.x + 15) <= mt.x + 30 ) {
			//我方坦克在上边
			if (this.y > mt.y) {
				return Tank.UP;
			}else {
				return Tank.DOWM;
			}
		}else if ( (this.y + 15) >= mt.y && (this.y + 15) <= mt.y + 30 ) {
			//我方坦克在左边
			if (this.x > mt.x + 30) {
				return Tank.LEFT;
			}else {
				return Tank.RIGHT;
			}
		}
		return -1;
	}
	
	/**
	 * 坦克寻路算法
	 * 返回:可以走的方向的所有可能性
	 */
	public int[] moveDirection () {
		int[] arr = {0, 1, 2, 3};
		
		int key = 4;
		//左边有墙
		if (this.x <= 0){
			if (this.searchKey(arr, key, Tank.LEFT)) {
				key--;
			}
		}//上面有墙
		if (this.y <= 0) {
			if (this.searchKey(arr, key, Tank.UP)) {
				key--;
			}
		}//右边有墙
		if (this.x >= 370) {
			if (this.searchKey(arr, key, Tank.RIGHT)) {
				key--;
			}
		}//下面有墙
		if (this.y >= 270) {
			if (this.searchKey(arr, key, Tank.DOWM)) {
				key--;
			}
		}
		
		//排除墙方向后数组
		int[] wall = new int[key];
		for (int i = 0; i < key; i++) {
			wall[i] = arr[i];
		}
		
		//检测我方坦克
		if (mt.isAlive) {
			//mt在this右边
			if ( ( mt.x >= (this.x + 30) && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y - HostileTank.VIEW_LENGTH && mt.y <= (this.y + 30) + HostileTank.VIEW_LENGTH) ||
			(  mt.x >= (this.x + 30) && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (mt.y + 30) <= (this.y + 30 + HostileTank.VIEW_LENGTH)) ) {
				if (this.searchKey(arr, key, Tank.RIGHT)) {
					key--;
				}
			}
			//mt在this左边
			if ( ( (mt.x + 30) <= this.x && (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && mt.y >= this.y - HostileTank.VIEW_LENGTH && mt.y <= (this.y + 30) + HostileTank.VIEW_LENGTH ) || 
			( (mt.x + 30) <= this.x && (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (mt.y + 30) <= (this.y + 30) + HostileTank.VIEW_LENGTH ) ) {
				if (this.searchKey(arr, key, Tank.LEFT)) {
					key--;
				}
			}
			//mt在this上边
			if ( (mt.x >= this.x - HostileTank.VIEW_LENGTH && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) <= this.y && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH) ||
			((mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) <= this.y && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH)) {
				if (this.searchKey(arr, key, Tank.UP)) {
					key--;
				}
			}
			//mt在this下边
			if (( mt.x >= this.x - HostileTank.VIEW_LENGTH && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y + 30 && mt.y <= this.y + 30 + HostileTank.VIEW_LENGTH)||
			( (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y + 30 && mt.y <= this.y + 30 + HostileTank.VIEW_LENGTH )) {
				if (this.searchKey(arr, key, Tank.DOWM)) {
					key--;
				}
			}
		}
		
		//历遍坦克
//		这个for循环有问题
		for (int i = 0; i < hts.size(); i++) {
			//取出坦克
			HostileTank ht = hts.get(i);
			if (ht != this && ht.isAlive) {
				//ht在this右边
				if ( ( ht.x >= (this.x + 30) && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y - HostileTank.VIEW_LENGTH && ht.y <= (this.y + 30) + HostileTank.VIEW_LENGTH) ||
				(  ht.x >= (this.x + 30) && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (ht.y + 30) <= (this.y + 30 + HostileTank.VIEW_LENGTH)) ) {
					if (this.searchKey(arr, key, Tank.RIGHT)) {
						key--;
					}
				}
				//ht在this左边
				if ( ( (ht.x + 30) <= this.x && (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && ht.y >= this.y - HostileTank.VIEW_LENGTH && ht.y <= (this.y + 30) + HostileTank.VIEW_LENGTH ) || 
				( (ht.x + 30) <= this.x && (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (ht.y + 30) <= (this.y + 30) + HostileTank.VIEW_LENGTH ) ) {
					if (this.searchKey(arr, key, Tank.LEFT)) {
						key--;
					}
				}
				//ht在this上边
				if ( (ht.x >= this.x - HostileTank.VIEW_LENGTH && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) <= this.y && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH) ||
				((ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) <= this.y && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH)) {
					if (this.searchKey(arr, key, Tank.UP)) {
						key--;
					}
				}
				//ht在this下边
				if (( ht.x >= this.x - HostileTank.VIEW_LENGTH && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y + 30 && ht.y <= this.y + 30 + HostileTank.VIEW_LENGTH)||
				( (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y + 30 && ht.y <= this.y + 30 + HostileTank.VIEW_LENGTH )) {
					if (this.searchKey(arr, key, Tank.DOWM)) {
						key--;
					}
				}
			}
		}
		
		//copy 数组
		int[] res = null;
		if (key == 0) {
			//如果无路可退,就四处乱撞
			return wall;
		}else {
			res = new int[key];
			for (int i = 0; i < key; i++) {
				res[i] = arr[i];
			}
		}
		return res;
	}
	
	/**
	 * 寻路算法的辅助算法
	 * 功能:在数组中搜索是否有key,如若有,则将第一个放到最后一个有效位置然后退出
	 * @param arr
	 * @param key
	 */
	private boolean searchKey (int[] arr, int vaildSize, int value) {
		for (int i = 0; i < vaildSize; i++) {
			if (value == arr[i]) {
				//交换到有效位置并且退出
				arr[i] = arr[vaildSize - 1];
				arr[vaildSize - 1] = value;
				return true;
			}
		}
		return false;
	}

	//敌军坦克移动
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/**
		 * 因为坦克在同一时刻只能做一件事情,所以
		 * 可以使用switch进行选择结构,看坦克是走
		 * 还是发射子弹
		 */
		
		while (isAlive) {
			int key = (int) (Math.random() * 8);
			switch (key) {
			case 0:
				int attackDirection = attack();
				if (attackDirection != -1 && mt.isAlive) {
					this.tankDirection = attackDirection;
				}
				//发射子弹
				this.creatBullet();
				// 发射完了子弹就可能逃跑
//				if (attackDirection != -1) {
//					if (this.tankDirection == Tank.UP || this.tankDirection == Tank.DOWM) {
//						this.tankDirection = (int) (Math.random() * 2) == 0 ? Tank.LEFT : Tank.RIGHT;
//					} else {
//						this.tankDirection = (int) (Math.random() * 2) == 0 ? Tank.UP : Tank.DOWM;
//					}
//				}
				break;
			default:
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//走路
				int[] arr = moveDirection();
				if (arr.length == 1) {
					this.tankDirection = arr[0];
				}else {
					int i;
					int temp = -1;
					for (i = 0; i < arr.length; i++) {
						if (this.tankDirection == arr[i]) {
							//除了 0 - x 还可能出现负数
							temp = (int) (Math.random() * arr.length) - (int) (Math.random() * 50);
							break;
						}
					}if (i == arr.length) {
						temp = (int) (Math.random() * arr.length);
					}
					switch (temp) {
					case 0:
						this.tankDirection = arr[0];
						break;
					case 1:
						this.tankDirection = arr[1];
						break;
					case 2:
						this.tankDirection = arr[2];
						break;
					case 3:
						this.tankDirection = arr[3];
						break;
					default: 
						this.tankDirection = this.tankDirection;
						break;
					}
				}
				
				switch(this.tankDirection) {
				case Tank.UP:
					this.y-=this.tankSpeed;
					break;
				case Tank.DOWM:
					this.y+=this.tankSpeed;
					break;
				case Tank.RIGHT:
					this.x+=this.tankSpeed;
					break;
				case Tank.LEFT:
					this.x-=this.tankSpeed;
					break;
				}
				break;
			}
		}
	}
}

class MyTank extends Tank{
	//构造函数初始化
		//我方坦克不需要选择颜色 不需要选择方向  需要传入坐标
	public MyTank(int x, int y) {
		super(x, y, MyTank.UP, MyTank.RED,2);
		// TODO Auto-generated constructor stub
	}
}


class Bullet implements Runnable{
	//速度等级
	public static final int SLOW = 1;
	public static final int NORMAL = 3;
	public static final int FAST = 5;
	//方向
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	//我方坦克子弹专用颜色
	public static final int RED = 10;
	//敌方坦克子弹颜色
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;
	//坐标
	int x;
	int y;
	
	//子弹颜色
	int bulletColor;
	
	//子弹速度
	int bulletSpeed;
	//子弹方向
	int bulletDirection;
	
	//子弹是否存在
	boolean isAlive = true;
	
	
	//画子弹
	public void drawBullet(Graphics g){
		switch(this.bulletColor) {
		case Bullet.RED:
			g.setColor(Color.red);break;
		case Bullet.BLUE:
			g.setColor(Color.blue);break;
		case Bullet.YELLOW:
			g.setColor(Color.yellow);break;
		case Bullet.WHITE:
			g.setColor(Color.WHITE);break;
		case Bullet.GREEN:
			g.setColor(Color.GREEN);break;
		}
		g.fillOval(x, y, 3, 3);
	}
	

	
	//构造函数初始化
	public Bullet(int x,int y,int bulletColor,int bulletSpeed,int bulletDirection) {
		this.x = x;
		this.y = y;
		this.bulletColor = bulletColor;
		this.bulletDirection = bulletDirection;
		this.bulletSpeed = bulletSpeed;
	}
	
	//子弹移动
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (this.isAlive){
			try {
				// 线程里的静态方法
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch (this.bulletDirection) {
			case Bullet.UP:
				this.y -= this.bulletSpeed;
				break;
			case Bullet.DOWM:
				this.y += this.bulletSpeed;
				break;
			case Bullet.LEFT:
				this.x -= this.bulletSpeed;
				break;
			case Bullet.RIGHT:
				this.x += this.bulletSpeed;
				break;
			}
			if (this.x >= 400 || this.x <= 0 || this.y >= 300 || this.y <= 0) {
				this.isAlive = false;
			}
		}
	}
}

//炸弹类
class Bomb{
	//坐标
	int x;
	int y;
	
	//血量
	int bloodValue = 15;
	
	//炸弹是否"活着"
	boolean isAlive = true;
	
	//爆炸图片
		//创建一个图片对象数组并且设置为静态的原因
			//爆炸图片可以共用,只要每个炸弹类的血量和生命不同即可！
	static Image[] img = new Image[16];
	
	//构造函数初始化  
		//不初始化爆炸地点  爆炸需要产生时才设置爆炸地点	
	static {
		for	(int i = 0; i < 16; i++) {
			//这种方法调用图片文件存在第一次绘画不明显甚至几乎看不见的问题！
//			img[i] = Toolkit.getDefaultToolkit().getImage("E:\\java\\MyselfGame\\src\\Image\\explode\\e" + (16 - i) + ".gif");
			try {
				img[i] = ImageIO.read(new File("E:\\java\\MyselfGame\\src\\Image\\explode\\e" + (16 - i) + ".gif"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//画炸弹
	public void drawBomb (Graphics g,MyPanel mp) {
		//不需要循环的原因是g.drawImage(img[bloodValue],this.x,this.y,mp);传入的第一个数据是数组的情况下会自动调用repoint();
		if (bloodValue >= 0) {
System.out.println("画图");
			g.drawImage(img[bloodValue],this.x,this.y,mp);
			bloodValue--;
		}
	}
	
	
	//设置 爆炸地点
	public void setX(int x) {
		this.x = x - 17;
	}
	
	public void setY(int y) {
		this.y = y - 30;
	}
}