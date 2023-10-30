/*
 *
 * Proyecto Semana Tec
 * Autor: Jesus Ramirez Delgado
 * Matricula: A01274723
 * Fecha: 26 Octubre 2023
 *
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton; // Libreria para agregar boton


public class Board extends JPanel implements ActionListener {
	private static final int B_WIDTH = 300;
	private static final int B_HEIGHT = 300;
	private static final int DOT_SIZE = 10;
	private static final int ALL_DOTS = 900;
	private static final int RAND_POS = 29;
	private static final int DELAY = 150;
	
	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	
	private int dots;
	private int appleX;
	private int appleY;
	private int badAppleX;
	private int badAppleY;
	private Timer timer;
	private JButton playAgainButton;
	
	private Image dot, apple, head, badApple;
	
	public Board() {
		initUI();
	}
	
	private void initUI() {
		addKeyListener(new TAdapter());
		
		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		
		// Boton 
		playAgainButton = new JButton("Play Again");
	    playAgainButton.setBounds(B_WIDTH / 2 - 75, B_HEIGHT / 2 + 30, 150, 30);  // posiciona el botón en el centro
	    playAgainButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            restartGame();
	        }
	    });
	    playAgainButton.setVisible(false);  // inicialmente el botón está oculto
	    this.add(playAgainButton);
		
		loadImages();
		initGame();
	}
	
	private void loadImages() {
		ImageIcon ii;
		
		ii = new ImageIcon("images/dot.png");
		dot = ii.getImage();
		
		ii = new ImageIcon("images/apple.png");
		apple = ii.getImage();
		
		ii = new ImageIcon("images/head.png");
		head = ii.getImage();
		
		ii = new ImageIcon("images/badApple.png");
		badApple = ii.getImage();
	}
	
	private void initGame() {
		dots = 3;
		
		for (int i = 0; i < dots; i++) {
			x[i] = 50 - (i * DOT_SIZE);
			y[i] = 50 ;
		}
		
		locateApple();
		locateBadApple();
		
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	private void locateApple() {
		int aux;
		
		aux = (int) (Math.random() * RAND_POS);
		appleX = aux * DOT_SIZE;
		
		aux = (int) (Math.random() * RAND_POS);
		appleY = aux * DOT_SIZE;
	}
	
	// Metodo para generar una manzana veneosa
	private void locateBadApple() {
		int aux;
		aux = (int)(Math.random() * RAND_POS);
		badAppleX = aux * DOT_SIZE;
		
		aux = (int) (Math.random() * RAND_POS);
		badAppleY = aux * DOT_SIZE;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawning(g);
	}
	
	private void doDrawning(Graphics g) {
	    if(inGame) {
	        g.drawImage(apple, appleX, appleY, null);
	        g.drawImage(badApple, badAppleX, badAppleY, null);  // Agregado manzana venenoza

	        for(int i = 0; i < dots; i++) {
	            if (i == 0) {
	                g.drawImage(head, x[i], y[i], null);
	            } else {
	                g.drawImage(dot, x[i], y[i], null);
	            }
	        }
	        Toolkit.getDefaultToolkit().sync();
	    } else {
	        drawGameOver(g);
	    }
	}
	
	// Metodo para reiniciar 
	private void restartGame() {
	    dots = 3;
	    for (int i = 0; i < dots; i++) {
	        x[i] = 50 - (i * DOT_SIZE);
	        y[i] = 50;
	    }
	    leftDirection = false;
	    rightDirection = true;
	    upDirection = false;
	    downDirection = false;
	    inGame = true;

	    locateApple();
	    locateBadApple();
	    timer.start();
	    playAgainButton.setVisible(false);
	}

	private void drawGameOver(Graphics g) {
	    String msg = "Game Over";
	    Font small = new Font("Helvetica", Font.BOLD, 14);
	    FontMetrics fm = getFontMetrics(small);

	    g.setColor(Color.white);
	    g.setFont(small);
	    g.drawString(msg, (B_WIDTH - fm.stringWidth(msg)) / 2, B_HEIGHT / 2);

	    playAgainButton.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(inGame) {
			checkApple();
			checkBadApple();
			checkCollision();
			move();
		}
		
		repaint();
	}
	
	private void checkApple() {
		if (x[0] == appleX && y[0] == appleY) {
			dots ++;
			locateApple();
		}
	}
	
	// Logica de manzana venenosa
	private void checkBadApple() {
		if (x[0] == badAppleX && y[0] == badAppleY) {
			dots --;
			locateBadApple();
		}
	}
	
	private void checkCollision() {
		for (int i = dots; i > 0; i--) {
			if ((dots > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
				inGame = false;
			}
		}
		if (x[0] >= B_WIDTH) {
			inGame = false;
		}
		if (x[0] < 0) {
			inGame = false;
		}
		if(y[0] >= B_HEIGHT) {
			inGame = false;
		}
		if(y[0] < 0) {
			inGame = false;
		}
		// Si no tiene dots es game Over
		if (dots <= 0) { 
			inGame = false;
		}
		if(!inGame) {
			timer.stop();
		}
	}
	
	private void move() {
		for (int i = dots; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		
		if (leftDirection) {
			x[0] -= DOT_SIZE;
		}
		if (rightDirection) {
			x[0] += DOT_SIZE;
		}
		if (upDirection) {
			y[0] -= DOT_SIZE;
		}
		if(downDirection) {
			y[0] += DOT_SIZE;
		}
	}
	
	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if ((key == KeyEvent.VK_LEFT) && !rightDirection) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}
			
			if ((key == KeyEvent.VK_RIGHT) && !leftDirection) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
				
			}
			if ((key == KeyEvent.VK_UP) && !downDirection) {
				upDirection = true;
				leftDirection = false;
				rightDirection = false;
			}
			if((key == KeyEvent.VK_DOWN) && !upDirection) {
				downDirection = true;
				leftDirection = false;
				rightDirection = false;
				
			}
		}
	}
}
