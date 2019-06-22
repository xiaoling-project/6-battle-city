package cn.vizdl.MyTankGame5_2;

import java.awt.*;
import java.awt.datatransfer.FlavorListener;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.naming.ldap.Control;
import javax.sound.midi.ControllerEventListener;
import javax.swing.*;

import org.omg.CORBA.PUBLIC_MEMBER;




class Controler extends Thread{
	static final int HIGH=10;
	static final int MIDUM=40;
	static final int SLOW=70;
	
	int capcity;
	boolean full;
	Vector<HostileTank> hts;

	int index;
	int addnumber;
	
	public Controler(){
		hts=new Vector<HostileTank>();
		this.full=false;
		this.addnumber=0;
		this.capcity=10;
		this.index=0;
	}
	
	void addTank(HostileTank tank) {
		hts.add(tank);
		tank.setControler(this);
		if(addnumber++==capcity-1) full=true;
		
	}
	
	void removeTank(HostileTank tank) {
		hts.remove(tank);
		index--;
	}
	
	int getCapcity() {
		return this.capcity;
	}
	
	public void run() {
		while(full==false||hts.size()!=0) {
			index=0;
			for(;index<hts.size();index++) {
				hts.get(index).tankDo();
			}
			try {
				Thread.sleep(MIDUM);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("success");
	}
	
}


class TankCreate extends Thread{
	final static int TIME=5000;
	Controler newcontroler;
	HostileTank newHt;
	
	public TankCreate() {
		this.newcontroler=new Controler();
	}
	public void run() {
		newcontroler.start();
		while(MyPanel.allnumber!=0) {
			try {
				Thread.sleep(TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(newcontroler.full==true) {
				newcontroler=new Controler();
				newcontroler.start();
			}
			if(MyPanel.color==5) break;	
			if (MyPanel.colorArr[MyPanel.color]--==0) MyPanel.color++; 
			int newcolor=MyPanel.color+Tank.YELLOW;
			newHt=new HostileTank(400,((int)(Math.random()*1000%6))*50
					, HostileTank.LEFT ,newcolor , 2, Tank.TankColorToLiveTable(newcolor));
			newHt.setHts(MyPanel.hts);
			newHt.setMt(MyPanel.mt);
			newcontroler.addTank(newHt);
			MyPanel.hts.add(newHt);
			}
	}
}



public class MyTankGame6 extends JFrame implements ActionListener{

	MyPanel mp = null;

	MyStartPanel msp = null;

	JMenuBar jmb = null;

	JMenu jm1 = null;


		JMenuItem jmi = null;

		JMenuItem jmi2 = null;

		JMenuItem jmi3 = null;

		JMenuItem jmi4 = null;

	public MyTankGame6(){
		msp = new MyStartPanel();
		this.add(msp);
		

		jmb = new JMenuBar();

		jm1 = new JMenu("游戏(G)");

		jm1.setMnemonic('G');

		jmi = new JMenuItem("开始新游戏(H)");
		jmi.setMnemonic('H');
		jmi4 = new JMenuItem("读档开始(U)");
		jmi4.setMnemonic('U');
		jmi3 = new JMenuItem("存档退出(F)");
		jmi3.setMnemonic('F');
		jmi2 = new JMenuItem("退出游戏(E)");
		jmi2.setMnemonic('E');

		jmi.addActionListener(this);
		jmi2.addActionListener(this);
		jmi3.addActionListener(this);
		jmi4.addActionListener(this);

		jmi.setActionCommand("playGame");
		jmi2.setActionCommand("exitGame");
		jmi3.setActionCommand("recordAndExit");
		jmi4.setActionCommand("playAndRead");

		jm1.add(jmi);
		jm1.add(jmi4);
		jm1.add(jmi3);
		jm1.add(jmi2);
		jmb.add(jm1);
		this.setJMenuBar(jmb);
		
		
		Thread t = new Thread(msp);
		t.start();

		

		this.setTitle("TankGame");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	

	public static void main (String[] args) {
		new MyTankGame6();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if (command.equals("playGame") || command.equals("playAndRead")) {

			this.remove(msp);
			

			if (command.equals("playAndRead")) {
				mp = new  MyPanel(Record.readRecording());
			}else {
				mp = new MyPanel();
			}
			

			this.add(mp);
			

			this.addKeyListener(mp);
			

			Thread t = new Thread (mp);
			t.start();
			

			this.setVisible(true);
		}else if (command.equals("exitGame") || command.equals("recordAndExit")) {
			Record.updataHighestScore();
			if (command.equals("recordAndExit")) {
				Record.keepRecording(mp.mt, mp.hts);
			}
			System.exit(0);
		}
	}
}


class MyStartPanel extends JPanel implements Runnable {

	public void paint (Graphics g){
		g.fillRect(0, 0, 400, 300);
		times++;
		if ((times & 1) != 0) {
			g.setColor(Color.YELLOW);
			Font myFont = new Font("华文新魏",Font.BOLD, 30);
			g.setFont(myFont);
			g.drawString("Stage : 1", 150, 150);
		}
	}

	int times = 0;
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			this.repaint();
		}
	}
}


class Move implements Runnable {

	final static int FAST=10;
	final static int MIDIUM=40;
	final static int SLOW=70;
	
	MyTank mt = null;	
	public Move(MyTank mt) {
		this.mt = mt;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(MIDIUM);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			for (Bullet bu : mt.Bullets) {
				bu.move();
			}
		}
	}
}


class Operation implements Runnable{
	MyTank mt = null;
	 Vector<HostileTank> hts = null;
	
	public Operation(MyTank mt, Vector<HostileTank> hts) {
		this.mt = mt;
		 this.hts = hts;
	}
	
	@Override
	public void run() {
		while (true) {

			this.hts=new Vector<HostileTank>(MyPanel.hts);

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (HostileTank ht : hts) {

				if (ht.isAlive) {
					for (int j = 0; j < mt.Bullets.size(); j++) {

						Bullet bu = mt.Bullets.get(j);

						if (bu.isAlive) {
							this.hitTank(ht, bu, false);
						}
					}
				}
			}
	

			for (HostileTank ht : hts) {

				for (int j = 0; j < ht.Bullets.size(); j++) {

					Bullet bu = ht.Bullets.get(j);

					if (bu.isAlive && mt.isAlive) {
						this.hitTank(mt, bu, true);
					}
				}
			}
		}
	}
	
	

	public void hitTank(Tank tank, Bullet bullet, boolean isMyTank) {

		if (bullet.x >= tank.x && bullet.x < (tank.x + 30) && bullet.y >= tank.y && bullet.y <= (tank.y + 30)){

			if (tank.isAlive) {
				
				if (!isMyTank) {

					if (Record.isSpecialMode()) {
						Record.addScore(bullet.bulletBlood < tank.tankBlood ? bullet.bulletBlood : tank.tankBlood);
					}else {
						Record.addScore(1);
					}
				}
				
				if (Record.isSpecialMode()) {
					tank.dropOfBlood(bullet.bulletBlood);
					bullet.dropOfBlood(tank.tankBlood);
				}else {
					tank.dropOfBlood();
					bullet.dropOfBlood();
				}

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




class MyPanel extends JPanel implements KeyListener, Runnable{

	static MyTank mt;
	

	MyTank myTankImage;
	Tank hostileTankImages;
	
	
	static int[] colorArr;
	static int allnumber;
	final static int startnumber=8;
	static int  color=0;
	Controler startControler;
	TankCreate creator;
	boolean draw;
	


	static Vector<HostileTank> hts = new Vector<HostileTank>();

	public MyPanel(FileReader reader) {
		Scanner scanner = null;
		Record.initData();
		try {
			scanner = new Scanner(reader);

			int x = scanner.nextInt();
			int y = scanner.nextInt();
			int tankDirection = scanner.nextInt();
			int tankColor = scanner.nextInt();
			int tankSpeed = scanner.nextInt();
			int blood = scanner.nextInt();
			Vector<Bullet> bullet = new Vector<Bullet>();
			int m = scanner.nextInt();

			for (int i = 0; i < m; i++) {
				Bullet bu = new Bullet(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
						scanner.nextInt(), scanner.nextInt());
				bullet.add(bu);
			}
			mt = new MyTank(x, y, tankDirection, tankColor, tankSpeed, blood, bullet);

			myTankImage = new MyTank(50, 350, 1);
			
			
			Record.setHostileTankNumber(scanner.nextInt());
			for (int i = 0; i < Record.getHostileTankNumber(); i++) {

				x = scanner.nextInt();
				y = scanner.nextInt();
				tankDirection = scanner.nextInt();
				tankColor = scanner.nextInt();
				tankSpeed = scanner.nextInt();
				blood = scanner.nextInt();
				bullet = new Vector<Bullet>();
				m = scanner.nextInt();

				for (int i1 = 0; i1 < m; i1++) {
					Bullet bu = new Bullet(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
							scanner.nextInt(), scanner.nextInt());
					bullet.add(bu);
				}

				HostileTank newHt = new HostileTank(x, y, tankDirection, tankColor, tankSpeed, blood, bullet);
				newHt.setHts(hts);
				newHt.setMt(mt);
	

				hts.add(newHt);
			}

			

			Record.setScore(scanner.nextInt());

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

					e.printStackTrace();
				}
		}
	}


	public MyPanel() {
		
		draw=true;

		Controler startControler=new Controler(); 
		creator=new TankCreate();
		

		Record.initData();

		mt = new MyTank(100, 200, Record.getMyTankBlood());

		myTankImage = new MyTank(50, 350, 1);
	


		colorArr = Record.getColorArr();
		allnumber=Record.getHostileTankNumber();
		
		for (int i = 0; i <startnumber; i++) {
			
			if(--colorArr[color]==0) color++;
			
			int nowTankColor = color + Tank.YELLOW;

			HostileTank newHt = new HostileTank(i * 50, 0, HostileTank.DOWM, nowTankColor, 1,
						Tank.TankColorToLiveTable(nowTankColor));
			newHt.setHts(hts);
			newHt.setMt(mt);

			hts.add(newHt);
			startControler.addTank(newHt);
		}
		startControler.start();

		hostileTankImages = new Tank(180, 350, Tank.UP, Tank.BLACK, 0, 1);
		

		this.startOperation();
		
		this.startMove();
		
		creator.start();
		
		
	}
	
	
	public void startMove() {
		Move m = new Move(mt);
		Thread t = new Thread(m);
		t.start();
	}
	

	public void startOperation() {
		Operation opt = new Operation(mt,hts);
		Thread t = new Thread(opt);
		t.start();
	}
	


	public void repaint(Graphics g) {

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


		this.drowInfo(g);


		if (mt.isAlive == true) {
			mt.drawTank(g);
		}


		if (!mt.isAlive && mt.bomb.isAlive) {

			mt.bomb.setX(mt.getX());
			mt.bomb.setY(mt.getY());

			mt.bomb.drawBomb(g, this);
			if (mt.bomb.bloodValue >= 16) {
				mt.bomb.isAlive = false;
			}
		}


		for (HostileTank ht : hts) {
			if (ht.isAlive) {
				ht.drawTank(g);
			}


			else if (!ht.isAlive && ht.bomb.isAlive) {

				ht.bomb.setX(ht.getX());
				ht.bomb.setY(ht.getY());

				ht.bomb.drawBomb(g, this);
				if (ht.bomb.bloodValue < 0) {
					ht.bomb.isAlive = false;
				}
			}


			for (int k = 0; k < ht.Bullets.size(); k++) {
				Bullet bu = ht.Bullets.get(k);
				if (bu != null && bu.isAlive) {
					bu.drawBullet(g);
				}

				if (bu.isAlive == false) {
					ht.Bullets.remove(bu);
				}
			}
		}


		for (int i = 0; i < mt.Bullets.size(); i++) {
			Bullet bu = mt.Bullets.get(i);
			if (bu != null && bu.isAlive) {
				bu.drawBullet(g);
			}

			if (bu.isAlive == false) {
				mt.Bullets.remove(bu);
			}
		}
	}
	

	public void drowInfo(Graphics g) {

		 myTankImage.drawTank(g);	
		g.setColor(Color.RED);
		g.drawString("我方血量 : " + Record.getMyTankBlood(), 30, 400);
		 hostileTankImages.drawTank(g);
		g.setColor(Color.orange);
		g.drawString("敌方坦克数量 : " + Record.getHostileTankNumber(), 150, 400);

		 Badge.drawKingBadge(300, 340, g, this);
		g.drawString("历史最高得分 : " + Record.getHighestScore(), 285, 400);

		Badge.drawBadge(450, 340, g, this);
		g.drawString("当前得分 : " + Record.getScore(), 447, 400);
	}


	

	public boolean hasntHostileTank (int Direction) {

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
		

		for (HostileTank ht : hts) {
			if (ht.isAlive && !(ht.x > x2 || ht.x + 30 < x1) && !(ht.y > y2 || ht.y + 30 < y1)) {
				return false;
			}
		}return true;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {


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

		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		
	}

	@Override
	public void run() {
		while(true) {

			try {
				Thread.sleep(50);

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			this.repaint();
		}
	}

}



class Tank{


	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;


	public static final int RED = 1000;
	public static final int PINK = 1001;

	public static final int BLACK = 15;
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;

	boolean isAlive = true;
	

	int tankBlood;
	

	boolean isStop = false;


	Bomb bomb = null;
	

	int x;
	int y;

	int tankDirection;

	int tankColor;

	int tankSpeed;


	Vector<Bullet> Bullets = null;




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
	

	public void dropOfBlood () {
		dropOfBlood(1);
	}
	

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
	public void stop() {
		isStop = true;

		for (Bullet b : Bullets) 
			b.stop();
	}
	
	public void move() {
		isStop = false;
		for (Bullet b : Bullets) 
			b.startMove();
	}
	

	public void creatBullet() {
		if (!isStop) {

			Bullet bu = null;

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
			Bullets.add(bu);
		}
	}



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
			g.setColor(Color.orange);
			break;
		}
		switch (this.tankDirection) {

		case Tank.UP:

			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x, this.y+5, 5, 5, false);
			g.fill3DRect(this.x, this.y+10, 5, 5, false);
			g.fill3DRect(this.x, this.y+15, 5, 5, false);
			g.fill3DRect(this.x, this.y+20, 5, 5, false);
			g.fill3DRect(this.x, this.y+25, 5, 5, false);

			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+5, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+10, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+15, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+20, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);

			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			

			g.fillOval(this.x+10, this.y+10, 10, 10);

			g.drawLine(this.x+15, this.y+15, this.x+15, this.y-10);
			break;

		case Tank.DOWM:

			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x, this.y+5, 5, 5, false);
			g.fill3DRect(this.x, this.y+10, 5, 5, false);
			g.fill3DRect(this.x, this.y+15, 5, 5, false);
			g.fill3DRect(this.x, this.y+20, 5, 5, false);
			g.fill3DRect(this.x, this.y+25, 5, 5, false);

			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+5, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+10, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+15, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+20, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);

			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			

			g.fillOval(this.x+10, this.y+10, 10, 10);

			g.drawLine(this.x+15, this.y+15, this.x+15, this.y+40);
			break;
		case Tank.LEFT:

			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x+5, this.y, 5, 5, false);
			g.fill3DRect(this.x+10, this.y, 5, 5, false);
			g.fill3DRect(this.x+15, this.y, 5, 5, false);
			g.fill3DRect(this.x+20, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y, 5, 5, false);

			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+5, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+10, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+15, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+20, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);

			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			

			g.fillOval(this.x+10, this.y+10, 10, 10);

			g.drawLine(this.x+15, this.y+15, this.x-10, this.y+15);
			break;
		case Tank.RIGHT:

			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x+5, this.y, 5, 5, false);
			g.fill3DRect(this.x+10, this.y, 5, 5, false);
			g.fill3DRect(this.x+15, this.y, 5, 5, false);
			g.fill3DRect(this.x+20, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y, 5, 5, false);

			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+5, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+10, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+15, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+20, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);

			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			

			g.fillOval(this.x+10, this.y+10, 10, 10);

			g.drawLine(this.x+15, this.y+15, this.x+40, this.y+15);
			break;
		}
	}	
	

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
		if (!this.isStop) {
			this.tankDirection = tankDirection;
		}
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
}


class HostileTank extends Tank{

	public static final int VIEW_LENGTH = 15;
	
	

	Controler belong;
	


	Vector<HostileTank> hts = new Vector<HostileTank>();
	

	MyTank mt = null;
	


	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood, Vector<Bullet> bullet) {
		super(x, y, tankDirection, tankColor,tankSpeed, blood, bullet);

	}
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood) {
		super(x, y, tankDirection, tankColor,tankSpeed, blood);
	}

	public void setHts(Vector<HostileTank> hts) {
		this.hts = hts;
	}

	public void setMt (MyTank mt) {
		this.mt = mt;
	}
	

	public int attack () {

		if ( (this.x + 15) >= mt.x && (this.x + 15) <= mt.x + 30 ) {

			if (this.y > mt.y) {
				return Tank.UP;
			}else {
				return Tank.DOWM;
			}
		}else if ( (this.y + 15) >= mt.y && (this.y + 15) <= mt.y + 30 ) {

			if (this.x > mt.x + 30) {
				return Tank.LEFT;
			}else {
				return Tank.RIGHT;
			}
		}
		return -1;
	}
	

	public int[] moveDirection () {
		int[] arr = {0, 1, 2, 3};
		
		int key = 4;

		if (this.x <= 0){
			if (this.searchKey(arr, key, Tank.LEFT)) {
				key--;
			}
		}
		if (this.y <= 0) {
			if (this.searchKey(arr, key, Tank.UP)) {
				key--;
			}
		}
		if (this.x >= 370) {
			if (this.searchKey(arr, key, Tank.RIGHT)) {
				key--;
			}
		}
		if (this.y >= 270) {
			if (this.searchKey(arr, key, Tank.DOWM)) {
				key--;
			}
		}
		

		int[] wall = new int[key];
		for (int i = 0; i < key; i++) {
			wall[i] = arr[i];
		}
		

		if (mt.isAlive) {

			if ( ( mt.x >= (this.x + 30) && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y - HostileTank.VIEW_LENGTH && mt.y <= (this.y + 30) + HostileTank.VIEW_LENGTH) ||
			(  mt.x >= (this.x + 30) && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (mt.y + 30) <= (this.y + 30 + HostileTank.VIEW_LENGTH)) ) {
				if (this.searchKey(arr, key, Tank.RIGHT)) {
					key--;
				}
			}

			if ( ( (mt.x + 30) <= this.x && (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && mt.y >= this.y - HostileTank.VIEW_LENGTH && mt.y <= (this.y + 30) + HostileTank.VIEW_LENGTH ) || 
			( (mt.x + 30) <= this.x && (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (mt.y + 30) <= (this.y + 30) + HostileTank.VIEW_LENGTH ) ) {
				if (this.searchKey(arr, key, Tank.LEFT)) {
					key--;
				}
			}

			if ( (mt.x >= this.x - HostileTank.VIEW_LENGTH && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) <= this.y && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH) ||
			((mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) <= this.y && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH)) {
				if (this.searchKey(arr, key, Tank.UP)) {
					key--;
				}
			}

			if (( mt.x >= this.x - HostileTank.VIEW_LENGTH && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y + 30 && mt.y <= this.y + 30 + HostileTank.VIEW_LENGTH)||
			( (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y + 30 && mt.y <= this.y + 30 + HostileTank.VIEW_LENGTH )) {
				if (this.searchKey(arr, key, Tank.DOWM)) {
					key--;
				}
			}
		}
		

		for (int i = 0; i < hts.size(); i++) {

			HostileTank ht = hts.get(i);
			if (ht != this && ht.isAlive) {

				if ( ( ht.x >= (this.x + 30) && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y - HostileTank.VIEW_LENGTH && ht.y <= (this.y + 30) + HostileTank.VIEW_LENGTH) ||
				(  ht.x >= (this.x + 30) && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (ht.y + 30) <= (this.y + 30 + HostileTank.VIEW_LENGTH)) ) {
					if (this.searchKey(arr, key, Tank.RIGHT)) {
						key--;
					}
				}

				if ( ( (ht.x + 30) <= this.x && (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && ht.y >= this.y - HostileTank.VIEW_LENGTH && ht.y <= (this.y + 30) + HostileTank.VIEW_LENGTH ) || 
				( (ht.x + 30) <= this.x && (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (ht.y + 30) <= (this.y + 30) + HostileTank.VIEW_LENGTH ) ) {
					if (this.searchKey(arr, key, Tank.LEFT)) {
						key--;
					}
				}

				if ( (ht.x >= this.x - HostileTank.VIEW_LENGTH && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) <= this.y && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH) ||
				((ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) <= this.y && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH)) {
					if (this.searchKey(arr, key, Tank.UP)) {
						key--;
					}
				}

				if (( ht.x >= this.x - HostileTank.VIEW_LENGTH && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y + 30 && ht.y <= this.y + 30 + HostileTank.VIEW_LENGTH)||
				( (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y + 30 && ht.y <= this.y + 30 + HostileTank.VIEW_LENGTH )) {
					if (this.searchKey(arr, key, Tank.DOWM)) {
						key--;
					}
				}
			}
		}
		

		int[] res = null;
		if (key == 0) {

			return res;
		}else {
			res = new int[key];
			for (int i = 0; i < key; i++) {
				res[i] = arr[i];
			}
		}
		return res;
	}
	

	private boolean searchKey (int[] arr, int vaildSize, int value) {
		for (int i = 0; i < vaildSize; i++) {
			if (value == arr[i]) {

				arr[i] = arr[vaildSize - 1];
				arr[vaildSize - 1] = value;
				return true;
			}
		}
		return false;
	}
	
































































































	

	public void tankDo() {


		if(isAlive || Bullets.size() != 0) {

				for (Bullet bu : Bullets) {
					if (bu.isAlive)
						bu.move();
				}
			if (isAlive) {
				int lastDirection = this.tankDirection;
				int key = (int) (Math.random() * 8);
				switch (key) {
				case 0:
					if (this.isStop) {
						break;
					}
					int attackDirection = attack();
					if (attackDirection != -1 && mt.isAlive) {
						this.tankDirection = attackDirection;
					}

					this.creatBullet();
					break;
				default:


					int[] arr = moveDirection();
					if (arr == null) {
						break;
					} else if (arr.length == 1) {
						this.setTankDirection(arr[0]);
					} else {
						int i;
						int temp = -1;
						for (i = 0; i < arr.length; i++) {
							if (this.tankDirection == arr[i]) {

								temp = (int) (Math.random() * arr.length) - (int) (Math.random() * 50);
								break;
							}
						}
						if (i == arr.length) {
							temp = (int) (Math.random() * arr.length);
						}

						if (isStop) {
							temp = 1000;
						}
						switch (temp) {
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

					switch (this.tankDirection) {
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

		else {
			belong.removeTank(this);
		}
	}
	
	public void setControler(Controler belong) {
		this.belong=belong;
	}
}

class MyTank extends Tank {

	public void setLive (int live) {
		this.tankBlood = live;
	}
	

	public void dropOfBlood() {
		dropOfBlood(1);
	}


	public void dropOfBlood(int blood) {
		if (this.tankColor != Tank.RED)
			this.tankColor = Tank.RED;
		else
			this.tankColor = Tank.PINK;
		this.tankBlood -= blood;
		if (this.tankBlood <= 0) {
			this.isAlive = false;
			this.tankBlood = 0;
		}
	}
	


	public MyTank(int x, int y, int blood) {
		super(x, y, MyTank.UP, MyTank.RED, 2, blood);
	}
	
	public MyTank (int x,int y,int tankDirection,int tankColor,int speed, int blood, Vector<Bullet> Bullets) {
		super(x, y, tankDirection, tankColor, speed, blood, Bullets);
	}
}

class Bullet {

	public static final int SLOW = 1;
	public static final int NORMAL = 3;
	public static final int FAST = 5;

	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public int bulletBlood;
	

	int x;
	int y;
	

	int bulletColor;
	

	int bulletSpeed;

	int bulletDirection;
	

	boolean isAlive = true;
	

	boolean isStop = false;
	
	public void setColor(int color) {
		this.bulletColor = color;
	}
	

	public void dropOfBlood() {
		this.isAlive = false;
	}

	public void dropOfBlood(int tankBlood) {
		this.bulletBlood -= tankBlood;
		if (this.bulletBlood <= 0) {
			this.isAlive = false;
		}
	}
	

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
	
	public void stop () {
		isStop = true;
	}
	
	public void startMove () {
		isStop = false;
	}
	

	public Bullet(int x,int y,int bulletColor,int bulletSpeed,int bulletDirection, int bulletBlood) {
		this.x = x;
		this.y = y;
		this.bulletColor = bulletColor;
		this.bulletDirection = bulletDirection;
		this.bulletSpeed = bulletSpeed;
		this.bulletBlood = bulletBlood;
	}

	public void move() {

		if (this.isAlive) {
			switch (this.bulletDirection) {
			case Bullet.UP:
				if (!isStop)
					this.y -= this.bulletSpeed;
				break;
			case Bullet.DOWM:
				if (!isStop)
					this.y += this.bulletSpeed;
				break;
			case Bullet.LEFT:
				if (!isStop)
					this.x -= this.bulletSpeed;
				break;
			case Bullet.RIGHT:
				if (!isStop)
					this.x += this.bulletSpeed;
				break;
			}
			if (this.x >= 400 || this.x <= 0 || this.y >= 300 || this.y <= 0) {
				this.isAlive = false;
			}
		}
	}
	




































}


class Bomb{

	int x;
	int y;
	

	int bloodValue = 15;
	

	boolean isAlive = true;
	



	static Image[] img = new Image[16];
	


	static {
		for	(int i = 0; i < 16; i++) {
			try {
				img[i] = ImageIO.read(new File("E:\\java\\MyselfGame\\src\\Image\\explode\\e" + (16 - i) + ".gif"));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	

	public void drawBomb (Graphics g, MyPanel mp) {

		if (bloodValue >= 0) {
			g.drawImage(img[bloodValue],this.x,this.y,mp);
			bloodValue--;
		}
	}
	
	

	public void setX(int x) {
		this.x = x - 17;
	}
	
	public void setY(int y) {
		this.y = y - 30;
	}
}


class Record{

	private static int myTankBlood = 5;


	private static int[] colorArr = new int[5];
	private static boolean isSpecialMode = false;
	private static int hostileTankNumber = 0;
	private static int highestScore = 0;
	private static int score = 0;
	private static String recordPath = "E:\\java\\MyselfGame\\src\\Record\\record.txt";
	private static String highestScoreRecordPath = "E:\\java\\MyselfGame\\src\\Record\\highestScoreRecord.txt";
	private static String recorderPath = "E:\\java\\MyselfGame\\src\\Record\\recorder.txt";
	private static boolean isStop = false;
	
	
	
	public static void initData () {

		File file = null, highestScoreFile = null;
		Scanner scanner = null, highestScoreScanner = null;
		try {
			file = new File(recordPath);
			scanner = new Scanner(file);
			myTankBlood = scanner.nextInt();
			for (int i = 0; i < 5; i++) {
				colorArr[i] = scanner.nextInt();
				hostileTankNumber += colorArr[i];
			}
			isSpecialMode = scanner.nextInt() != 0;
			highestScoreFile = new File(highestScoreRecordPath);
			highestScoreScanner = new Scanner(highestScoreFile);
			highestScore = highestScoreScanner.nextInt();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}finally {
			if (scanner != null)		
				scanner.close();
		}
	}


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

			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

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
	

	private static String makeBulletDataString (Bullet bullet) {
		if (!bullet.isAlive) {
			return "";
		}
		return bullet.x + " " + bullet.y + " " + bullet.bulletColor + " " + bullet.bulletSpeed + " " + bullet.bulletDirection + " " + bullet.bulletBlood + "\r\n";
	}

	public static FileReader readRecording() {

		File file = new File(recorderPath);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} 
		return fr;
	}

	public static int[] getColorArr() {

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

	public static void updataHighestScore() {
		FileWriter fw = null;
		BufferedWriter bw = null;
		File file = new File(highestScoreRecordPath);

		try {
			if (score > highestScore) {
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				String s = score + "\r\n";
				bw.write(s);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
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

class Badge{
	private static String kingFilePath = "E:\\java\\MyselfGame\\src\\111.jpg";
	private static File kingBadgeImage = new File(kingFilePath);
	private static String filePath = "E:\\java\\MyselfGame\\src\\222.jpg";
	private static File badgeImage = new File(filePath);
	public static void drawKingBadge(int x, int y, Graphics g, MyPanel mp) {
		try {
			Image image = ImageIO.read(kingBadgeImage);
			g.drawImage(image, x, y, x + 56, y + 52, 0, 0, 280, 260, mp);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static void drawBadge(int x, int y, Graphics g, MyPanel mp) {
		try {
			Image image = ImageIO.read(badgeImage);
			g.drawImage(image, x, y, x + 56, y + 52, 0, 0, 280, 260, mp);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}