package cn.vizdl.MyTankGame1;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

/**
 * ����:̹����Ϸ2.0�� 
 * ������ɰ�
 * 1.����̹��
 * 2.�ҵ�̹�˿������������ƶ�(�����ε�̹�˷���ı�̹����̬)
 * 3.�ҵ�̹�˿��Է����ӵ�(һ��)
 * 
 * �޸�:
 * 1.�޸���̹��1.0�汾̹���м�Բ�β������Բ����
 * @author Vizdl
 *
 */

//������
public class MyTankGame2 extends JFrame{
	//����ָ���ʼ��
	MyPanel mp = null;
	//����������ʼ��
	public MyTankGame2(){
		//ָ���ʼ��
		mp = new  MyPanel();
		
		//������
		this.add(mp);
		
		//��Ӽ���
		this.addKeyListener(mp); //���ô��ڼ���������Ϊ���
		
		//���������̣߳������ػ棩
		Thread t = new Thread (mp);
		t.start();
		
		//��������
		this.setTitle("TankGame");
		this.setSize(400, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//������
	public static void main (String[] args) {
		new MyTankGame2();
	}
}




//�������
	//���������Ҫ�м������� ��������ֱ��ͨ��������Ϣ����̹�˶����λ�� ���� ����ɫ
	//�����߳�
class MyPanel extends JPanel implements KeyListener,Runnable{
	//����ָ���ʼ��
	MyTank mt = null;
		//�з�̹�˲�ֹһ�� ���Եö��� �о�̹������ ��һ���� ���� �о�̹�˵�ʹ��������ʵ�ֳ�Ϊһ������
	//�������̹����
	Vector<HostileTank> hts = new Vector<HostileTank>();
	//����̹������
	int htNumber = 3;
	
	//���캯����ʼ��
	public MyPanel() {
		//��������ȡ
		mt = new MyTank(100,100);
		
		for (int i = 0; i < htNumber; i++) {
			//����һ���з�̹�˶���
			HostileTank newHt = new HostileTank(i*50+50,0,HostileTank.DOWM,HostileTank.BLUE,2);
			//���з�̹�˶������з���
			hts.add(newHt);
		}
	}
	
	//��дpaint����
		//paint�ǻ�ͼ����
	public void paint (Graphics g) {
		//����
		g.fillRect(0, 0, 400, 300);
		
		//�ҵ�̹�˻���
		mt.drawTank(g);
		//�з�̹�˻���
		for (int i = 0; i < htNumber; i++) {
			hts.get(i).drawTank(g);
		}
		
		//���ҵ��ӵ�
		if (mt.bu != null && mt.bu.isAlive) {
			mt.bu.drawBullet(g);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		//������Ϣ
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
		
		//ˢ��ͼ��
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
				//ÿ��һ��ʱ���ˢ��
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
		}
	}

}


//����̹�˵Ĺ��Է���Tank����
class Tank{
	//����
		//����
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
		//��ɫ
			//�ҷ�̹��ר����ɫ
	public static final int RED = 10;
			//�з�̹����ɫ
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;
	
	
	//̹������
	int x;
	int y;
	//̹�˷���
	int tankDirection;
	//̹����ɫ
	int tankColor;
	//̹���ٶ�
	int tankSpeed;
	



	//��̹��     Ӧ���Ǿ�̬��ô�� ��Ӧ�� ��ͬ���󻭲�̹ͬ��   û��̹�˶�������̹�� 	һ��̹�˶���һ��̹�ˣ�
		//��Ȼһ����������һ��̹�� ����̹�˶����ڲ�ӵ�����еĲ�������ô��̹�˾ò���Ҫ�������Ĳ�����
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
		//�ڿ�����
		case Tank.UP:
			//����ߵľ��� �����и�Ϊ��������γ��Ĵ�
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x, this.y+5, 5, 5, false);
			g.fill3DRect(this.x, this.y+10, 5, 5, false);
			g.fill3DRect(this.x, this.y+15, 5, 5, false);
			g.fill3DRect(this.x, this.y+20, 5, 5, false);
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			//���ұߵľ���
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+5, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+10, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+15, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+20, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//���м��������
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//���м��Բ
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//���м����
			g.drawLine(this.x+15, this.y+15, this.x+15, this.y-10);
			break;
		//�ڿ�����	
		case Tank.DOWM:
			//����ߵľ��� �����и�Ϊ��������γ��Ĵ�
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x, this.y+5, 5, 5, false);
			g.fill3DRect(this.x, this.y+10, 5, 5, false);
			g.fill3DRect(this.x, this.y+15, 5, 5, false);
			g.fill3DRect(this.x, this.y+20, 5, 5, false);
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			//���ұߵľ���
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+5, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+10, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+15, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+20, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//���м��������
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//���м��Բ 
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//���м����
			g.drawLine(this.x+15, this.y+15, this.x+15, this.y+40);
			break;
		case Tank.LEFT:
			//���ϱߵľ��� �����и�Ϊ��������γ��Ĵ�
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x+5, this.y, 5, 5, false);
			g.fill3DRect(this.x+10, this.y, 5, 5, false);
			g.fill3DRect(this.x+15, this.y, 5, 5, false);
			g.fill3DRect(this.x+20, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			//���±ߵľ���
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+5, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+10, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+15, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+20, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//���м��������
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//���м��Բ
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//���м����
			g.drawLine(this.x+15, this.y+15, this.x-10, this.y+15);
			break;
		case Tank.RIGHT:
			//���ϱߵľ��� �����и�Ϊ��������γ��Ĵ�
			g.fill3DRect(this.x, this.y, 5, 5, false);
			g.fill3DRect(this.x+5, this.y, 5, 5, false);
			g.fill3DRect(this.x+10, this.y, 5, 5, false);
			g.fill3DRect(this.x+15, this.y, 5, 5, false);
			g.fill3DRect(this.x+20, this.y, 5, 5, false);
			g.fill3DRect(this.x+25, this.y, 5, 5, false);
			//���±ߵľ���
			g.fill3DRect(this.x, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+5, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+10, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+15, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+20, this.y+25, 5, 5, false);
			g.fill3DRect(this.x+25, this.y+25, 5, 5, false);
			//���м��������
			g.fill3DRect(this.x+5, this.y+5, 20, 20, false);
			
			//���м��Բ
			g.fillOval(this.x+10, this.y+10, 10, 10);
			//���м����
			g.drawLine(this.x+15, this.y+15, this.x+40, this.y+15);
			break;
		}
	}
	
	
	
	
	
	
	//���캯����ʼ��
		//��ͬ��̹�� ��ʼλ�ò�ͬ ��ʼ��ɫ��ͬ ��ʼ����ͬ
	public Tank (int x,int y,int tankDirection,int tankColor,int speed) {
		this.x = x;
		this.y = y;
		this.tankColor = tankColor;
		this.tankDirection = tankDirection;
		this.tankSpeed = speed;
	}
	
	
	
	
	
	//get �� set ����
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
	
	//̹���ƶ�
			//̹�������ƶ�
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

//��Ҫ���ӵ����Է���������
class HostileTank extends Tank{
	//�о�̹�˼���̶�ʱ�����ӵ�
	Tank bullet = null;	//��Ӧ��ֻ��һ���ӵ�
	
	//���캯����ʼ��
		//�ط�̹�� ��Ҫ�������� ��Ҫ������ɫ ��Ҫ���뷽��
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed) {
		super(x, y, tankDirection, tankColor,tankSpeed);
		// TODO Auto-generated constructor stub
	}
}
class MyTank extends Tank{
	//�Ҿ�̹�� ��J��������һ���µ�̹�� �ʶ������Ҿ�̹�˷�����о���ͬ  ��Ӧ���и�����
	Bullet bu = null;
	public void creatBullet() {
		//����̹�˷���ͬ��̹���ӵ����ֳ�ʼλ��Ҳ��ͬ
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
	
	//���캯����ʼ��
		//�ҷ�̹�˲���Ҫѡ����ɫ ����Ҫѡ����  ��Ҫ��������
	public MyTank(int x, int y) {
		super(x, y, MyTank.UP, MyTank.RED,2);
		// TODO Auto-generated constructor stub
	}
}


class Bullet implements Runnable{
	//�ٶȵȼ�
	public static final int SLOW = 1;
	public static final int NORMAL = 3;
	public static final int FAST = 5;
	//����
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	//�ҷ�̹���ӵ�ר����ɫ
	public static final int RED = 10;
	//�з�̹���ӵ���ɫ
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;
	//����
	int x;
	int y;
	
	//�ӵ���ɫ
	int bulletColor;
	
	//�ӵ��ٶ�
	int bulletSpeed;
	//�ӵ�����
	int bulletDirection;
	
	//�ӵ��Ƿ����
	boolean isAlive = true;
	
	
	//���ӵ�
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
	

	
	//���캯����ʼ��
	public Bullet(int x,int y,int bulletColor,int bulletSpeed,int bulletDirection) {
		this.x = x;
		this.y = y;
		this.bulletColor = bulletColor;
		this.bulletDirection = bulletDirection;
		this.bulletSpeed = bulletSpeed;
	}
	
	//�ӵ��ƶ�
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (this.isAlive) {
			try {
				// �߳���ľ�̬����
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