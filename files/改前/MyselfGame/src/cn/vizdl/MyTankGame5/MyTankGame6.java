package cn.vizdl.MyTankGame5;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

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
 * 6.�������Ҿ����ǵо���̹�˱����к�ᷢ����ը
 * 7.ʹ�õо��ܹ�����ƶ�
 * 8.����̹����ײ���,������̹�����ص�
 * 9.�ӵ���̹�˶���Ѫ��,̹��Ѫ��Խ�����������ӵ��Ĺ�����ҲԽ�ߡ�
 * 10.����ѡ��ؿ�(*)
 * 	˼· : ��һ�������������ʾ
 * 
 * 11.��ԭ������MyPanel�ڶ����ݵ����㵥����װ��һ���̡߳�
 * 
 * δ���BUG:
 * 	��ͣʱ�ɸı䷽��
 * �ѽ��BUG:
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

/**
 * MyTankGame��һ��������
 * @author HP
 *
 */
public class MyTankGame6 extends JFrame implements ActionListener{
/*********************************��Щ�������******************************************/
	//�ҵĻ��� ����ʾ�߳���
	MyPanel mp = null;
	//�ҵĿ�ʼ��� 
	MyStartPanel msp = null;
	//�˵���
	JMenuBar jmb = null;
	//�˵�
	JMenu jm1 = null;
	//�˵��ڰ�ť
		//��ʼ��Ϸ
		JMenuItem jmi = null;
		//�˳���Ϸ
		JMenuItem jmi2 = null;
		//�浵�˳�
		JMenuItem jmi3 = null;
		//������ʼ
		JMenuItem jmi4 = null;
/****************************************************************************************/
	//����������ʼ��
	public MyTankGame6(){
		/**
		 * �����ⲿ����һЩ����Ĵ����Ϳ�ݷ�ʽ������
		 * ��������������ַ���������ַ�������ʾ�������
		 * ��ݷ�ʽ�Ǵ����ַ����á�
		 */
		//�����ҵĿ�ʼ����
		msp = new MyStartPanel();
		//�����˵���
		jmb = new JMenuBar();
		//�����˵�	
		jm1 = new JMenu("��Ϸ(G)");	//������ַ�������ʾ������� ����ͬ��
		//���ÿ�ݷ�ʽ
		jm1.setMnemonic('G');	//��Ctrl + G���ܿ�ݴ�
		//������ť
		jmi = new JMenuItem("��ʼ����Ϸ(H)");
		jmi.setMnemonic('H');
		jmi4 = new JMenuItem("������ʼ(U)");
		jmi4.setMnemonic('U');
		jmi3 = new JMenuItem("�浵�˳�(F)");
		jmi3.setMnemonic('F');
		jmi2 = new JMenuItem("�˳���Ϸ(E)");
		jmi2.setMnemonic('E');
		/**
		 * �����Ǽ�����ע��
		 * ������������ע������Ĺ���,
		 * ������� : ��Ϊĳһʵ����ActionListener�Ķ���ʵ��P
		 * �������˲���֮��,�������м����¼�,���統ǰ��ť����
		 * �������P������actionPerformed������յ���Ϣ(actionPerformed�Ǳ���Ҫ������������������ ��Ȼ�ᱨ��)��
		 */
		jmi.addActionListener(this); 
		jmi2.addActionListener(this);
		jmi3.addActionListener(this);
		jmi4.addActionListener(this);
		/**
		 * ���ǰ�����ļ���ע��
		 * ���ü����ź�,�����ź�Ҳ������������º�
		 * �ᷢ��ʲô�źŸ�actionPerformed����
		 * ����jmi.setActionCommand("playGame");
		 * �ͻᷢ��playGame��actionPerformed
		 */
		jmi.setActionCommand("playGame");
		jmi2.setActionCommand("exitGame");
		jmi3.setActionCommand("recordAndExit");
		jmi4.setActionCommand("playAndRead");
		//���ҵĿ�ʼ������ӵ��ҵ�̹�����������
		this.add(msp);
		//һ������
		jm1.add(jmi);
		jm1.add(jmi4);
		jm1.add(jmi3);
		jm1.add(jmi2);
		jmb.add(jm1);
		//���˵���ӵ���ǰ����
		this.setJMenuBar(jmb);
		
		/**
		 * ������ʼ����߳�Ҳ����һ��ʼ����ʾ���Ǹ��߳�
		 * ��java�� �߳�������һ������,����������Ҫimplements Runnable 
		 * ���������������Ӧ����һ��Ҫ��run()����
		 * ֮��t.start();����ֱ�����߳��������������run();
		 * �Ӷ�ʵ�ֿ����߳�
		 */
		Thread t = new Thread(msp);
		t.start();

		
		//��������
		this.setTitle("TankGame");//���ڱ�������
		this.setSize(600, 500);//���ڴ�С����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Ĭ�ϰ���X�͹رմ���
		this.setVisible(true);//�Ƿ�ɼ�
	}
	
	/**
	 * �������ڴ˽��𵽿�����Ϸ�Ĺ���
	 * @param args
	 */
	public static void main (String[] args) {
		new MyTankGame6();
	}
	
	
	@Override
	/**
	 * ��������ǽ�������źŵĺ���
	 * arg0�����¼��ź�
	 * һ������getActionCommand�����������ź�
	 * Ȼ����������
	 */
	public void actionPerformed(ActionEvent arg0) {
		//һ������getActionCommand�����������ź�
		String command = arg0.getActionCommand();
		//�����ǿ�ʼ��Ϸ ���߶�����ʼ��Ϸ
		if (command.equals("playGame") || command.equals("playAndRead")) {
			//��ɾ��һ��ʼ�� ��ʼ����
			this.remove(msp);
			
			//ָ���ʼ��
			if (command.equals("playAndRead")) {
				//������֮ǰ�ṹͼ��˵�����ִ��η�ʽ
				mp = new  MyPanel(Record.readRecording());
			}else {
				//������֮ǰ�ṹͼ��˵�����ִ��η�ʽ
				mp = new MyPanel();
			}
			
			/**
			 * ��֮�����ʾ��Ϸ����Ļ�����ӵ�����
			 */
			this.add(mp);
			
			/**
			 * ��������ӵ�����
			 * �����ӵļ���ʱ���̼���
			 * Ҳ��Ϊ��֮���ҷ�̹�˿����ɰ�������
			 * 
			 * ���Ǽ��̼������źŴ����� �����mp������
			 * ���̵Ĵ�������MyPanel����
			 */
			this.addKeyListener(mp); //���ô��ڼ���������Ϊ���
			
			//���������̣߳������ػ棩
			Thread t = new Thread (mp);
			t.start();
			
			/**
			 * ���������Ƿ�ɼ�
			 * ����ˢ�´���
			 */
			this.setVisible(true);
		}else if (command.equals("exitGame") || command.equals("recordAndExit")) {
			//����߷ָ���
			Record.updataHighestScore();
			//�����Ǵ浵�˳�
			if (command.equals("recordAndExit")) {
				//����ᴫ���ҵ�̹��ָ��͵о�̹������ָ�� Ȼ�����е����Ա��浽�ļ���
				Record.keepRecording(mp.mt, mp.hts);
			}
			System.exit(0);
		}
	}
}

/**
 * ������ʾ��ʼ������߳���
 * @author HP
 *
 */
class MyStartPanel extends JPanel implements Runnable {
	int times = 0;
	//��дpaint����
	public void paint (Graphics g){
		g.fillRect(0, 0, 400, 300);
		/**
		 * ������������times����ż����ʹ�ÿ�ʼ������ʾ
		 * ����������Ч��
		 * Ҳ����һ�»� һ�²���
		 */
		times++;
		if ((times & 1) != 0) {
			g.setColor(Color.YELLOW);
			Font myFont = new Font("������κ",Font.BOLD, 30);
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
			 * �����repaingt������Ҫ����
			 * �����˵�ǰ���е�paint
			 * ��Ϊpaint,repaint�Ǽ̳���JPanel������
			 * Ȼ����д��paint��
			 */
			this.repaint();
		}
	}
}

/**
 * ������ײ�����߳���
 * @author HP
 *
 */
class Operation implements Runnable{
	MyTank mt = null;
	Vector<HostileTank> hts = null;
	
	/**
	 * ������� mt �� hts ��Ϊ�˵õ�����
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
				// ÿ��һ��ʱ���ˢ��
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// �ж����ез�̹�˺��ҷ��ӵ��Ƿ�����Ч��ײ
			for (HostileTank ht : hts) {
				// �ж�̹���Ƿ����
				if (ht.isAlive) {
					for (int j = 0; j < mt.Bullets.size(); j++) {
						// ȡ���ӵ�
						Bullet bu = mt.Bullets.get(j);
						// �ж��ӵ��Ƿ���Ч
						if (bu.isAlive) {
							//���Ǳ����е�һ������ ����˵���뿴����������Ϸ�
							this.hitTank(ht, bu, false);
						}
					}
				}
			}
	
			// �ж��ҷ�̹����з��ӵ��Ƿ�ᷢ����ײ
			for (HostileTank ht : hts) {
				// ��ʹ�з�̹����Ч,��ʹ�ط�̹���ӵ���Ч,��ô����
				for (int j = 0; j < ht.Bullets.size(); j++) {
					//ȡ���з�̹�˵��ӵ�
					Bullet bu = ht.Bullets.get(j);
					//�ж��ӵ����ҷ�̹���Ƿ���Ч
					if (bu.isAlive && mt.isAlive) {
						this.hitTank(mt, bu, true);
					}
				}
			}
		}
	}
	
	
	/**
	 *  ��� ����ӵ� �����̹�� �Ƿ�ᷢ����ײ ������� �� �ӵ���̹��ͬ���ھ�(ͨ���޸��ӵ���̹�� isAlive�������)��������ɾ������
	 *  ���η�ʽ���
	 *  1) �ҷ�̹��ָ�� �з��ӵ�����ָ�� true
	 *  2) �з�̹��ָ�� �ҷ��ӵ�����ָ�� false
	 * @param tank
	 * @param bullet
	 * @param isMyTank
	 */
	public void hitTank(Tank tank, Bullet bullet, boolean isMyTank) {
		//����������ײ
		if (bullet.x >= tank.x && bullet.x < (tank.x + 30) && bullet.y >= tank.y && bullet.y <= (tank.y + 30)){
			//̹������
			if (tank.isAlive) {
				//
				if (!isMyTank) {
					/**
					 * �����Ϸ��ʵ������������Ϸģʽ,isSpecialMode����Ϊtrue
					 * ���ӵ����д�͸��,�����ӵ���Ѫ����͵з���Ѫ������
					 * ����Ϊ0������
					 * ��һ�־������ǿ����İ汾
					 */
					if (Record.isSpecialMode()) {
						//�ڼ�¼������� ��ӷ�������  
						//����Ĳ������ʽ����Ϊ�˷�ֹ�о�������Ѫ ȴ��Ϊ�ӵ�Ѫ�����ĵ� �����ķ�(����ģʽ��)
						Record.addScore(bullet.bulletBlood < tank.tankBlood ? bullet.bulletBlood : tank.tankBlood);
					}else {
						Record.addScore(1);
					}
				}
				
				if (Record.isSpecialMode()) {
					//̹�˽�Ѫ
					tank.dropOfBlood(bullet.bulletBlood);
					//�ӵ���Ѫ
					bullet.dropOfBlood(tank.tankBlood);
				}else {
					tank.dropOfBlood();
					bullet.dropOfBlood();
				}
				//ʵʱ�޸ļ�¼������
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
 * ������ʾ�ػ���Ϸ������߳���
 * @author HP
 *
 */
//�������
//���������Ҫ�м������� ��������ֱ��ͨ��������Ϣ����̹�˶����λ�� ���� ����ɫ
//�����߳�
class MyPanel extends JPanel implements KeyListener, Runnable{ 
	//����ָ���ʼ��
	MyTank mt = null;
	
	/**
	 * ���Ǵ���������̹�˵�ͼ�� ֻ�Ǻÿ��� ��ʾ��ʣ���ٸ�̹��
	 */
	MyTank myTankImage = null;
	Tank hostileTankImages = null;
	
		//�з�̹�˲�ֹһ�� ���Եö��� �о�̹������ ��һ���� ���� �о�̹�˵�ʹ��������ʵ�ֳ�Ϊһ������
	//�������̹����
	Vector<HostileTank> hts = new Vector<HostileTank>();

	/**
	 * ������ʼ��Ϸ,����һ���ļ���
	 * Ȼ��ͨ������ļ������õ�̹�˵����Իָ�֮ǰ����Ϸ
	 * @param reader
	 */
	public MyPanel(FileReader reader) {
		Scanner scanner = null;
		Record.initData();
		try {
			scanner = new Scanner(reader);
			// ��������ȡ
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			int tankDirection = scanner.nextInt();
			int tankColor = scanner.nextInt();
			int tankSpeed = scanner.nextInt();
			int blood = scanner.nextInt();
			Vector<Bullet> bullet = new Vector<Bullet>();
			int m = scanner.nextInt();
			// ��ȡ�ӵ�
			for (int i = 0; i < m; i++) {
				Bullet bu = new Bullet(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
						scanner.nextInt(), scanner.nextInt());
				bullet.add(bu);
				Thread t = new Thread(bu);
				t.start();
			}
			mt = new MyTank(x, y, tankDirection, tankColor, tankSpeed, blood, bullet);
			// �����ҷ���ʾ̹��
			myTankImage = new MyTank(50, 350, 1);
			
			
			Record.setHostileTankNumber(scanner.nextInt());
			for (int i = 0; i < Record.getHostileTankNumber(); i++) {
				// ��������ȡ
				x = scanner.nextInt();
				y = scanner.nextInt();
				tankDirection = scanner.nextInt();
				tankColor = scanner.nextInt();
				tankSpeed = scanner.nextInt();
				blood = scanner.nextInt();
				bullet = new Vector<Bullet>();
				m = scanner.nextInt();
				// ��ȡ�ӵ�
				for (int i1 = 0; i1 < m; i1++) {
					Bullet bu = new Bullet(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(),
							scanner.nextInt(), scanner.nextInt());
					bullet.add(bu);
					Thread t = new Thread(bu);
					t.start();
				}
				// ����һ���з�̹�˶���
				HostileTank newHt = new HostileTank(x, y, tankDirection, tankColor, tankSpeed, blood, bullet);
				newHt.setHts(hts);
				newHt.setMt(mt);
				// �����о��߳�
				Thread t = new Thread(newHt);
				t.start();
	
				// ���з�̹�˶������з���
				hts.add(newHt);
			}

			
			//�ָ���һ�ѵķ�̹������
			Record.setScore(scanner.nextInt());
			// �����з�̹����ʾ
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
	 * ֱ�ӿ�ʼ��Ϸ
	 * Ĭ�ϴ�record�ڶ�ȡ��Ϣ,����ʼ���������ԡ�
	 */
	public MyPanel() {
		// ��ʼ����¼��Ϣ
		Record.initData();
		// ��������ȡ
		mt = new MyTank(100, 200, Record.getMyTankBlood());
		// �����ҷ���ʾ̹��
		myTankImage = new MyTank(50, 350, 1);
		int[] colorArr = Record.getColorArr();
		for (int i = 0, index = 0; i < Record.getHostileTankNumber(); i++) {
			while (colorArr[index] == 0) {
				index++;
			}
			int nowTankColor = index + Tank.YELLOW;
			colorArr[index]--;
			// ����һ���з�̹�˶���
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
			// �����о��߳�
			Thread t = new Thread(newHt);
			t.start();

			// ���з�̹�˶������з���
			hts.add(newHt);
		}
		// �����з�̹����ʾ
		hostileTankImages = new Tank(180, 350, Tank.UP, Tank.BLACK, 0, 1);
		
		this.startOperation();
	}
	
	/**
	 * ���������̨�߳�
	 */
	public void startOperation() {
		Operation opt = new Operation(mt,hts);
		Thread t = new Thread(opt);
		t.start();
	}
	
	// ��дpaint����
	// paint�ǻ�ͼ����
	public void paint(Graphics g) {
		// ����
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

		// ��ʾ��Ϣ����
		this.drowInfo(g);

		// �ҵ�̹�˻���
		if (mt.isAlive == true) {
			mt.drawTank(g);
		}

		// �ҷ���ը����
		if (!mt.isAlive && mt.bomb.isAlive) {
			// ���ñ�ը�ص�
			mt.bomb.setX(mt.getX());
			mt.bomb.setY(mt.getY());
			// ��ը��
			mt.bomb.drawBomb(g, this);
			if (mt.bomb.bloodValue >= 16) {
				mt.bomb.isAlive = false;
			}
		}

		// �з�̹�˻���
		for (HostileTank ht : hts) {
			if (ht.isAlive) {
				ht.drawTank(g);
			}

			// ̹��ը������
			else if (!ht.isAlive && ht.bomb.isAlive) {
				// ���ñ�ը�ص�
				ht.bomb.setX(ht.getX());
				ht.bomb.setY(ht.getY());

				ht.bomb.drawBomb(g, this);
				if (ht.bomb.bloodValue < 0) {
					ht.bomb.isAlive = false;
				}
			}

			// ���Ƶо��ӵ�
			for (int k = 0; k < ht.Bullets.size(); k++) {
				Bullet bu = ht.Bullets.get(k);
				if (bu != null && bu.isAlive) {
					bu.drawBullet(g);
				}
				// ��"��"�ӵ�����
				if (bu.isAlive == false) {
					ht.Bullets.remove(bu); // Ϊʲô�������ᷢ������Ǳ�Խ�����⣿
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
	
	//������ʾ��Ϣ
	public void drowInfo(Graphics g) {
		// �ҷ���ʾ̹�˻���
		myTankImage.drawTank(g);
		g.setColor(Color.RED);
		g.drawString("�ҷ�Ѫ�� : " + Record.getMyTankBlood(), 30, 400);
		hostileTankImages.drawTank(g);
		g.setColor(Color.BLACK);
		g.drawString("�з�̹������ : " + Record.getHostileTankNumber(), 150, 400);
		// ��߷ֻ��»���
		Badge.drawKingBadge(300, 340, g, this);
		g.drawString("��ʷ��ߵ÷� : " + Record.getHighestScore(), 285, 400);
		// ��ǰ�ֻ��»���
		Badge.drawBadge(450, 340, g, this);
		g.drawString("��ǰ�÷� : " + Record.getScore(), 447, 400);
	}


	
	/**
	 * ����ҵ�̹�˸����Ƿ��ео�̹��
	 * ����û�з���true
	 * ������ ����false
	 * @param Direction
	 * @return
	 */
	public boolean hasntHostileTank (int Direction) {
		//�������
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
		
		//����Ƿ��ео�̹����x1,x2,y1,y2��ɵķ�����
		for (HostileTank ht : hts) {
			if (ht.isAlive && !(ht.x > x2 || ht.x + 30 < x1) && !(ht.y > y2 || ht.y + 30 < y1)) {
				return false;
			}
		}return true;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		// ������Ϣ
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
				//ÿ��һ��ʱ���ˢ��
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
		}
	}

}


/**
 * ��������̹����ĸ���
 * @author HP
 *
 */
//����̹�˵Ĺ��Է���Tank����
class Tank{
	/**
	 * ��������c�����ƺ�Ķ���
	 */
	//����
		//����
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
		//��ɫ
			//�ҷ�̹��ר����ɫ
	public static final int RED = 1000;
	public static final int PINK = 1001;
			//�з�̹����ɫ
	public static final int BLACK = 15;
	public static final int YELLOW = 11;
	public static final int BLUE = 12;
	public static final int GREEN = 13;
	public static final int WHITE = 14;
	//̹�˴������
	boolean isAlive = true;
	
	//Ѫ��
	int tankBlood;
	
	/**
	 * ֮�����ͣ���ܾ��ǽ�����������������
	 */
	boolean isStop = false;

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
	Vector<Bullet> Bullets = null;


	//���캯����ʼ��
		//��ͬ��̹�� ��ʼλ�ò�ͬ ��ʼ��ɫ��ͬ ��ʼ����ͬ
	/**
	 * ��Ϊ��Ҫ�ָ����� ���ָ�����ʱ��һ������ӵ�,�������������ֹ��췽��
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
		//��ը
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
	 * ������һ��ģʽ�µ��õķ���
	 * �ı�̹����ɫ
	 */
	public void dropOfBlood () {
		dropOfBlood(1);
	}
	
	/**
	 * ����������ģʽ�µ��õķ�����
	 * ���ݲ�ͬ��ɫ�ӵ��Ĺ��� ��ʧ��ͬѪ��
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
	/*****************��������̹���ƶ�����ͣ���Ե�*************************/
	/**
	 * ֮�����ͣ���ܾ��ǽ�����������������
	 */
	public void stop() {
		isStop = true;
		//���ӵ���ͣ
		for (Bullet b : Bullets) 
			b.stop();
	}
	
	public void move() {
		isStop = false;
		for (Bullet b : Bullets) 
			b.move();
	}
	/********************************************************/
	// �����ӵ�
	public void creatBullet() {
		//����̹��״̬������ͣ��
		if (!isStop) {
			Bullet bu = null;
			// ����̹�˷���ͬ��̹���ӵ����ֳ�ʼλ��Ҳ��ͬ
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
			//���ӵ���ӵ��ӵ�������
			Bullets.add(bu);
			//�����ӵ����߳�
			Thread t = new Thread(bu);
			t.start();
		}
	}

	// ��̹�� Ӧ���Ǿ�̬��ô�� ��Ӧ�� ��ͬ���󻭲�̹ͬ�� û��̹�˶�������̹�� һ��̹�˶���һ��̹�ˣ�
	// ��Ȼһ����������һ��̹�� ����̹�˶����ڲ�ӵ�����еĲ�������ô��̹�˾ò���Ҫ�������Ĳ�����
	/**
	 * ��Ϊ��Ϸ��̹�����ɼ������κ�Բƴ�ճɵ�
	 * ����������Graphics�����滭���κ�Բ
	 * ��������̹�������������������
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
	
	/************************�����Ƕ�������,�ı�ͻ�ȡ̹�����Եķ���**************************/
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
	
	//̹���ƶ�
			//̹�������ƶ�
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
 * AI���Զ��ƶ�����:
 * ������AI����mt��hts�����������жϸ����Ƿ��������̹��
 * �������� ������ͬ�ķ�����
 * ������·���� ��ѡ��ֹͣ����
 * �������ܱ���̹���ص���һ��
 * @author HP
 *
 */
//��Ҫ���ӵ����Է���������
class HostileTank extends Tank implements Runnable{
	//��Ұ���� ��Ұ������AI�����Զ��ƶ���
	public static final int VIEW_LENGTH = 15;
	
	
	//��֪���̹��
		//�����������������ָ�벢û�����ָ��MyPanel��ĵо�̹������
	Vector<HostileTank> hts = new Vector<HostileTank>();
	
	//��֪�ҷ�̹��
	MyTank mt = null;
	
	//���캯����ʼ��
		//�ط�̹�� ��Ҫ�������� ��Ҫ������ɫ ��Ҫ���뷽��
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood, Vector<Bullet> bullet) {
		super(x, y, tankDirection, tankColor,tankSpeed, blood, bullet);
		// TODO Auto-generated constructor stub
	}
	public HostileTank(int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood) {
		super(x, y, tankDirection, tankColor,tankSpeed, blood);
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
	 * ��θ�֪̹��:
	 * �жϵ�ǰ̹�˵��������ҵĵ��Ŀ�������Ƿ�����һ��̹��
	 */
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
	 * ����û�з���null
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
			/**
			 * ��θ�֪̹��:
			 * �жϵ�ǰ̹�˵��������ҵĵ��Ŀ�������Ƿ�����һ��̹��
			 */
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
		 * ��Ϊ̹����ͬһʱ��ֻ����һ������,���� ����ʹ��switch����ѡ��ṹ,��̹������ ���Ƿ����ӵ�
		 */
		//�����о�̹������,���߳�ֹͣ��
		while (isAlive) {
			//������ǰ̹�˴�����ͣ״̬,�߳����ߺ����
			if (this.isStop) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			//��ʱ�����ҷ� ̹��֮ǰ�ķ���,Ӧ����·���ߺ���ͣ����״��
			int lastDirection = this.tankDirection;
			/**
			 * ��ΪҪ���� �����ӵ� �� ��·���ֶ���,����������ѡ��
			 * �����ӵ��Ŀ���������һ���İ˷�֮һ
			 */
			int key = (int) (Math.random() * 8);
			switch (key) {
			case 0:
				int attackDirection = attack();
				if (attackDirection != -1 && mt.isAlive) {
					this.tankDirection = attackDirection;
				}
				// �����ӵ�
				 this.creatBullet();
				break;
			//��·
			default:
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/**
				 * ��������moveDirection,��ȡ�ÿ����ߵķ���
				 * ����û���򷵻�null
				 */
				int[] arr = moveDirection();
				//������·����,�Ǿ�ֱ������switch��Ҳ���൱����ͣ��
				if (arr == null) {
					break;
				}
				else if (arr.length == 1) {
					this.setTankDirection(arr[0]);
				} 
				//�����ж���·,����Ҫ����������˸�������
				else {
					int i;
					key = -1; //�����key��Ȼ������switch
					for (i = 0; i < arr.length; i++) {
						if (this.tankDirection == arr[i]) {
							// ���� 0 - x �����ܳ��ָ���
							key = (int) (Math.random() * arr.length) - (int) (Math.random() * 50);
							break;
						}
					}
					if (i == arr.length) {
						key = (int) (Math.random() * arr.length);
					}
					//�����˵�ǰ̹����Ҫ�ƶ��ķ���
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
				//�������޸���̹��̹�˵�����
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
	 * �����ҷ�̹�˵�Ѫ��
	 * @param live
	 */
	public void setLive (int live) {
		this.tankBlood = live;
	}
	
	/**
	 * �ı�̹����ɫ,�����������ͬ�����η���,��Ҳ��һ��ģʽ�µĵ�Ѫ������
	 */
	public void dropOfBlood() {
		dropOfBlood(1);
	}

	/**
	 * ��дchangeColor����
	 */
	public void dropOfBlood(int blood) {
		//ʹ��̹����ɫ�ں�ɫ�ͷ�ɫ֮��任 ��һ�����ѵ�Ѫ�˵�Ч��
		if (this.tankColor != Tank.RED) 
			this.tankColor = Tank.RED;
		else
			this.tankColor = Tank.PINK;
		this.tankBlood -= blood;
		//����Ѫ��С�ڵ���0 ��̹������
		if (this.tankBlood <= 0) {
			this.isAlive = false;
			this.tankBlood = 0;
		}
	}
	
	// ���캯����ʼ��
	// �ҷ�̹�˲���Ҫѡ����ɫ ����Ҫѡ���� ��Ҫ��������
	public MyTank(int x, int y, int blood) {
		super(x, y, MyTank.UP, MyTank.RED, 2, blood);
	}
	//���ڻָ�����(����������Ϸ)ʱ����������������ҷ��ӵ������ݣ�����һ�������ڶ��˷��� ��ɫ �ٶ� �ӵ�������趨
	public MyTank (int x,int y,int tankDirection,int tankColor,int speed, int blood, Vector<Bullet> Bullets) {
		super(x, y, tankDirection, tankColor, speed, blood, Bullets);
	}
}

/**
 * �����ӵ���,��¼���ӵ������ݲ�����ÿ���ӵ�Ϊ�߳�ʵ������
 * @author HP
 *
 */
class Bullet implements Runnable {
	//�ٶȵȼ�
	public static final int SLOW = 1;
	public static final int NORMAL = 3;
	public static final int FAST = 5;
	//����
	public static final int UP = 0;
	public static final int DOWM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	public int bulletBlood;
	
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
	
	//�ӵ��Ƿ���ͣ
	boolean isStop = false;
	
	/**
	 * ���������ӵ���ɫ�ĺ���,һ�㴫�����ȶ���õ���ɫ��̬����
	 * @param color
	 */
	public void setColor(int color) {
		this.bulletColor = color;
	}
	
	/**
	 * һ��ģʽ : ������������ģʽ����,�ӵ�Ѫ���趨Ϊ1�������ӵ��������ֱ������
	 */
	public void dropOfBlood() {
		this.isAlive = false;
	}
	/**
	 * ����ģʽ : �ӵ��Ǿ���"Ѫ��"��,�����ӵ�Ѫ����4,��̹��Ѫ����2,��ֱ�Ӱ�̹�˴���
	 * Ȼ���ӵ�Ѫ������ʣ��2,�ӵ���Ȼ����,�������趨��
	 * @param tankBlood
	 */
	public void dropOfBlood(int tankBlood) {
		this.bulletBlood -= tankBlood;
		if (this.bulletBlood <= 0) {
			this.isAlive = false;
		}
	}
	
	/**
	 * ���ǻ��ӵ���,������ʾ(��ͼ)�߳��������ڻ�������ʾͼ��
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
	/*************************��̹������ͬ,���������ṩ��ͣ���ܵ�����isStop���޸�****************************************/
	/**
	 * ͨ���޸�isStop��Ϊһ���ź�,ʹ���ӵ��޷��ƶ�
	 * ����
	 */
	public void stop () {
		isStop = true;
	}
	
	public void move () {
		isStop = false;
	}
	
	//���캯����ʼ��
	public Bullet(int x,int y,int bulletColor,int bulletSpeed,int bulletDirection, int bulletBlood) {
		this.x = x;
		this.y = y;
		this.bulletColor = bulletColor;
		this.bulletDirection = bulletDirection;
		this.bulletSpeed = bulletSpeed;
		this.bulletBlood = bulletBlood;
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
			//�����ӵ��ķ��� �����ݽ��в�ͬ���޸�
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
			//�������˱߿�,������ӵ�����
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
			try {
				img[i] = ImageIO.read(Bomb.class.getClassLoader().getResourceAsStream("Image\\explode\\e" + (16 - i) + ".gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//��ը��
	public void drawBomb (Graphics g, MyPanel mp) {
		//����Ҫѭ����ԭ����g.drawImage(img[bloodValue],this.x,this.y,mp);����ĵ�һ�����������������»��Զ�����repoint();
		if (bloodValue >= 0) {
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

//��¼��
class Record{
	//�ҷ�̹��Ѫ��
	private static int myTankBlood = 5;
	//������ɫ̹�˵ĳ�ʼֵ
	//�� �� �� �� ��
	private static int[] colorArr = new int[5];
	private static boolean isSpecialMode = false;
	private static int hostileTankNumber = 0;
	private static int highestScore = 0;
	private static int score = 0;
	//��¼��γ�ʼ�������ļ���srcΪ��Ŀ¼���ļ�·����
	private static String recordPath = "Record\\record.txt";
	//��¼��߷��ļ������ļ���srcΪ��Ŀ¼���ļ�·����
	private static String highestScoreRecordPath = "Record\\highestScoreRecord.txt";
	//��¼֮ǰ�浵�����ݵ����ļ���srcΪ��Ŀ¼���ļ�·����
	private static String recorderPath = "Record\\recorder.txt";
	private static boolean isStop = false;
	
	
	/**
	 * ƾ���ļ�"Record\\record.txt"
	 * �����ó�ʼ���ĸ��� 
	 * ����ļ���ֻ������ 
	 * ��һ�� : �ҷ�̹�˵ĳ�ʼѪ��
	 * �ڶ��� : ����ɫΪ����,������ɫ��̹�˷ֱ�ռ�ݶ��ٸ�(һ����������ɫ�� �� �� �� ��)
	 * ������ : ģʽ�ĸı�,����0�������һ��ֻ��һ��Ѫ,�ڶ���ģʽ��һ�δ��ӵ�Ѫ����ô��Ѫ
	 */
	public static void initData () {
		InputStream file = null;
		//�ȶ���о�����ɫ̹�������Լ��ҷ�̹�˵�Ѫ��
		InputStream highestScoreFile = null;
		Scanner scanner = null, highestScoreScanner = null;
		try {
			//���ļ���srcΪ��Ŀ¼,�����ļ�·����
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
	 * ���ڴ����˳�
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
	 * ��������̹�˵�����,���ַ�����ʽ����
	 * ��һ�зֱ�Ϊ : int x, int y, int tankDirection, int tankColor,int tankSpeed, int blood
	 * �������� Bullets�ĳ���.
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
	 * ���������ӵ�����Ϣ : int x,int y,int bulletColor,int bulletSpeed,int bulletDirection, int bulletBlood
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
	 * ��ȡ��һ�δ���
	 */
	public static FileReader readRecording() {
		//�ָ��ϴη�̹������
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
		// �ȿ���һ����ɫ����
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
	//������߷�
	public static void updataHighestScore() {
		FileWriter fw = null;
		BufferedWriter bw = null;
		File file = new File(highestScoreRecordPath);
		//��ȡ����ǰ��
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
	/***************************************������һϵ���޸����ݵķ���**********************************************/
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
 * Badgeֻ�ṩ����ͼƬ�Ļ���,������ͼƬ������Ϸ��������ʹ�
 * @author HP
 *
 */
class Badge{
	//��߷���ʾ�Ļʹڵ����ļ���srcΪ��Ŀ¼���ļ�·����
	private static String kingFilePath = "111.jpg";
	//��ǰ�ĵ÷���ʾ�Ļʹڵ����ļ���srcΪ��Ŀ¼���ļ�·����
	private static String filePath = "222.jpg";
	/**
	 * @param x 
	 * @param y : ����������������
	 * @param g : ����
	 * @param mp : �����ĸ�������
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