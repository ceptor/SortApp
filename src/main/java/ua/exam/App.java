package ua.exam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Single page application consists of 2 main screens, Intro & Sort.
 *
 * 1.Clicking “enter” button takes to next screen, Sort.
 * Numbers buttons:
 * 1.	Show X random numbers (depends on data entered by user in the previous screen)
 * 2.	The max number value is 1000
 * 3.	At least one value should be equal or less than 30
 * 4.	Present maximum 10 numbers in a column. If there are more numbers, add another column
 * Sort button:
 * 5.	Clicking the sort button will sort the presented numbers in a descending order
 * 6.	Clicking the sort button again, will change it to increasing order
 * 7.	The screen should be updated after each iteration of quick-sort (i.e. re-implement quick-sort; copy/paste of existing implementation is permitted).
 * Reset button:
 * Takes back to intro screen.
 * Clicking one of the numbers button:
 * 8.	If the clicked value is equal or less than 30, present X new random numbers on the screen
 * 9.	If the clicked value is more than 30, pop up a message “Please select a value smaller or equal to 30.”
 *
 * @see JFrame
 * @see JPanel
 * @see JScrollPane
 */
public class App {
    private static final JFrame mainFrame = new JFrame("App");
    private static final JPanel mainPanel = new JPanel();

    private static final int maxX = 800;
    private static final int maxY = 600;
    private static final int generalWidth = 120;
    private static final int generalHeight = 25;

    private static int[] numbers;
    private static int count;
    private static boolean isDescent = true;
    private static final int threshold = 30;

    /**
     * Main method creates app and run it.
     *
     * @param args
     */
    public static void main(String[] args) {
        App app = new App();
        app.createAndShowUI();
    }

    /**
     * Prepare main frame and fill necessary components.
     *
     * @see JFrame
     * @see JScrollPane
     */
    private void createAndShowUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainFrame.add(scroll);
        mainFrame.setSize(maxX, maxY);
        mainFrame.setLocationRelativeTo(null);

        placeComponentsOnLoginPanel();

        mainFrame.setVisible(true);
    }

    private void placeComponentsOnLoginPanel() {
        mainPanel.setLayout(null);
        mainPanel.removeAll();

        JLabel numberLabel = new JLabel("How many numbers to display?");
        JTextField numberField = new JTextField("", 10);
        numberField.setText("10");

        numberLabel.setBounds((maxX-generalWidth)/2, (maxY-generalHeight)/2-80, generalWidth+100, generalHeight);
        numberField.setBounds((maxX-generalWidth)/2, (maxY-generalHeight)/2-40, generalWidth, generalHeight);

        addEnterButton(numberField);

        mainPanel.add(numberLabel);
        mainPanel.add(numberField);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void addEnterButton(final JTextField numberField) {
        JButton enterButton = new JButton("Enter");
        enterButton.setBounds((maxX-generalWidth)/2, (maxY-generalHeight)/2, generalWidth, generalHeight);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    count = Integer.parseInt(numberField.getText().trim());
                    int width = count*6+30;
                    mainPanel.setPreferredSize(new Dimension(width,maxY-100));
                    numbers = new int[count];
                    placeComponentsOnMainPanel(mainPanel, false);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Value must be number!");
                }
            }
        });
        mainPanel.add(enterButton);
    }

    private void placeComponentsOnMainPanel(final JPanel mainPanel, boolean sorted) {
        mainPanel.setLayout(null);
        mainPanel.removeAll();

        Random random = new Random();

        if(!sorted) {
            for (int i = 0; i < count; i++) {
                numbers[i] = random.nextInt(1000);
            }
            if(numbers != null && numbers.length>0) numbers[0] = random.nextInt(threshold);
        } else {
            quickSort();
            if(isDescent){
                reverse();
                isDescent = false;
            } else {
                isDescent = true;
            }
        }

        for (int i = 0; i < count; i++) {
            assert numbers != null;
            JButton randomNumber = new JButton(String.valueOf(numbers[i]));
            randomNumber.setBounds(60*(i/10), 27*(1+i%10), generalWidth-60, generalHeight);
            addButtonListener(mainPanel, randomNumber);
        }

        addResetButton(mainPanel);
        addSortButton(mainPanel);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void reverse() {
        for(int i = 0; i < numbers.length / 2; i++) {
            int temp = numbers[i];
            numbers[i] = numbers[numbers.length - i - 1];
            numbers[numbers.length - i - 1] = temp;
        }
    }

    private void addSortButton(final JPanel mainPanel) {
        JButton sortButton = new JButton("Sort");
        sortButton.setBounds((maxX-generalWidth)-250, (maxY-generalHeight)-100, generalWidth, generalHeight);
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeComponentsOnMainPanel(mainPanel,true);
            }
        });
        mainPanel.add(sortButton);
    }

    private void addResetButton(final JPanel mainPanel) {
        JButton resetButton = new JButton("Reset");
        resetButton.setBounds((maxX-generalWidth)-100, (maxY-generalHeight)-100, generalWidth, generalHeight);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDescent = true;
                mainPanel.setPreferredSize(new Dimension(maxX-100,maxY-100));
                placeComponentsOnLoginPanel();
            }
        });
        mainPanel.add(resetButton);
    }

    private void addButtonListener(final JPanel mainPanel, JButton randomNumberButton) {
        randomNumberButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                int num = Integer.parseInt(button.getText());
                if (num <= threshold){
                    isDescent = true;
                    placeComponentsOnMainPanel(mainPanel,false);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a value smaller or equal to 30.");
                }
            }
        });
        mainPanel.add(randomNumberButton);
    }


    public static void quickSort() {
        int startIndex = 0;
        int endIndex = count - 1;
        doSort(startIndex, endIndex);
    }

    private static void doSort(int start, int end) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (numbers[i] <= numbers[cur])) {
                i++;
            }
            while (j > cur && (numbers[cur] <= numbers[j])) {
                j--;
            }
            if (i < j) {
                int temp = numbers[i];
                numbers[i] = numbers[j];
                numbers[j] = temp;
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        doSort(start, cur);
        doSort(cur+1, end);
    }
}