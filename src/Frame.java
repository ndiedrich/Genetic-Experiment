import java.awt.GridLayout;
import javax.swing.JFrame;
import java.util.concurrent.TimeUnit;
import java.util.Timer;

public class Frame extends JFrame {


	private static final long serialVersionUID = 1L;

	Screen s;

	public Frame(char[][] input) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 820);
		setResizable(true);
		setTitle("Graphics");

		init(input);
	}

	public void init(char[][] input) {
		setLocationRelativeTo(null);

		setLayout(new GridLayout(1,1,0,0));

		s = new Screen(input);
		super.add(s);

		setVisible(true);
	}
}