package ua.exam;

import javax.swing.*;
import java.awt.*;
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
    private static final String appName = "App";
    private static final String initialQuestion = "How many numbers to display?";
    private static final String errorMessage = "Value must be number!";
    private static final String suggestionMessage = "Please select a value smaller or equal to 30.";
    private static final String enterButtonText = "Enter";
    private static final String sortButtonText = "Sort";
    private static final String resetButtonText = "Reset";

    private static final JFrame mainFrame = new JFrame(appName);
    private static final JPanel mainPanel = new JPanel();

    private static final int maxX = 800;
    private static final int maxY = 600;
    private static final int generalWidth = 120;
    private static final int generalHeight = 25;

    private static int[] numbers;
    private static int count;
    private static boolean isDescent = true;
    private static final int threshold = 30;
    private static final int maxNumberValue = 1001;

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
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        mainFrame.add(scroll);
        mainFrame.setSize(maxX, maxY);
        mainFrame.setLocationRelativeTo(null);

        placeComponentsOnLoginPanel();

        mainFrame.setVisible(true);
    }

    /**
     * Full fill 1st screen
     *
     * @see JPanel
     */
    private void placeComponentsOnLoginPanel() {
        mainPanel.setLayout(null);
        mainPanel.removeAll();

        JLabel numberLabel = new JLabel(initialQuestion);
        JTextField numberField = new JTextField("", 10);

        numberLabel.setBounds((maxX-generalWidth)/2-25, (maxY-generalHeight)/2-80, generalWidth+100, generalHeight);
        numberField.setBounds((maxX-generalWidth)/2, (maxY-generalHeight)/2-40, generalWidth, generalHeight);

        addEnterButton(numberField);

        mainPanel.add(numberLabel);
        mainPanel.add(numberField);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Add "Enter" button
     *
     * @param numberField
     */
    private void addEnterButton(final JTextField numberField) {
        JButton enterButton = new JButton(enterButtonText);
        enterButton.setBounds((maxX-generalWidth)/2, (maxY-generalHeight)/2, generalWidth, generalHeight);
        enterButton.addActionListener(e -> {
            try {
                count = Integer.parseInt(numberField.getText().trim());
                int width = count*64/10;
                mainPanel.setPreferredSize(new Dimension(width,maxY));
                numbers = new int[count];
                placeComponentsOnMainPanel(mainPanel, false);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, errorMessage);
            }
        });
        mainPanel.add(enterButton);
    }

    /**
     * Full fill main screen
     *
     * @param mainPanel
     * @param sorted
     */
    private void placeComponentsOnMainPanel(final JPanel mainPanel, boolean sorted) {
        mainPanel.setLayout(null);
        mainPanel.removeAll();

        Random random = new Random();

        if(!sorted) {
            for (int i = 0; i < count; i++) {
                numbers[i] = random.nextInt(maxNumberValue);
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
            randomNumber.setBounds(60*(i/10), 27*(1+i%10), generalWidth-55, generalHeight);
            addButtonListener(mainPanel, randomNumber);
        }

        addResetButton(mainPanel);
        addSortButton(mainPanel);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Entry point for Quick sort algorithm
     */
    public static void quickSort() {
        int startIndex = 0;
        int endIndex = count - 1;
        doSort(startIndex, endIndex);
    }

    /**
     *  Pick an element, called a pivot, from the array.
     *  Partitioning: reorder the array so that all elements with values less than the pivot come before the pivot,
     *      while all elements with values greater than the pivot come after it (equal values can go either way).
     *      After this partitioning, the pivot is in its final position. This is called the partition operation.
     *  Recursively apply the above steps to the sub-array of elements with smaller values and separately
     *      to the sub-array of elements with greater values.
     */
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

    /**
     * It makes the array in reverse order
     */
    private void reverse() {
        for(int i = 0; i < numbers.length / 2; i++) {
            int temp = numbers[i];
            numbers[i] = numbers[numbers.length - i - 1];
            numbers[numbers.length - i - 1] = temp;
        }
    }

    /**
     * Add "Sort" button
     *
     * @param mainPanel
     */
    private void addSortButton(final JPanel mainPanel) {
        JButton sortButton = new JButton(sortButtonText);
        sortButton.setBounds((maxX-generalWidth)-250, (maxY-generalHeight)-100, generalWidth, generalHeight);
        sortButton.addActionListener(e -> placeComponentsOnMainPanel(mainPanel,true));
        mainPanel.add(sortButton);
    }

    /**
     * Add "Reset" button
     *
     * @param mainPanel
     */
    private void addResetButton(final JPanel mainPanel) {
        JButton resetButton = new JButton(resetButtonText);
        resetButton.setBounds((maxX-generalWidth)-100, (maxY-generalHeight)-100, generalWidth, generalHeight);
        resetButton.addActionListener(e -> {
            isDescent = true;
            mainPanel.setPreferredSize(new Dimension(maxX-100,maxY));
            placeComponentsOnLoginPanel();
        });
        mainPanel.add(resetButton);
    }

    /**
     * Add listener to all buttons which contain numbers
     *
     * @param mainPanel
     * @param randomNumberButton
     */
    private void addButtonListener(final JPanel mainPanel, JButton randomNumberButton) {
        randomNumberButton.addActionListener(e -> {
            JButton button = (JButton) e.getSource();
            int num = Integer.parseInt(button.getText());
            if (num <= threshold){
                isDescent = true;
                placeComponentsOnMainPanel(mainPanel,false);
            } else {
                JOptionPane.showMessageDialog(null, suggestionMessage);
            }
        });
        mainPanel.add(randomNumberButton);
    }
}