import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.border.Border;

public class FloodFillGUI extends JFrame {

    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private JLabel originalLabel;
    private JLabel processedLabel;
    private JButton floodFillStackButton;
    private JButton floodFillQueueButton;
    private ArrayList<BufferedImage> animationFrames;
    private Timer animationTimer;
    private int currentFrame;

    public FloodFillGUI() {
        setTitle("Flood Fill");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(1, 2));

        originalLabel = new JLabel();
        processedLabel = new JLabel();


        Border border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Imagem Original");
        originalLabel.setBorder(border);

        Border border2 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Imagem Processada");
        processedLabel.setBorder(border2);

        imagePanel.add(originalLabel);
        imagePanel.add(processedLabel);


        floodFillStackButton = new JButton("Flood Fill com Pilha");
        floodFillQueueButton = new JButton("Flood Fill com Fila");
        floodFillStackButton.setEnabled(false);
        floodFillQueueButton.setEnabled(false);

        floodFillStackButton.addActionListener(e -> applyFloodFillWithStack());
        floodFillQueueButton.addActionListener(e -> applyFloodFillWithQueue());

        JPanel controlPanel = new JPanel();
        controlPanel.add(floodFillStackButton);
        controlPanel.add(floodFillQueueButton);

        add(imagePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);


        loadImageFromDirectory("imagens\\teste.png");

        animationFrames = new ArrayList<>();
    }

    private void loadImageFromDirectory(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            originalImage = ImageIO.read(imageFile);
            processedImage = ImageIO.read(imageFile);
            originalLabel.setIcon(new ImageIcon(originalImage));
            processedLabel.setIcon(null);
            floodFillStackButton.setEnabled(true);
            floodFillQueueButton.setEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não foi possível carregar a imagem", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFloodFillWithStack() {
        if (processedImage != null) {
            animationFrames.clear();
            floodFillWithStack(processedImage, 10, 10, Color.YELLOW.getRGB());
            startAnimation();
        }
    }

    private void applyFloodFillWithQueue() {
        if (processedImage != null) {
            animationFrames.clear();
            floodFillWithQueue(processedImage, 10, 10, Color.YELLOW.getRGB());
            startAnimation();
        }
    }


    public void floodFillWithStack(BufferedImage image, int x, int y, int newColor) {
        int targetColor = image.getRGB(x, y);
        if (targetColor == newColor) {
            return;
        }

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y});
        int steps = 0;

        while (!stack.isEmpty()) {
            int[] point = stack.pop();
            int px = point[0];
            int py = point[1];

            if (px < 0 || px >= image.getWidth() || py < 0 || py >= image.getHeight()) {
                continue;
            }

            if (image.getRGB(px, py) != targetColor) {
                continue;
            }

            image.setRGB(px, py, newColor);


            stack.push(new int[]{px - 1, py});
            stack.push(new int[]{px + 1, py});
            stack.push(new int[]{px, py - 1});
            stack.push(new int[]{px, py + 1});


            if (steps++ % 50 == 0) {
                saveFrame(image);
            }
        }


        saveFrame(image);
    }


    public void floodFillWithQueue(BufferedImage image, int x, int y, int newColor) {
        int targetColor = image.getRGB(x, y);
        if (targetColor == newColor) {
            return;
        }

        Queue<int[]> queue = new Queue<>();
        queue.enqueue(new int[]{x, y});
        int steps = 0;

        while (!queue.isEmpty()) {
            int[] point = queue.dequeue();
            int px = point[0];
            int py = point[1];

            if (px < 0 || px >= image.getWidth() || py < 0 || py >= image.getHeight()) {
                continue;
            }

            if (image.getRGB(px, py) != targetColor) {
                continue;
            }

            image.setRGB(px, py, newColor);


            queue.enqueue(new int[]{px - 1, py});
            queue.enqueue(new int[]{px + 1, py});
            queue.enqueue(new int[]{px, py - 1});
            queue.enqueue(new int[]{px, py + 1});

            if (steps++ % 50 == 0) {
                saveFrame(image);
            }
        }


        saveFrame(image);
    }


    private void saveFrame(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        animationFrames.add(copy);
    }


    private void startAnimation() {
        currentFrame = 0;
        animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFrame < animationFrames.size()) {
                    processedLabel.setIcon(new ImageIcon(animationFrames.get(currentFrame)));
                    currentFrame++;
                } else {
                    animationTimer.stop();
                }
            }
        });
        animationTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FloodFillGUI gui = new FloodFillGUI();
            gui.setVisible(true);
        });
    }
}
