import java.awt.EventQueue;
import javax.swing.JFrame;

public class Application extends JFrame{

	private static final long serialVersionUID = -2517616123689799182L;

	public Application() {
		initUI();
	}
	
	private void initUI() {
		add(new Board());
		
		setTitle("Snake");
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String args[]) {
		EventQueue.invokeLater( () -> {
			Application app = new Application();
			app.setVisible(true);
		});
	}
}