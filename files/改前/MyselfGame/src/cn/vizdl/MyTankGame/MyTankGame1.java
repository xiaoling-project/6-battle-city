package cn.vizdl.MyTankGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

/**
 * 功能:坦克游戏1.0版 
 * 独自完成版
 * 1.画出坦克
 * 2.我的坦克可以上下左右移动(正方形的坦克方便改变坦克形态)
 * @author Vizdl
 *
 */

//窗口类
public class MyTankGame1 extends JFrame{
	//对象指针初始化
	MyPanel mp = null;
	
	//构建函数初始化
	public MyTankGame1(){
		//指针初始化
		mp = new  MyPanel();
		
		//添加组件
		this.add(mp);
		
		//添加监听
		this.addKeyListener(mp); //将该窗口监听者设置为面板
		
		//窗口设置
		this.setTitle("TankGame");
		this.setSize(400, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//主函数
	public static void main (String[] args) {
		new MyTankGame1();
	}
}




//画板组件
	//画板组件需要有监听功能 这样方便直接通过接收信息更改坦克对象的位置 方向 与颜色
class MyPanel extends JPanel implements KeyListener{
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
	
	
	
	//构造函数初始化
		//地方坦克 需要传入坐标 需要传入颜色 需要传入方向
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed) {
		super(x, y, tankDirection, tankColor,tankSpeed);
		// TODO Auto-generated constructor stub
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