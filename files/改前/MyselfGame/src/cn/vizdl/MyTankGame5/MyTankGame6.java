package cn.vizdl.MyTankGame5;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

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
 * 6.无论是我军还是敌军，坦克被击中后会发生爆炸
 * 7.使得敌军能够随机移动
 * 8.增加坦克碰撞检测,不允许坦克能重叠
 * 9.子弹和坦克都有血量,坦克血量越高所产生的子弹的攻击力也越高。
 * 10.可以选择关卡(*)
 * 	思路 : 做一个空面板用于提示
 * 
 * 11.将原本处于MyPanel内对数据的运算单独封装成一个线程。
 * 
 * 未解决BUG:
 * 	暂停时可改变方向
 * 已解决BUG:
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

/**
 * MyTankGame是一个窗口类
 * @author HP
 *
 */
public class MyTankGame6 extends JFrame implements ActionListener{
/*********************************这些都是组件******************************************/
	//我的画板 即显示线程类
	MyPanel mp = null;
	//我的开始面板 
	MyStartPanel msp = null;
	//菜单栏
	JMenuBar jmb = null;
	//菜单
	JMenu jm1 = null;
	//菜单内按钮
		//开始游戏
		JMenuItem jmi = null;
		//退出游戏
		JMenuItem jmi2 = null;
		//存档退出
		JMenuItem jmi3 = null;
		//读档开始
		JMenuItem jmi4 = null;
/****************************************************************************************/
	//构建函数初始化
	public MyTankGame6(){
		/**
		 * 下面这部分是一些组件的创建和快捷方式的设置
		 * 部分组件允许传入字符串传入的字符串会显示在组件上
		 * 快捷方式是传入字符设置。
		 */
		//创建我的开始画板
		msp = new MyStartPanel();
		//创建菜单栏
		jmb = new JMenuBar();
		//创建菜单	
		jm1 = new JMenu("游戏(G)");	//传入的字符串会显示在组件上 后面同理
		//设置快捷方式
		jm1.setMnemonic('G');	//按Ctrl + G就能快捷打开
		//创建按钮
		jmi = new JMenuItem("开始新游戏(H)");
		jmi.setMnemonic('H');
		jmi4 = new JMenuItem("读档开始(U)");
		jmi4.setMnemonic('U');
		jmi3 = new JMenuItem("存档退出(F)");
		jmi3.setMnemonic('F');
		jmi2 = new JMenuItem("退出游戏(E)");
		jmi2.setMnemonic('E');
		/**
		 * 以下是监听的注册
		 * 大多数组件具有注册监听的功能,
		 * 输入参数 : 设为某一实现了ActionListener的对象实例P
		 * 在设置了参数之后,如若具有监听事件,例如当前按钮按下
		 * 这个对象P就能在actionPerformed里面接收到消息(actionPerformed是必须要存在在这个对象的类中 不然会报错)。
		 */
		jmi.addActionListener(this); 
		jmi2.addActionListener(this);
		jmi3.addActionListener(this);
		jmi4.addActionListener(this);
		/**
		 * 这是按键类的监听注册
		 * 设置监听信号,监听信号也就是组件被按下后
		 * 会发送什么信号给actionPerformed函数
		 * 例如jmi.setActionCommand("playGame");
		 * 就会发送playGame给actionPerformed
		 */
		jmi.setActionCommand("playGame");
		jmi2.setActionCommand("exitGame");
		jmi3.setActionCommand("recordAndExit");
		jmi4.setActionCommand("playAndRead");
		//将我的开始画板添加到我的坦克这个窗口上
		this.add(msp);
		//一层层加入
		jm1.add(jmi);
		jm1.add(jmi4);
		jm1.add(jmi3);
		jm1.add(jmi2);
		jmb.add(jm1);
		//将菜单添加到当前窗口
		this.setJMenuBar(jmb);
		
		/**
		 * 开启开始面板线程也就是一开始的提示的那个线程
		 * 在java中 线程允许传入一个对象,这个对象必须要implements Runnable 
		 * 并且这个对象所对应类中一定要有run()函数
		 * 之后t.start();就能直接在线程里调用这个对象的run();
		 * 从而实现开启线程
		 */
		Thread t = new Thread(msp);
		t.start();

		
		//窗口设置
		this.setTitle("TankGame");//窗口标题设置
		this.setSize(600, 500);//窗口大小设置
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认按下X就关闭窗口
		this.setVisible(true);//是否可见
	}
	
	/**
	 * 主函数在此仅起到开启游戏的功能
	 * @param args
	 */
	public static void main (String[] args) {
		new MyTankGame6();
	}
	
	
	@Override
	/**
	 * 这个函数是接收组件信号的函数
	 * arg0就是事件信号
	 * 一般利用getActionCommand函数来解析信号
	 * 然后再做处理
	 */
	public void actionPerformed(ActionEvent arg0) {
		//一般利用getActionCommand函数来解析信号
		String command = arg0.getActionCommand();
		//如若是开始游戏 或者读档开始游戏
		if (command.equals("playGame") || command.equals("playAndRead")) {
			//先删除一开始的 开始画板
			this.remove(msp);
			
			//指针初始化
			if (command.equals("playAndRead")) {
				//这是我之前结构图里说的两种传参方式
				mp = new  MyPanel(Record.readRecording());
			}else {
				//这是我之前结构图里说的两种传参方式
				mp = new MyPanel();
			}
			
			/**
			 * 将之后的显示游戏画面的画板添加到窗口
			 */
			this.add(mp);
			
			/**
			 * 将画板添加到窗口
			 * 这次添加的监听时键盘监听
			 * 也是为了之后我方坦克可以由按键操作
			 * 
			 * 但是键盘监听的信号处理是 这里的mp来处理
			 * 键盘的处理函数在MyPanel类内
			 */
			this.addKeyListener(mp); //将该窗口监听者设置为面板
			
			//启动画板线程（用于重绘）
			Thread t = new Thread (mp);
			t.start();
			
			/**
			 * 重新设置是否可见
			 * 用于刷新窗口
			 */
			this.setVisible(true);
		}else if (command.equals("exitGame") || command.equals("recordAndExit")) {
			//将最高分更新
			Record.updataHighestScore();
			//如若是存档退出
			if (command.equals("recordAndExit")) {
				//这里会传入我的坦克指针和敌军坦克数组指针 然后将所有的属性保存到文件中
				Record.keepRecording(mp.mt, mp.hts);
			}
			System.exit(0);
		}
	}
}

/**
 * 用于显示开始画面的线程类
 * @author HP
 *
 */
class MyStartPanel extends JPanel implements Runnable {
	int times = 0;
	//重写paint方法
	public void paint (Graphics g){
		g.fillRect(0, 0, 400, 300);
		/**
		 * 在这里利用了times的奇偶性来使得开始画面提示
		 * 具有闪动的效果
		 * 也就是一下画 一下不画
		 */
		times++;
		if ((times & 1) != 0) {
			g.setColor(Color.YELLOW);
			Font myFont = new Font("华文新魏",Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("Stage : 1", 150, 150);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 这里的repaingt函数主要就是
			 * 调用了当前类中的paint
			 * 因为paint,repaint是继承了JPanel得来的
			 * 然后重写了paint。
			 */
			this.repaint();
		}
	}
}

/**
 * 用于碰撞检测的线程类
 * @author HP
 *
 */
class Operation implements Runnable{
	MyTank mt = null;
	Vector<HostileTank> hts = null;
	
	/**
	 * 传入参数 mt 和 hts 是为了得到数据
	 * @param mt
	 * @param hts
	 */
	public Operation(MyTank mt, Vector<HostileTank> hts) {
		this.mt = mt;
		this.hts = hts;
	}
	
	@Override
	public void run() {
		while (true) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(200);
				// 每隔一点时间就刷新
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 判断所有敌方坦克和我方子弹是否发生有效碰撞
			for (HostileTank ht : hts) {
				// 判断坦克是否活着
				if (ht.isAlive) {
					for (int j = 0; j < mt.Bullets.size(); j++) {
						// 取出子弹
						Bullet bu = mt.Bullets.get(j);
						// 判断子弹是否有效
						if (bu.isAlive) {
							//这是本类中的一个函数 函数说明请看这个函数的上方
							this.hitTank(ht, bu, false);
						}
					}
				}
			}
	
			// 判断我方坦克与敌方子弹是否会发生碰撞
			for (HostileTank ht : hts) {
				// 即使敌方坦克无效,即使地方坦克子弹有效,那么就行
				for (int j = 0; j < ht.Bullets.size(); j++) {
					//取出敌方坦克的子弹
					Bullet bu = ht.Bullets.get(j);
					//判断子弹和我方坦克是否有效
					if (bu.isAlive && mt.isAlive) {
						this.hitTank(mt, bu, true);
					}
				}
			}
		}
	}
	
	
	/**
	 *  检测 这个子弹 与这个坦克 是否会发生碰撞 如果发生 则 子弹与坦克同归于尽(通过修改子弹和坦克 isAlive这个属性)并不真正删除数据
	 *  传参方式组合
	 *  1) 我方坦克指针 敌方子弹数组指针 true
	 *  2) 敌方坦克指针 我方子弹数组指针 false
	 * @param tank
	 * @param bullet
	 * @param isMyTank
	 */
	public void hitTank(Tank tank, Bullet bullet, boolean isMyTank) {
		//如若发生碰撞
		if (bullet.x >= tank.x && bullet.x < (tank.x + 30) && bullet.y >= tank.y && bullet.y <= (tank.y + 30)){
			//坦克死亡
			if (tank.isAlive) {
				//
				if (!isMyTank) {
					/**
					 * 这个游戏其实设置了两种游戏模式,isSpecialMode如若为true
					 * 则子弹具有穿透性,并且子弹的血量会和敌方的血量抵消
					 * 如若为0则死亡
					 * 另一种就是我们看到的版本
					 */
					if (Record.isSpecialMode()) {
						//在记录类里面的 添加分数函数  
						//这里的布尔表达式就是为了防止敌军有三滴血 却因为子弹血量有四滴 而加四分(特殊模式下)
						Record.addScore(bullet.bulletBlood < tank.tankBlood ? bullet.bulletBlood : tank.tankBlood);
					}else {
						Record.addScore(1);
					}
				}
				
				if (Record.isSpecialMode()) {
					//坦克降血
					tank.dropOfBlood(bullet.bulletBlood);
					//子弹降血
					bullet.dropOfBlood(tank.tankBlood);
				}else {
					tank.dropOfBlood();
					bullet.dropOfBlood();
				}
				//实时修改记录类内数
				if (isMyTank) {
					Record.setMyTankBlood(tank.getLive());
				}else{
					if (!tank.isAlive){
						Record.reduceHostileTankNumber();
					}
				} 
			}
		}
	}
}


/**
 * 用于显示重绘游戏画面的线程类
 * @author HP
 *
 */
//画板组件
//画板组件需要有监听功能 这样方便直接通过接收信息更改坦克对象的位置 方向 与颜色
//画板线程
class MyPanel extends JPanel implements KeyListener, Runnable{ 
	//对象指针初始化
	MyTank mt = null;
	
	/**
	 * 这是创建了两个坦克的图像 只是好看的 提示还剩多少个坦克
	 */
	MyTank myTankImage = null;
	Tank hostileTankImages = null;
	
		//敌方坦克不止一辆 所以得定义 敌军坦克数量 这一变量 并且 敌军坦克得使用链表来实现成为一个集合
	//定义敌人坦克组
	Vector<HostileTank> hts = new Vector<HostileTank>();

	/**
	 * 读档开始游戏,传入一个文件类
	 * 然后通过这个文件类来得到坦克的属性恢复之前的游戏
	 * @param reader
	 */
	public MyPanel(FileReader reader) {
		Scanner scanner = null;
		Record.initData();
		try {
			scanner = new Scanner(reader);
			// 对象名认取
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			int tankDirection = scanner.nextInt();
			int tankColor = scanner.nextInt();
			int tankSpeed = scanner.nextInt();
			int blood = scanner.nextInt();
			Vector<Bullet> bullet = new Vector<Bullet>();
			int m = scanner.nextInt();
			// 读取子弹
			for (int i = 0; i < m; i++) {
				Bullet bu = new Bullet(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
						scanner.nextInt(), scanner.nextInt());
				bullet.add(bu);
				Thread t = new Thread(bu);
				t.start();
			}
			mt = new MyTank(x, y, tankDirection, tankColor, tankSpeed, blood, bullet);
			// 创建我方提示坦克
			myTankImage = new MyTank(50, 350, 1);
			
			
			Record.setHostileTankNumber(scanner.nextInt());
			for (int i = 0; i < Record.getHostileTankNumber(); i++) {
				// 对象名认取
				x = scanner.nextInt();
				y = scanner.nextInt();
				tankDirection = scanner.nextInt();
				tankColor = scanner.nextInt();
				tankSpeed = scanner.nextInt();
				blood = scanner.nextInt();
				bullet = new Vector<Bullet>();
				m = scanner.nextInt();
				// 读取子弹
				for (int i1 = 0; i1 < m; i1++) {
					Bullet bu = new Bullet(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
							scanner.nextInt(), scanner.nextInt());
					bullet.add(bu);
					Thread t = new Thread(bu);
					t.start();
				}
				// 创建一个敌方坦克对象
				HostileTank newHt = new HostileTank(x, y, tankDirection, tankColor, tankSpeed, blood, bullet);
				newHt.setHts(hts);
				newHt.setMt(mt);
				// 启动敌军线程
				Thread t = new Thread(newHt);
				t.start();
	
				// 将敌方坦克对象加入敌方组
				hts.add(newHt);
			}

			
			//恢复上一把的非坦克数据
			Record.setScore(scanner.nextInt());
			// 创建敌方坦克提示
			hostileTankImages = new Tank(180, 350, Tank.UP, Tank.BLACK, 0, 1);
			
			this.startOperation();
		}catch (Exception e	) {
			e.printStackTrace();
		}finally {
			if (scanner != null)
				scanner.close();
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * 直接开始游戏
	 * 默认从record内读取信息,来初始化数据属性。
	 */
	public MyPanel() {
		// 初始化记录信息
		Record.initData();
		// 对象名认取
		mt = new MyTank(100, 200, Record.getMyTankBlood());
		// 创建我方提示坦克
		myTankImage = new MyTank(50, 350, 1);
		int[] colorArr = Record.getColorArr();
		for (int i = 0, index = 0; i < Record.getHostileTankNumber(); i++) {
			while (colorArr[index] == 0) {
				index++;
			}
			int nowTankColor = index + Tank.YELLOW;
			colorArr[index]--;
			// 创建一个敌方坦克对象
			HostileTank newHt = null;
			if (i < 8) {
				newHt = new HostileTank(i * 50, 0, HostileTank.DOWM, nowTankColor, 1,
						Tank.TankColorToLiveTable(nowTankColor));
			} else {
				int temp = i % 6;
				newHt = new HostileTank(i * 50, temp * 50, HostileTank.LEFT, nowTankColor, 2,
						Tank.TankColorToLiveTable(nowTankColor));
			}
			newHt.setHts(hts);
			newHt.setMt(mt);
			// 启动敌军线程
			Thread t = new Thread(newHt);
			t.start();

			// 将敌方坦克对象加入敌方组
			hts.add(newHt);
		}
		// 创建敌方坦克提示
		hostileTankImages = new Tank(180, 350, Tank.UP, Tank.BLACK, 0, 1);
		
		this.startOperation();
	}
	
	/**
	 * 开启计算后台线程
	 */
	public void startOperation() {
		Operation opt = new Operation(mt,hts);
		Thread t = new Thread(opt);
		t.start();
	}
	
	// 重写paint方法
	// paint是画图区域
	public void paint(Graphics g) {
		// 背景
		g.fillRect(0, 0, 400, 300);
		g.setColor(Color.GRAY);
		g.fillRect(400, 0, 1800, 300);
		g.setColor(Color.WHITE);
		g.fillRect(0, 300, 1800, 500);
		g.setColor(Color.RED);
		g.drawLine(0, 0, 0, 300);
		g.drawLine(0, 0, 400, 0);
		g.drawLine(0, 300, 1800, 300);
		g.setColor(Color.GREEN);
		g.drawLine(400, 0, 400, 300);

		// 提示信息绘制
		this.drowInfo(g);

		// 我的坦克绘制
		if (mt.isAlive == true) {
			mt.drawTank(g);
		}

		// 我方爆炸绘制
		if (!mt.isAlive && mt.bomb.isAlive) {
			// 设置爆炸地点
			mt.bomb.setX(mt.getX());
			mt.bomb.setY(mt.getY());
			// 画炸弹
			mt.bomb.drawBomb(g, this);
			if (mt.bomb.bloodValue >= 16) {
				mt.bomb.isAlive = false;
			}
		}

		// 敌方坦克绘制
		for (HostileTank ht : hts) {
			if (ht.isAlive) {
				ht.drawTank(g);
			}

			// 坦克炸弹绘制
			else if (!ht.isAlive && ht.bomb.isAlive) {
				// 设置爆炸地点
				ht.bomb.setX(ht.getX());
				ht.bomb.setY(ht.getY());

				ht.bomb.drawBomb(g, this);
				if (ht.bomb.bloodValue < 0) {
					ht.bomb.isAlive = false;
				}
			}

			// 绘制敌军子弹
			for (int k = 0; k < ht.Bullets.size(); k++) {
				Bullet bu = ht.Bullets.get(k);
				if (bu != null && bu.isAlive) {
					bu.drawBullet(g);
				}
				// 已"死"子弹清理
				if (bu.isAlive == false) {
					ht.Bullets.remove(bu); // 为什么这样不会发生数组角标越界问题？
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
	
	//绘制提示信息
	public void drowInfo(Graphics g) {
		// 我方提示坦克绘制
		myTankImage.drawTank(g);
		g.setColor(Color.RED);
		g.drawString("我方血量 : " + Record.getMyTankBlood(), 30, 400);
		hostileTankImages.drawTank(g);
		g.setColor(Color.BLACK);
		g.drawString("敌方坦克数量 : " + Record.getHostileTankNumber(), 150, 400);
		// 最高分徽章绘制
		Badge.drawKingBadge(300, 340, g, this);
		g.drawString("历史最高得分 : " + Record.getHighestScore(), 285, 400);
		// 当前分徽章绘制
		Badge.drawBadge(450, 340, g, this);
		g.drawString("当前得分 : " + Record.getScore(), 447, 400);
	}


	
	/**
	 * 检测我的坦克附近是否有敌军坦克
	 * 如若没有返回true
	 * 如若有 返回false
	 * @param Direction
	 * @return
	 */
	public boolean hasntHostileTank (int Direction) {
		//警戒距离
		int DISTANCE = 2;
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		switch(Direction) {
		case Tank.UP:
			x1 = mt.x;
			x2 = mt.x + 30;
			y1 = mt.y - DISTANCE;
			y2 = mt.y;
			break;
		case Tank.DOWM:
			x1 = mt.x;
			x2 = mt.x + 30;
			y1 = mt.y + 30;
			y2 = mt.y + 30 + DISTANCE;
			break;
		case Tank.LEFT:
			x1 = mt.x - DISTANCE;
			x2 = mt.x;
			y1 = mt.y;
			y2 = mt.y + 30;
			break;
		case Tank.RIGHT:
			x1 = mt.x + 30;
			x2 = mt.x + 30 + DISTANCE;
			y1 = mt.y;
			y2 = mt.y + 30;
			break;
		}
		
		//检测是否有敌军坦克在x1,x2,y1,y2组成的方框内
		for (HostileTank ht : hts) {
			if (ht.isAlive && !(ht.x > x2 || ht.x + 30 < x1) && !(ht.y > y2 || ht.y + 30 < y1)) {
				return false;
			}
		}return true;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		// 更改信息
		if (arg0.getKeyCode() == KeyEvent.VK_W && mt.getY() > 1) {
			mt.setTankDirection(Tank.UP);
			if (this.hasntHostileTank(Tank.UP))
				mt.moveUp();
		} else if (arg0.getKeyCode() == KeyEvent.VK_S && mt.getY() < 269) {
			mt.setTankDirection(Tank.DOWM);
			if (this.hasntHostileTank(Tank.DOWM))
				mt.moveDown();
		} else if (arg0.getKeyCode() == KeyEvent.VK_A && mt.getX() > 1) {
			mt.setTankDirection(Tank.LEFT);
			if (this.hasntHostileTank(Tank.LEFT))
				mt.moveLeft();
		} else if (arg0.getKeyCode() == KeyEvent.VK_D && mt.getX() < 369) {
			mt.setTankDirection(Tank.RIGHT);
			if (this.hasntHostileTank(Tank.RIGHT))
				mt.moveRight();
		} else if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			Record.setStop(!Record.isStop());
			for (HostileTank ht : hts) {
				if (Record.isStop()) {
					ht.stop();
				}else {
					ht.move();
				}
			}
			
			if (Record.isStop()) {
				mt.stop();
			}else {
				mt.move();
			}
		}

		if (arg0.getKeyCode() == KeyEvent.VK_J && mt.isAlive) {
			if (mt.Bullets.size() <= 4) {
				mt.creatBullet();
			}
		}
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
				Thread.sleep(50);
				//每隔一点时间就刷新
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
		}
	}

}


/**
 * 这是所有坦克类的父类
 * @author HP
 *
 */
//所有坦克的共性放在Tank类内
class Tank{
	/**
	 * 常数就是c中类似宏的定义
	 */
	//常数
		//方向
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
		//颜色
			//我方坦克专用颜色
	public static final int RED = 1000;
	public static final int PINK = 1001;
			//敌方坦克颜色
	public static final int BLACK = 15;
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;
	//坦克存活问题
	boolean isAlive = true;
	
	//血量
	int tankBlood;
	
	/**
	 * 之后的暂停功能就是借助了这个属性来完成
	 */
	boolean isStop = false;

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
	Vector<Bullet> Bullets = null;


	//构造函数初始化
		//不同的坦克 初始位置不同 初始颜色不同 初始方向不同
	/**
	 * 因为需要恢复数据 而恢复数据时候一般会有子弹,所以设置了两种构造方法
	 */
	public Tank (int x,int y,int tankDirection,int tankColor,int speed, int blood) {
		this(x, y, tankDirection, tankColor, speed, blood, null);
	}
	public Tank (int x,int y,int tankDirection,int tankColor,int speed, int blood, Vector<Bullet> Bullets) {
		this.x = x;
		this.y = y;
		this.tankColor = tankColor;
		this.tankDirection = tankDirection;
		this.tankSpeed = speed;
		this.tankBlood = blood;
		//爆炸
		bomb = new Bomb();
		if(Bullets != null) {
			this.Bullets = Bullets;
		}else {
			this.Bullets = new Vector<Bullet>();
		}
	}
	
	public static int TankColorToLiveTable (int tankColor) {
		if (tankColor != Tank.BLACK) {
			return tankColor - Tank.YELLOW + 1;
		}return 1;
	}
	
	/**
	 * 这是在一般模式下调用的方法
	 * 改变坦克颜色
	 */
	public void dropOfBlood () {
		dropOfBlood(1);
	}
	
	/**
	 * 这是在特殊模式下调用的方法。
	 * 根据不同颜色子弹的攻击 丢失不同血量
	 * @param bulletColor
	 */
	public void dropOfBlood(int blood) {
		this.tankColor-=blood;
		this.tankBlood -= blood;
		if (this.tankBlood <= 0) {
			this.isAlive = false;
		}
	}
	public int getLive () {
		return this.tankBlood;
	}
	/*****************这是设置坦克移动和暂停属性的*************************/
	/**
	 * 之后的暂停功能就是借助了这个方法来完成
	 */
	public void stop() {
		isStop = true;
		//给子弹暂停
		for (Bullet b : Bullets) 
			b.stop();
	}
	
	public void move() {
		isStop = false;
		for (Bullet b : Bullets) 
			b.move();
	}
	/********************************************************/
	// 创建子弹
	public void creatBullet() {
		//如若坦克状态不是暂停的
		if (!isStop) {
			Bullet bu = null;
			// 根据坦克方向不同，坦克子弹出现初始位置也不同
			switch (this.tankDirection) {
			case MyTank.UP:
				bu = new Bullet(this.x + 14, this.y - 11, this.tankColor, Bullet.NORMAL, this.tankDirection, this.tankBlood);
				break;
			case MyTank.DOWM:
				bu = new Bullet(this.x + 14, this.y + 41, this.tankColor, Bullet.NORMAL, this.tankDirection, this.tankBlood);
				break;
			case MyTank.LEFT:
				bu = new Bullet(this.x - 11, this.y + 14, this.tankColor, Bullet.NORMAL, this.tankDirection, this.tankBlood);
				break;
			case MyTank.RIGHT:
				bu = new Bullet(this.x + 41, this.y + 14, this.tankColor, Bullet.NORMAL, this.tankDirection, this.tankBlood);
				break;
			}
			//将子弹添加到子弹向量里
			Bullets.add(bu);
			//开启子弹的线程
			Thread t = new Thread(bu);
			t.start();
		}
	}

	// 画坦克 应该是静态类么？ 不应该 不同对象画不同坦克 没有坦克对象不允许画坦克 一个坦克对象画一个坦克？
	// 既然一个对象制造一个坦克 并且坦克对象内部拥有所有的参数，那么画坦克久不需要传入多余的参数！
	/**
	 * 因为游戏中坦克是由几个矩形和圆拼凑成的
	 * 在这里利用Graphics类来绘画矩形和圆
	 * 并且利用坦克类的属性来决定方向
	 * @param g
	 */
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
		case Tank.PINK:
			g.setColor(Color.PINK);
			break;
		case Tank.BLACK:
			g.setColor(Color.BLACK);
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
	
	/************************以下是都是设置,改变和获取坦克属性的方法**************************/
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
		if (!isStop)
			this.y -= this.tankSpeed;
	}
	public void moveDown() {
		if (!isStop)
			this.y += this.tankSpeed;
	}
	public void moveLeft() {
		if (!isStop)
			this.x -= this.tankSpeed;
	}
	public void moveRight() {
		if (!isStop)
			this.x += this.tankSpeed;
	}

	public int getTankDirection() {
		return tankDirection;
	}

	public void setTankDirection(int tankDirection) {
		if (!this.isStop) {
			this.tankDirection = tankDirection;
		}
	}
	
}




/**
 * AI的自动移动讲解:
 * 在这里AI利用mt和hts调用数据来判断附近是否具有其他坦克
 * 如若具有 就往不同的方向走
 * 如若无路可走 就选择停止在那
 * 这样就能避免坦克重叠在一起
 * @author HP
 *
 */
//需要增加的特性放在子类内
class HostileTank extends Tank implements Runnable{
	//视野长度 视野长度是AI用于自动移动的
	public static final int VIEW_LENGTH = 15;
	
	
	//感知外界坦克
		//这里仅仅定义了链表指针并没有真的指向MyPanel里的敌军坦克链表
	Vector<HostileTank> hts = new Vector<HostileTank>();
	
	//感知我方坦克
	MyTank mt = null;
	
	//构造函数初始化
		//地方坦克 需要传入坐标 需要传入颜色 需要传入方向
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood, Vector<Bullet> bullet) {
		super(x, y, tankDirection, tankColor,tankSpeed, blood, bullet);
		// TODO Auto-generated constructor stub
	}
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood) {
		super(x, y, tankDirection, tankColor,tankSpeed, blood);
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
	 * 如何感知坦克:
	 * 判断当前坦克的上下左右的的四块矩形内是否有另一个坦克
	 */
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
	 * 如若没有返回null
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
			/**
			 * 如何感知坦克:
			 * 判断当前坦克的上下左右的的四块矩形内是否有另一个坦克
			 */
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
			return res;
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
		 * 因为坦克在同一时刻只能做一件事情,所以 可以使用switch进行选择结构,看坦克是走 还是发射子弹
		 */
		//如若敌军坦克死亡,则线程停止。
		while (isAlive) {
			//如若当前坦克处于暂停状态,线程休眠后继续
			if (this.isStop) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			//暂时保存我方 坦克之前的方向,应对无路可走和暂停两种状况
			int lastDirection = this.tankDirection;
			/**
			 * 因为要调节 发射子弹 和 走路两种动作,所以在这里选择
			 * 发射子弹的可能性是走一步的八分之一
			 */
			int key = (int) (Math.random() * 8);
			switch (key) {
			case 0:
				int attackDirection = attack();
				if (attackDirection != -1 && mt.isAlive) {
					this.tankDirection = attackDirection;
				}
				// 发射子弹
				 this.creatBullet();
				break;
			//走路
			default:
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/**
				 * 这里利用moveDirection,来取得可以走的方向。
				 * 如若没有则返回null
				 */
				int[] arr = moveDirection();
				//如若无路可走,那就直接跳出switch。也就相当于暂停。
				if (arr == null) {
					break;
				}
				else if (arr.length == 1) {
					this.setTankDirection(arr[0]);
				} 
				//如若有多条路,则需要随机产生出了个方向来
				else {
					int i;
					key = -1; //这里的key仍然是用于switch
					for (i = 0; i < arr.length; i++) {
						if (this.tankDirection == arr[i]) {
							// 除了 0 - x 还可能出现负数
							key = (int) (Math.random() * arr.length) - (int) (Math.random() * 50);
							break;
						}
					}
					if (i == arr.length) {
						key = (int) (Math.random() * arr.length);
					}
					//设置了当前坦克需要移动的方向
					switch (key) {
					case 0:
						this.setTankDirection(arr[0]);
						break;
					case 1:
						this.setTankDirection(arr[1]);
						break;
					case 2:
						this.setTankDirection(arr[2]);
						break;
					case 3:
						this.setTankDirection(arr[3]);
						break;
					default:
						this.setTankDirection(lastDirection);
						break;
					}
				}
				//真正地修改了坦克坦克的坐标
				switch(this.tankDirection) {
				case Tank.UP:
					this.moveUp();
					break;
				case Tank.DOWM:
					this.moveDown();
					break;
				case Tank.RIGHT:
					this.moveRight();
					break;
				case Tank.LEFT:
					this.moveLeft();
					break;
				}
				break;
			}
		}
	}
}

class MyTank extends Tank {
	/**
	 * 设置我方坦克的血量
	 * @param live
	 */
	public void setLive (int live) {
		this.tankBlood = live;
	}
	
	/**
	 * 改变坦克颜色,调用了下面的同名带参方法,这也是一般模式下的掉血方法。
	 */
	public void dropOfBlood() {
		dropOfBlood(1);
	}

	/**
	 * 重写changeColor方法
	 */
	public void dropOfBlood(int blood) {
		//使得坦克颜色在红色和粉色之间变换 起到一个提醒掉血了的效果
		if (this.tankColor != Tank.RED) 
			this.tankColor = Tank.RED;
		else
			this.tankColor = Tank.PINK;
		this.tankBlood -= blood;
		//如若血量小于等于0 则坦克死亡
		if (this.tankBlood <= 0) {
			this.isAlive = false;
			this.tankBlood = 0;
		}
	}
	
	// 构造函数初始化
	// 我方坦克不需要选择颜色 不需要选择方向 需要传入坐标
	public MyTank(int x, int y, int blood) {
		super(x, y, MyTank.UP, MyTank.RED, 2, blood);
	}
	//用于恢复数据(读档进入游戏)时调用这个方法传入我方子弹的数据，与上一个区别在多了方向 颜色 速度 子弹数组的设定
	public MyTank (int x,int y,int tankDirection,int tankColor,int speed, int blood, Vector<Bullet> Bullets) {
		super(x, y, tankDirection, tankColor, speed, blood, Bullets);
	}
}

/**
 * 这是子弹类,记录了子弹的数据并且以每个子弹为线程实现自增
 * @author HP
 *
 */
class Bullet implements Runnable {
	//速度等级
	public static final int SLOW = 1;
	public static final int NORMAL = 3;
	public static final int FAST = 5;
	//方向
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public int bulletBlood;
	
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
	
	//子弹是否暂停
	boolean isStop = false;
	
	/**
	 * 用于设置子弹颜色的函数,一般传入事先定义好的颜色静态常量
	 * @param color
	 */
	public void setColor(int color) {
		this.bulletColor = color;
	}
	
	/**
	 * 一般模式 : 参照下面特殊模式规则,子弹血量设定为1。所以子弹无论如何直接死亡
	 */
	public void dropOfBlood() {
		this.isAlive = false;
	}
	/**
	 * 特殊模式 : 子弹是具有"血量"的,例如子弹血量有4,而坦克血量有2,则直接把坦克打死
	 * 然而子弹血量还会剩余2,子弹仍然不是,这算是设定。
	 * @param tankBlood
	 */
	public void dropOfBlood(int tankBlood) {
		this.bulletBlood -= tankBlood;
		if (this.bulletBlood <= 0) {
			this.isAlive = false;
		}
	}
	
	/**
	 * 这是画子弹类,用于显示(绘图)线程来调用在画板上显示图像
	 * @param g
	 */
	public void drawBullet(Graphics g){
		switch(this.bulletColor) {
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
		case Tank.PINK:
			g.setColor(Color.PINK);break;
		case Tank.BLACK:
			g.setColor(Color.GRAY);break;
		}
		g.fillOval(x, y, 3, 3);
	}
	/*************************与坦克类相同,这是用于提供暂停功能的属性isStop的修改****************************************/
	/**
	 * 通过修改isStop作为一个信号,使得子弹无法移动
	 * 其中
	 */
	public void stop () {
		isStop = true;
	}
	
	public void move () {
		isStop = false;
	}
	
	//构造函数初始化
	public Bullet(int x,int y,int bulletColor,int bulletSpeed,int bulletDirection, int bulletBlood) {
		this.x = x;
		this.y = y;
		this.bulletColor = bulletColor;
		this.bulletDirection = bulletDirection;
		this.bulletSpeed = bulletSpeed;
		this.bulletBlood = bulletBlood;
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
			//根据子弹的方向 对数据进行不同的修改
			switch (this.bulletDirection) {
			case Bullet.UP:
				if(!isStop)
					this.y -= this.bulletSpeed;
				break;
			case Bullet.DOWM:
				if(!isStop)
					this.y += this.bulletSpeed;
				break;
			case Bullet.LEFT:
				if(!isStop)
					this.x -= this.bulletSpeed;
				break;
			case Bullet.RIGHT:
				if(!isStop)
					this.x += this.bulletSpeed;
				break;
			}
			//如若出了边框,则代表子弹死亡
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
			try {
				img[i] = ImageIO.read(Bomb.class.getClassLoader().getResourceAsStream("Image\\explode\\e" + (16 - i) + ".gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//画炸弹
	public void drawBomb (Graphics g, MyPanel mp) {
		//不需要循环的原因是g.drawImage(img[bloodValue],this.x,this.y,mp);传入的第一个数据是数组的情况下会自动调用repoint();
		if (bloodValue >= 0) {
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

//记录类
class Record{
	//我方坦克血量
	private static int myTankBlood = 5;
	//各种颜色坦克的初始值
	//黑 黄 蓝 绿 白
	private static int[] colorArr = new int[5];
	private static boolean isSpecialMode = false;
	private static int hostileTankNumber = 0;
	private static int highestScore = 0;
	private static int score = 0;
	//记录如何初始化的以文件夹src为根目录的文件路径。
	private static String recordPath = "Record\\record.txt";
	//记录最高分文件的以文件夹src为根目录的文件路径。
	private static String highestScoreRecordPath = "Record\\highestScoreRecord.txt";
	//记录之前存档的数据的以文件夹src为根目录的文件路径。
	private static String recorderPath = "Record\\recorder.txt";
	private static boolean isStop = false;
	
	
	/**
	 * 凭借文件"Record\\record.txt"
	 * 来设置初始化的个数 
	 * 这个文件里只有三行 
	 * 第一行 : 我方坦克的初始血量
	 * 第二行 : 以颜色为基础,各种颜色的坦克分别占据多少个(一共有五种颜色黑 黄 蓝 绿 白)
	 * 第三行 : 模式的改变,其中0代表的是一次只打一格血,第二种模式是一次打子弹血量那么多血
	 */
	public static void initData () {
		InputStream file = null;
		//先读入敌军各颜色坦克数量以及我方坦克的血量
		InputStream highestScoreFile = null;
		Scanner scanner = null, highestScoreScanner = null;
		try {
			//以文件夹src为根目录,传入文件路径。
			file = Record.class.getClassLoader().getResourceAsStream(recordPath);
			scanner = new Scanner(file);
			myTankBlood = scanner.nextInt();
			for (int i = 0; i < 5; i++) {
				colorArr[i] = scanner.nextInt();
				hostileTankNumber += colorArr[i];
			}
			isSpecialMode = scanner.nextInt() != 0;
			highestScoreFile = Record.class.getClassLoader().getResourceAsStream(highestScoreRecordPath);
			highestScoreScanner = new Scanner(highestScoreFile);
			highestScore = highestScoreScanner.nextInt();
		}finally {
			if (scanner != null)		
				scanner.close();
			if (highestScoreFile != null) {
				try {
					highestScoreFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (highestScoreScanner != null) 
				highestScoreScanner.close();
		}
	}

	/**
	 * 用于存盘退出
	 */
	public static void keepRecording(MyTank mt, Vector<HostileTank> hts) {
		File file = new File(recorderPath);
		StringBuilder r = new StringBuilder();
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			r.append(makeTankDataString(mt));
			r.append(Record.hostileTankNumber).append("\r\n");
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			for (HostileTank ht : hts) {
				if (ht.isAlive) {
					r.append(makeTankDataString(ht));
				}
			}
			bw.write(r.toString() + Record.score);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 制作单条坦克的数据,以字符串形式返回
	 * 第一行分别为 : int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood
	 * 接下来是 Bullets的长度.
	 * @param tank
	 * @return
	 */
	private static String makeTankDataString (Tank tank) {
		StringBuilder res = new StringBuilder();
		if (!tank.isAlive) {
			return res.toString();
		}
		res.append(tank.x).append(' ').append(tank.y).append(' ').append(tank.tankDirection).append(' ').append(tank.tankColor).append(' ').append(tank.tankSpeed).append(' ').append(tank.getLive()).append("\r\n");
		int htNum = 0;
		StringBuilder s = new StringBuilder();
		for (Bullet ht : tank.Bullets) {
			if (ht.isAlive) {
				htNum++;
				s.append(makeBulletDataString(ht)).append("\r\n");
			}
		}res.append(htNum).append("\r\n").append(s);
		return res.toString();
	}
	
	/**
	 * 制作单条子弹的信息 : int x,int y,int bulletColor,int bulletSpeed,int bulletDirection, int bulletBlood
	 * @param bullet
	 * @return
	 */
	private static String makeBulletDataString (Bullet bullet) {
		if (!bullet.isAlive) {
			return "";
		}
		return bullet.x + " " + bullet.y + " " + bullet.bulletColor + " " + bullet.bulletSpeed + " " + bullet.bulletDirection + " " + bullet.bulletBlood + "\r\n";
	}
	/**
	 * 读取上一次存盘
	 */
	public static FileReader readRecording() {
		//恢复上次非坦克数据
		File file = new File(recorderPath);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return fr;
	}

	public static int[] getColorArr() {
		// 先拷贝一份颜色数组
		int[] arr = new int[colorArr.length];
		for (int i = 0; i < colorArr.length; i++) {
			arr[i] = colorArr[i];
		}
		return arr;
	}
	public static int getHighestScore() {
		return highestScore;
	}
	public static void setHighestScore(int highestScore) {
		Record.highestScore = highestScore;
	}
	//更新最高分
	public static void updataHighestScore() {
		FileWriter fw = null;
		BufferedWriter bw = null;
		File file = new File(highestScoreRecordPath);
		//读取出当前数
		try {
			if (score > highestScore) {
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				String s = score + "\r\n";
				bw.write(s);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/***************************************以下是一系列修改数据的方法**********************************************/
	public static int getScore() {
		return score;
	}
	public static void setScore(int score) {
		Record.score = score;
	}
	public static void addScore(int score) {
		Record.score += score;
	}
	public static boolean isSpecialMode() {
		return isSpecialMode;
	}
	public static void setSpecialMode(boolean isSpecialMode) {
		Record.isSpecialMode = isSpecialMode;
	}
	public static int getMyTankBlood() {
		return myTankBlood;
	}
	public static void setMyTankBlood(int myTankBlood) {
		Record.myTankBlood = myTankBlood;
	}
	public static int getHostileTankNumber() {
		return hostileTankNumber;
	}
	public static void setHostileTankNumber(int hostileTankNumber) {
		Record.hostileTankNumber = hostileTankNumber;
	}
	public static boolean isStop() {
		return isStop;
	}
	public static void setStop(boolean isStop) {
		Record.isStop = isStop;
	}
	public static void reduceHostileTankNumber() {
		hostileTankNumber--;
	}
}


/**
 * Badge只提供两张图片的画法,这两张图片就是游戏里的两个皇冠
 * @author HP
 *
 */
class Badge{
	//最高分显示的皇冠的以文件夹src为根目录的文件路径。
	private static String kingFilePath = "111.jpg";
	//当前的得分显示的皇冠的以文件夹src为根目录的文件路径。
	private static String filePath = "222.jpg";
	/**
	 * @param x 
	 * @param y : 上两个参数是坐标
	 * @param g : 画笔
	 * @param mp : 画在哪个画板上
	 */
	public static void drawKingBadge(int x, int y, Graphics g, MyPanel mp) {
		try {
			InputStream kingBadgeImage = Badge.class.getClassLoader().getResourceAsStream(kingFilePath);
			Image image = ImageIO.read(kingBadgeImage);
			g.drawImage(image, x, y, x + 56, y + 52, 0, 0, 280, 260, mp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void drawBadge(int x, int y, Graphics g, MyPanel mp) {
		try {
			InputStream badgeImage = Badge.class.getClassLoader().getResourceAsStream(filePath);
			Image image = ImageIO.read(badgeImage);
			g.drawImage(image, x, y, x + 56, y + 52, 0, 0, 280, 260, mp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}