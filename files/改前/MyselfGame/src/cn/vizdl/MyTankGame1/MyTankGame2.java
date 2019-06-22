package cn.vizdl.MyTankGame1;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

/**
 * 功能:坦克游戏2.0版 
 * 独自完成版
 * 1.画出坦克
 * 2.我的坦克可以上下左右移动(正方形的坦克方便改变坦克形态)
 * 3.我的坦克可以发射子弹(一个)
 * 
 * 修复:
 * 1.修复了坦克1.0版本坦克中间圆形不是填充圆问题
 * @author Vizdl
 *
 */

//窗口类
public class MyTankGame2 extends JFrame{
	//对象指针初始化
	MyPanel mp = null;
	//构建函数初始化
	public MyTankGame2(){
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
		this.setSize(400, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//主函数
	public static void main (String[] args) {
		new MyTankGame2();
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
	int htNumber = 3;
	
	//构造函数初始化
	public MyPanel() {
		//对象名认取
		mt = new MyTank(100,100);
		
		for (int i = 0; i < htNumber; i++) {
			//创建一个敌方坦克对象
			HostileTank newHt = new HostileTank(i*50+50,0,HostileTank.DOWM,HostileTank.BLUE,2);
			//将敌方坦克对象加入敌方组
			hts.add(newHt);
		}
	}
	
	//重写paint方法
		//paint是画图区域
	public void paint (Graphics g) {
		//背景
		g.fillRect(0, 0, 400, 300);
		
		//我的坦克绘制
		mt.drawTank(g);
		//敌方坦克绘制
		for (int i = 0; i < htNumber; i++) {
			hts.get(i).drawTank(g);
		}
		
		//画我的子弹
		if (mt.bu != null && mt.bu.isAlive) {
			mt.bu.drawBullet(g);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		//更改信息
		if (arg0.getKeyCode() == KeyEvent.VK_W && mt.getY() > 1) {
			mt.setTankDirection(Tank.UP);
			mt.moveUp();
		}else if(arg0.getKeyCode() == KeyEvent.VK_S &&  mt.getY() < 269){
			mt.setTankDirection(Tank.DOWM);
			mt.moveDown();
		}else if(arg0.getKeyCode() == KeyEvent.VK_A  &&  mt.getX() > 1) {
			mt.setTankDirection(Tank.LEFT);
			mt.moveLeft();
		}else if(arg0.getKeyCode() == KeyEvent.VK_D  &&  mt.getX()  < 369) {
			mt.setTankDirection(Tank.RIGHT);
			mt.moveRight();
		}
		
		
		if (arg0.getKeyCode() == KeyEvent.VK_J) {
			mt.creatBullet();
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
	
	
	//坦克坐标
	int x;
	int y;
	//坦克方向
	int tankDirection;
	//坦克颜色
	int tankColor;
	//坦克速度
	int tankSpeed;
	



	//画坦克     应该是静态类么？ 不应该 不同对象画不同坦克   没有坦克对象不允许画坦克 	一个坦克对象画一个坦克？
		//既然一个对象制造一个坦克 并且坦克对象内部拥有所有的参数，那么画坦克久不需要传入多余的参数！
	public void drawTank (Graphics g) {
		switch (this.tankColor) {
		case Tank.RED:
			g.setColor(Color.red);break;
		case Tank.BLUE:
			g.setColor(Color.blue);break;
		case Tank.YELLOW:
			g.setColor(Color.yellow);break;
		case Tank.WHITE:
			g.setColor(Color.WHITE);break;
		case Tank.GREEN:
			g.setColor(Color.GREEN);break;
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
class HostileTank extends Tank{
	//敌军坦克间隔固定时间会放子弹
	Tank bullet = null;	//不应该只有一个子弹
	
	//构造函数初始化
		//地方坦克 需要传入坐标 需要传入颜色 需要传入方向
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed) {
		super(x, y, tankDirection, tankColor,tankSpeed);
		// TODO Auto-generated constructor stub
	}
}
class MyTank extends Tank{
	//我军坦克 按J键就生成一个新的坦克 故而创建我军坦克方法与敌军不同  但应该有个上线
	Bullet bu = null;
	public void creatBullet() {
		//根据坦克方向不同，坦克子弹出现初始位置也不同
		switch (this.tankDirection) {
		case MyTank.UP:
			bu = new Bullet(this.x + 14,this.y - 11,Bullet.RED,Bullet.NORMAL,Bullet.UP);
			break;
		case MyTank.DOWM:
			bu = new Bullet(this.x + 14,this.y + 41,Bullet.RED,Bullet.NORMAL,Bullet.DOWM);
			break;
		case MyTank.LEFT:
			bu = new Bullet(this.x - 11,this.y + 14,Bullet.RED,Bullet.NORMAL,Bullet.LEFT);
			break;
		case MyTank.RIGHT:
			bu = new Bullet(this.x + 41,this.y + 14,Bullet.RED,Bullet.NORMAL,Bullet.RIGHT);
			break;
		}
		Thread t = new Thread(bu);
		t.start();
	}
	
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
		while (this.isAlive) {
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
			System.out.println("x = "+this.x+",y = "+this.y);
			if (this.x >= 400 || this.x <= 0 || this.y >= 300 || this.y <= 0) {
				this.isAlive = false;
			}
		}
	}
}