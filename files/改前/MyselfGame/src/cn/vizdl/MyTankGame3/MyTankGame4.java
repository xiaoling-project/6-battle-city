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
 * ����:̹����Ϸ3.0�� 
 * ������ɰ�
 * 1.����̹��
 * 2.�ҵ�̹�˿������������ƶ�(�����ε�̹�˷���ı�̹����̬)
 * 3.�ҵ�̹�˿��Է������ӵ�(������һ��ֻ������Ļ��������ӵ�������)
 * 4.�ҵ�̹�˿���"ɱ��"�о�̹��
 * 5.�о�̹�˿��Է����ӵ�,�����ܻ�ɱ�ҷ�̹��(�������еĻ�)
 * 6.�������Ҿ����ǵо���̹�˱����к�ᷢ����ը(���ڵ�һ��̹�˲���ը����,���۵�һ�����ҷ����ǵз�̹��)
 * 7.ʹ�õо��ܹ�����ƶ�
 * 8.����̹����ײ���,������̹�����ص� (�ҷ�̹����ʱδʵ��)
 * 9.����ѡ��ؿ�(*)
 * 	˼· : ��һ�������������ʾ
 * 
 * 
 * BUG:
 * 1.��������Ȼ�ܷ�һ���ӵ����⣨��������Ϊ������ڡ� ̹�����겻���� Ҫ�������һ���ӵ��ſ���������(ͨ���޸�run���)
 * 2.����̹����ײ����ʧһ��,���Һ����ᷢ��ָ�����(��������Ϊ����̹����һ��,�����������Ͳ��ᡣ
 * 		������Ϊstop����(ȡ��stop,�����޴����߾�������̹������ײ,����̹�˻���·)
 * 
 * ����Ҫ��ʱ��� ����������޸Ĺ�����
 * 
 * �޸�:
 * 1.�޸����ӵ�ֻ��һ������
 * @author Vizdl
 *
 */

//������
public class MyTankGame4 extends JFrame{
	//����ָ���ʼ��
	MyPanel mp = null;
	//����������ʼ��
	public MyTankGame4(){
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
		this.setSize(415, 338);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//������
	public static void main (String[] args) {
		new MyTankGame4();
	}
}

//�ҵĿ�ʼ���
class MyStartPanel extends JPanel {
	//��дpaint����
	public void paint (Graphics g){
		g.fillRect(0, 0, 400, 300);
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
	int htNumber = 8;
	
	//���캯����ʼ��
	public MyPanel() {
		//��������ȡ
		mt = new MyTank(100,200);
		
		for (int i = 0; i < htNumber; i++) {
			//����һ���з�̹�˶���
			HostileTank newHt = null;
			if (i < 8) {
				newHt = new HostileTank(i*50,0,HostileTank.DOWM,HostileTank.GREEN,3);
			}else {
				int temp = i % 6;
				newHt = new HostileTank(i*50,temp * 50,HostileTank.LEFT,HostileTank.YELLOW,3);
			}
			newHt.setHts(hts);
			newHt.setMt(mt);
			//�����о��߳�
			Thread t = new Thread(newHt);
			t.start();
			
			//���з�̹�˶������з���
			hts.add(newHt);
		}
	}
	
	//��дpaint����
		//paint�ǻ�ͼ����
	public void paint (Graphics g) {
		// ����
		g.fillRect(0, 0, 1800, 300);

		// �ҵ�̹�˻���
		if (mt.isAlive == true) {
			mt.drawTank(g);
		}
		
		//�ҷ���ը����
		if (!mt.isAlive && mt.bomb.isAlive) {
			//���ñ�ը�ص�
			mt.bomb.setX(mt.getX());
			mt.bomb.setY(mt.getY());
			//��ը��
			mt.bomb.drawBomb(g, this);
			if (mt.bomb.bloodValue >= 16) {
				mt.bomb.isAlive = false;
			}
		}

		// �з�̹�˻���
		for (int i = 0; i < htNumber; i++) {
			HostileTank ht = hts.get(i);
			if (ht.isAlive) {
				ht.drawTank(g);
			}
			
			//̹��ը������
			else if (!ht.isAlive && ht.bomb.isAlive) {
				//���ñ�ը�ص�
				ht.bomb.setX(ht.getX());
				ht.bomb.setY(ht.getY());
				
				ht.bomb.drawBomb(g, this);
				if (ht.bomb.bloodValue < 0){
					ht.bomb.isAlive = false;
				}
			}
			
			//���Ƶо��ӵ�
			for (int k = 0; k < ht.Bullets.size(); k++) {
				Bullet bu = ht.Bullets.get(k);
				if (bu != null && bu.isAlive) {
					bu.drawBullet(g);
				}
				// ��"��"�ӵ�����
				if (bu.isAlive == false) {
					ht.Bullets.remove(bu);	//Ϊʲô�������ᷢ������Ǳ�Խ�����⣿
				}
			}
		}

		// ͨ���������ҵ��ӵ��������
		for (int i = 0; i < mt.Bullets.size(); i++) {
			Bullet bu = mt.Bullets.get(i);
			if (bu != null && bu.isAlive) {
				bu.drawBullet(g);
			}
			// ��"��"�ӵ�����
			if (bu.isAlive == false) {
				mt.Bullets.remove(bu);	//Ϊʲô�������ᷢ������Ǳ�Խ�����⣿
			}
		}
	}
	
	//��� ����ӵ� �����̹�� �Ƿ�ᷢ����ײ ������� �� �ӵ���̹��ͬ���ھ�
	public void hitTank (Tank tank,Bullet bullet) {
		//����������ײ
		if (bullet.x >= tank.x && bullet.x < (tank.x + 30) && bullet.y >= tank.y && bullet.y <= (tank.y + 30)){
			//̹������
			tank.isAlive = false;
			//�ӵ�����
			bullet.isAlive = false;
		}
	}
	
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		// ������Ϣ
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
			
			//�ж����ез�̹��̹�˺��ҷ��ӵ��Ƿ�����Ч��ײ
			for (int i = 0; i < htNumber; i++) {
				//ȡ��̹��
				HostileTank ht = hts.get(i);
				//�ж�̹���Ƿ���Ч
				if (ht.isAlive) {
					for (int j = 0; j < mt.Bullets.size(); j++) {
						//ȡ���ӵ�
						Bullet bu = mt.Bullets.get(j);
						//�ж��ӵ��Ƿ���Ч
						if (bu.isAlive) {
							this.hitTank(ht, bu);
						}
					}
				}
			}
			
			//�ж��ҷ�̹����з��ӵ��Ƿ�ᷢ����ײ
			for (int i = 0; i < htNumber; i++) {
				//ȡ��̹��
				HostileTank ht = hts.get(i);
				//��ʹ�з�̹����Ч,��ʹ�ط�̹���ӵ���Ч,��ô����
				for (int j = 0; j < ht.Bullets.size(); j++) {
					//ȡ���з�̹�˵��ӵ�
					Bullet bu = ht.Bullets.get(j);
					//�ж��ӵ����ҷ�̹���Ƿ���Ч
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
	
	
	//̹�˴������
	boolean isAlive = true;
	
	//ÿ��̹�˶����б�ըЧ����"Bomb"����ֻ�ܴ�һ��
	Bomb bomb = null;
	
	//̹������
	int x;
	int y;
	//̹�˷���
	int tankDirection;
	//̹����ɫ
	int tankColor;
	// ̹���ٶ�
	int tankSpeed;

	// ̹�˵��ӵ�
	Vector<Bullet> Bullets = new Vector<Bullet>();

	// �����ӵ�
	public void creatBullet() {
		// bu ��Ӧ�������Ա,���ܱ����á�
		Bullet bu = null;
		// ����̹�˷���ͬ��̹���ӵ����ֳ�ʼλ��Ҳ��ͬ
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

	// ��̹�� Ӧ���Ǿ�̬��ô�� ��Ӧ�� ��ͬ���󻭲�̹ͬ�� û��̹�˶�������̹�� һ��̹�˶���һ��̹�ˣ�
	// ��Ȼһ����������һ��̹�� ����̹�˶����ڲ�ӵ�����еĲ�������ô��̹�˾ò���Ҫ�������Ĳ�����
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
		
		//��ը
		bomb = new Bomb();
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
class HostileTank extends Tank implements Runnable{
	//��Ұ����
	public static final int VIEW_LENGTH = 10;
	
	//��֪���̹��
		//�����������������ָ�벢û�����ָ��MyPanel��ĵо�̹������
	Vector<HostileTank> hts = new Vector<HostileTank>();
	
	//��֪�ҷ�̹��
	MyTank mt = null;
	
	//���캯����ʼ��
		//�ط�̹�� ��Ҫ�������� ��Ҫ������ɫ ��Ҫ���뷽��
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed) {
		super(x, y, tankDirection, tankColor,tankSpeed);
		// TODO Auto-generated constructor stub
	}
	//��ȡ����̹����Ϣ
	public void setHts(Vector<HostileTank> hts) {
		this.hts = hts;
	}
	//��ȡ�ҷ�̹����Ϣ
	public void setMt (MyTank mt) {
		this.mt = mt;
	}
	
	/**
	 * ���������㷨:
	 * 	ͨ����֪�ҷ�̹��,�����ҷ�̹���ڵз�̹���������Ҽ�����λ��ô�ͷ��ض�Ӧ����ֵ,���򷵻�-1.
	 * @return
	 */
	public int attack () {
		//���·���
		if ( (this.x + 15) >= mt.x && (this.x + 15) <= mt.x + 30 ) {
			//�ҷ�̹�����ϱ�
			if (this.y > mt.y) {
				return Tank.UP;
			}else {
				return Tank.DOWM;
			}
		}else if ( (this.y + 15) >= mt.y && (this.y + 15) <= mt.y + 30 ) {
			//�ҷ�̹�������
			if (this.x > mt.x + 30) {
				return Tank.LEFT;
			}else {
				return Tank.RIGHT;
			}
		}
		return -1;
	}
	
	/**
	 * ̹��Ѱ·�㷨
	 * ����:�����ߵķ�������п�����
	 */
	public int[] moveDirection () {
		int[] arr = {0, 1, 2, 3};
		
		int key = 4;
		//�����ǽ
		if (this.x <= 0){
			if (this.searchKey(arr, key, Tank.LEFT)) {
				key--;
			}
		}//������ǽ
		if (this.y <= 0) {
			if (this.searchKey(arr, key, Tank.UP)) {
				key--;
			}
		}//�ұ���ǽ
		if (this.x >= 370) {
			if (this.searchKey(arr, key, Tank.RIGHT)) {
				key--;
			}
		}//������ǽ
		if (this.y >= 270) {
			if (this.searchKey(arr, key, Tank.DOWM)) {
				key--;
			}
		}
		
		//�ų�ǽ���������
		int[] wall = new int[key];
		for (int i = 0; i < key; i++) {
			wall[i] = arr[i];
		}
		
		//����ҷ�̹��
		if (mt.isAlive) {
			//mt��this�ұ�
			if ( ( mt.x >= (this.x + 30) && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y - HostileTank.VIEW_LENGTH && mt.y <= (this.y + 30) + HostileTank.VIEW_LENGTH) ||
			(  mt.x >= (this.x + 30) && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (mt.y + 30) <= (this.y + 30 + HostileTank.VIEW_LENGTH)) ) {
				if (this.searchKey(arr, key, Tank.RIGHT)) {
					key--;
				}
			}
			//mt��this���
			if ( ( (mt.x + 30) <= this.x && (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && mt.y >= this.y - HostileTank.VIEW_LENGTH && mt.y <= (this.y + 30) + HostileTank.VIEW_LENGTH ) || 
			( (mt.x + 30) <= this.x && (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (mt.y + 30) <= (this.y + 30) + HostileTank.VIEW_LENGTH ) ) {
				if (this.searchKey(arr, key, Tank.LEFT)) {
					key--;
				}
			}
			//mt��this�ϱ�
			if ( (mt.x >= this.x - HostileTank.VIEW_LENGTH && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) <= this.y && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH) ||
			((mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && (mt.y + 30) <= this.y && (mt.y + 30) >= this.y - HostileTank.VIEW_LENGTH)) {
				if (this.searchKey(arr, key, Tank.UP)) {
					key--;
				}
			}
			//mt��this�±�
			if (( mt.x >= this.x - HostileTank.VIEW_LENGTH && mt.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y + 30 && mt.y <= this.y + 30 + HostileTank.VIEW_LENGTH)||
			( (mt.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (mt.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && mt.y >= this.y + 30 && mt.y <= this.y + 30 + HostileTank.VIEW_LENGTH )) {
				if (this.searchKey(arr, key, Tank.DOWM)) {
					key--;
				}
			}
		}
		
		//����̹��
//		���forѭ��������
		for (int i = 0; i < hts.size(); i++) {
			//ȡ��̹��
			HostileTank ht = hts.get(i);
			if (ht != this && ht.isAlive) {
				//ht��this�ұ�
				if ( ( ht.x >= (this.x + 30) && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y - HostileTank.VIEW_LENGTH && ht.y <= (this.y + 30) + HostileTank.VIEW_LENGTH) ||
				(  ht.x >= (this.x + 30) && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (ht.y + 30) <= (this.y + 30 + HostileTank.VIEW_LENGTH)) ) {
					if (this.searchKey(arr, key, Tank.RIGHT)) {
						key--;
					}
				}
				//ht��this���
				if ( ( (ht.x + 30) <= this.x && (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && ht.y >= this.y - HostileTank.VIEW_LENGTH && ht.y <= (this.y + 30) + HostileTank.VIEW_LENGTH ) || 
				( (ht.x + 30) <= this.x && (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH && (ht.y + 30) <= (this.y + 30) + HostileTank.VIEW_LENGTH ) ) {
					if (this.searchKey(arr, key, Tank.LEFT)) {
						key--;
					}
				}
				//ht��this�ϱ�
				if ( (ht.x >= this.x - HostileTank.VIEW_LENGTH && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) <= this.y && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH) ||
				((ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && (ht.y + 30) <= this.y && (ht.y + 30) >= this.y - HostileTank.VIEW_LENGTH)) {
					if (this.searchKey(arr, key, Tank.UP)) {
						key--;
					}
				}
				//ht��this�±�
				if (( ht.x >= this.x - HostileTank.VIEW_LENGTH && ht.x <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y + 30 && ht.y <= this.y + 30 + HostileTank.VIEW_LENGTH)||
				( (ht.x + 30) >= this.x - HostileTank.VIEW_LENGTH && (ht.x + 30) <= (this.x + 30) + HostileTank.VIEW_LENGTH && ht.y >= this.y + 30 && ht.y <= this.y + 30 + HostileTank.VIEW_LENGTH )) {
					if (this.searchKey(arr, key, Tank.DOWM)) {
						key--;
					}
				}
			}
		}
		
		//copy ����
		int[] res = null;
		if (key == 0) {
			//�����·����,���Ĵ���ײ
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
	 * Ѱ·�㷨�ĸ����㷨
	 * ����:�������������Ƿ���key,������,�򽫵�һ���ŵ����һ����Чλ��Ȼ���˳�
	 * @param arr
	 * @param key
	 */
	private boolean searchKey (int[] arr, int vaildSize, int value) {
		for (int i = 0; i < vaildSize; i++) {
			if (value == arr[i]) {
				//��������Чλ�ò����˳�
				arr[i] = arr[vaildSize - 1];
				arr[vaildSize - 1] = value;
				return true;
			}
		}
		return false;
	}

	//�о�̹���ƶ�
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		/**
		 * ��Ϊ̹����ͬһʱ��ֻ����һ������,����
		 * ����ʹ��switch����ѡ��ṹ,��̹������
		 * ���Ƿ����ӵ�
		 */
		
		while (isAlive) {
			int key = (int) (Math.random() * 8);
			switch (key) {
			case 0:
				int attackDirection = attack();
				if (attackDirection != -1 && mt.isAlive) {
					this.tankDirection = attackDirection;
				}
				//�����ӵ�
				this.creatBullet();
				// ���������ӵ��Ϳ�������
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
				//��·
				int[] arr = moveDirection();
				if (arr.length == 1) {
					this.tankDirection = arr[0];
				}else {
					int i;
					int temp = -1;
					for (i = 0; i < arr.length; i++) {
						if (this.tankDirection == arr[i]) {
							//���� 0 - x �����ܳ��ָ���
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
		while (this.isAlive){
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
			if (this.x >= 400 || this.x <= 0 || this.y >= 300 || this.y <= 0) {
				this.isAlive = false;
			}
		}
	}
}

//ը����
class Bomb{
	//����
	int x;
	int y;
	
	//Ѫ��
	int bloodValue = 15;
	
	//ը���Ƿ�"����"
	boolean isAlive = true;
	
	//��ըͼƬ
		//����һ��ͼƬ�������鲢������Ϊ��̬��ԭ��
			//��ըͼƬ���Թ���,ֻҪÿ��ը�����Ѫ����������ͬ���ɣ�
	static Image[] img = new Image[16];
	
	//���캯����ʼ��  
		//����ʼ����ը�ص�  ��ը��Ҫ����ʱ�����ñ�ը�ص�	
	static {
		for	(int i = 0; i < 16; i++) {
			//���ַ�������ͼƬ�ļ����ڵ�һ�λ滭�������������������������⣡
//			img[i] = Toolkit.getDefaultToolkit().getImage("E:\\java\\MyselfGame\\src\\Image\\explode\\e" + (16 - i) + ".gif");
			try {
				img[i] = ImageIO.read(new File("E:\\java\\MyselfGame\\src\\Image\\explode\\e" + (16 - i) + ".gif"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//��ը��
	public void drawBomb (Graphics g,MyPanel mp) {
		//����Ҫѭ����ԭ����g.drawImage(img[bloodValue],this.x,this.y,mp);����ĵ�һ�����������������»��Զ�����repoint();
		if (bloodValue >= 0) {
System.out.println("��ͼ");
			g.drawImage(img[bloodValue],this.x,this.y,mp);
			bloodValue--;
		}
	}
	
	
	//���� ��ը�ص�
	public void setX(int x) {
		this.x = x - 17;
	}
	
	public void setY(int y) {
		this.y = y - 30;
	}
}