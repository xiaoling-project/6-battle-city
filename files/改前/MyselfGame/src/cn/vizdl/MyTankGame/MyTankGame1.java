package cn.vizdl.MyTankGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;

/**
 * ����:̹����Ϸ1.0�� 
 * ������ɰ�
 * 1.����̹��
 * 2.�ҵ�̹�˿������������ƶ�(�����ε�̹�˷���ı�̹����̬)
 * @author Vizdl
 *
 */

//������
public class MyTankGame1 extends JFrame{
	//����ָ���ʼ��
	MyPanel mp = null;
	
	//����������ʼ��
	public MyTankGame1(){
		//ָ���ʼ��
		mp = new  MyPanel();
		
		//������
		this.add(mp);
		
		//��Ӽ���
		this.addKeyListener(mp); //���ô��ڼ���������Ϊ���
		
		//��������
		this.setTitle("TankGame");
		this.setSize(400, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//������
	public static void main (String[] args) {
		new MyTankGame1();
	}
}




//�������
	//���������Ҫ�м������� ��������ֱ��ͨ��������Ϣ����̹�˶����λ�� ���� ����ɫ
class MyPanel extends JPanel implements KeyListener{
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
			g.drawOval(this.x+10, this.y+10, 10, 10);
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
	
	
	
	//���캯����ʼ��
		//�ط�̹�� ��Ҫ�������� ��Ҫ������ɫ ��Ҫ���뷽��
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed) {
		super(x, y, tankDirection, tankColor,tankSpeed);
		// TODO Auto-generated constructor stub
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