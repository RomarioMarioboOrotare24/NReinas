import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class Reinas extends JFrame {

    private static final long serialVersionUID = 1L;

    private int NUM_REINAS;
    private int cont;
    private int[] solution;
    private final static int SIZE = 600;
    private JPanel panelBoard;
    private List<int[]> listSolutions;
    private JButton[][] btnCells;

    public Reinas(int NUM_REINAS) {
        this.NUM_REINAS = NUM_REINAS;
        this.cont = 0;
        solution = new int[NUM_REINAS];
        init();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(SIZE, SIZE);

        BorderLayout gestorLayout = new BorderLayout();
        setLayout(gestorLayout);

        panelBoard = new JPanel();
        listSolutions = new LinkedList<>();

        add(BorderLayout.NORTH, getOptions());
        add(BorderLayout.SOUTH, getResult());
        add(BorderLayout.CENTER, getBoard());

        setLocationRelativeTo(this);
        setVisible(true);

    }

    public JPanel getBoard() {
        panelBoard.removeAll();
        panelBoard.revalidate();
        panelBoard.repaint();
        btnCells = new JButton[NUM_REINAS][NUM_REINAS];
        GridLayout gestor = new GridLayout(NUM_REINAS, NUM_REINAS);
        panelBoard.setLayout(gestor);
        for (int i = 0; i < NUM_REINAS; i++) {
            for (int j = 0; j < NUM_REINAS; j++) {
                JButton cell = new JButton(" ");
                if ((i + j) % 2 == 0) {
                    cell.setBackground(Color.WHITE);
                } else {
                    cell.setBackground(Color.BLACK);
                }
                cell.setEnabled(false);
                btnCells[i][j] = cell;
                panelBoard.add(cell);
            }
        }
        panelBoard.revalidate();
        panelBoard.repaint();
        return panelBoard;
    }

    public JPanel getOptions() {
        JPanel panelNorth = new JPanel();
        JLabel lbnnumreinas = new JLabel("NUMERO DE REINAS: ");
        panelNorth.add(lbnnumreinas);
        JTextField txtNumReinas = new JTextField(10);
        panelNorth.add(txtNumReinas);
        JButton btnGo = new JButton("INICIAR");

        btnGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strNumReinas = txtNumReinas.getText();
                NUM_REINAS = Integer.parseInt(strNumReinas);
                solution = new int[NUM_REINAS];
                getBoard();

                searchSolution();
                getResult();
                paintSolution(listSolutions.get(cont));
            }
        });

        panelNorth.add(btnGo);
        return panelNorth;
    }

    public void paintSolution(int[] s) {
        for (int i = 0; i < s.length; i++) {
            btnCells[s[i]][i].setText("X");
        }
    }

    public void LimpiarPanel() {
        for (int i = 0; i < NUM_REINAS; i++) {
            for (int j = 0; j < NUM_REINAS; j++)
                btnCells[i][j].setText("");
        }
    }

    public JPanel getResult() {
        JPanel panelSouth = new JPanel();
        JLabel lbnmsjcantidad = new JLabel("CANTIDAD DE SOLUCIONES: ");
        JLabel lbnCantidad = new JLabel("0");
        JButton btnPreview = new JButton("<<");
        JLabel lbnNumSolCurrent = new JLabel("0");
        JButton btnNext = new JButton(">>");

        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cont > 0) {
                    lbnCantidad.setText(String.valueOf(listSolutions.size()));
                    LimpiarPanel();
                    paintSolution(listSolutions.get(cont - 1));
                    cont--;
                    lbnNumSolCurrent.setText(String.valueOf(cont));
                }
            }
        });
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cont < listSolutions.size() - 1) {
                    lbnCantidad.setText(String.valueOf(listSolutions.size()));
                    LimpiarPanel();
                    paintSolution(listSolutions.get(cont + 1));
                    cont++;
                    lbnNumSolCurrent.setText(String.valueOf(cont));
                }
            }
        });

        panelSouth.add(lbnmsjcantidad);
        panelSouth.add(lbnCantidad);
        panelSouth.add(btnPreview);
        panelSouth.add(lbnNumSolCurrent);
        panelSouth.add(btnNext);
        return panelSouth;
    }

    public void init() {
        for (int i = 0; i < solution.length; i++) {
            solution[i] = -1;
        }
    }

    public void searchSolution() {
        init();
        listSolutions.clear();
        backtracking(solution, 0);
    }

    public boolean backtracking(int[] solucion, int reina) {
        boolean success = false;
        if (reina < NUM_REINAS) {
            do {
                solucion[reina]++;
                boolean valid = isValid(solution, reina);
                if (valid) {
                    success = backtracking(solucion, reina + 1);
                    if (reina == NUM_REINAS - 1) {
                        String strSol = Arrays.toString(solucion);
                        System.out.println(strSol);
                        int[] sClone = solucion.clone();
                        listSolutions.add(sClone);
                    }
                }
            } while (solution[reina] < (NUM_REINAS - 1) && (!success));
            solucion[reina] = -1;
        } else {

        }
        return success;
    }

    public boolean isValid(int[] solution, int reina) {
        boolean ok = true;
        for (int i = 0; i < reina; i++) {
            if ((solution[i] == solution[reina]) || (Math.abs(solution[i] - solution[reina]) == Math.abs(i - reina))) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    public static void main(String[] args) {
        Reinas reina = new Reinas(4);

    }
}