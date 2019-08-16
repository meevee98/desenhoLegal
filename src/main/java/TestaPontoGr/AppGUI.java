package TestaPontoGr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class AppGUI extends JFrame {

    private JLabel msg = new JLabel("Msg: ");
    TiposPrimitivos tipo = TiposPrimitivos.NENHUM;

    private PainelDesenho areaDesenho = new PainelDesenho(msg, tipo);
    // barra de 
    private JToolBar barraComandos = new JToolBar();
    private JButton jbPontos = new JButton("Pontos");
    /*private JButton jbRetas = new JButton("Retas");
    private JButton jbCirculos = new JButton("Circulos");
    private JButton jbVogais = new JButton("Vogais");
    private JLabel jDummy = new JLabel(" >>>>> ");
    private JLabel jDiv = new JLabel("Divisoes para Retas: ");
    private JTextField jtfDivisoes = new JTextField("3");
*/
    public AppGUI(int larg, int alt) {
        /**
         * Definicoes de janela
         */
        super("Testa Primitivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(larg, alt);
        setVisible(true);

        // Adicionando os componentes
        barraComandos.add(jbPontos);
/*        barraComandos.add(jbRetas);
        barraComandos.add(jbCirculos);
        barraComandos.add(jbVogais);
        barraComandos.add(jDummy);
        barraComandos.add(jDiv);
        barraComandos.add(jtfDivisoes);
*/
        add(barraComandos, BorderLayout.NORTH);                
        add(areaDesenho, BorderLayout.CENTER);                
        add(msg, BorderLayout.SOUTH);

        Eventos eventos = new Eventos();
        jbPontos.addActionListener(eventos);
/*        jbRetas.addActionListener(eventos);
        jbCirculos.addActionListener(eventos);
        jbVogais.addActionListener(eventos);
        jtfDivisoes.addActionListener(eventos);
*/
    }

    private class Eventos implements ActionListener{

        TiposPrimitivos tipo = TiposPrimitivos.RETAS;

        public void actionPerformed(ActionEvent event) {            

            if (event.getSource() == jbPontos){
                tipo = TiposPrimitivos.PONTOS;
                //areaDesenho.repaint();
            }            
 /*           if (event.getSource() == jbRetas){
                tipo = TiposPrimitivos.RETAS;
                divisoes = Integer.parseInt(jtfDivisoes.getText());
                repaint();
            }            

            if (event.getSource() == jbCirculos){
                tipo = TiposPrimitivos.CIRCULOS;
                repaint();
            }
            if (event.getSource() == jbVogais){
                tipo = TiposPrimitivos.VOGAIS;
                repaint();
            }
            if (event.getSource() == jtfDivisoes){
                tipo = TiposPrimitivos.RETAS;
                divisoes = Integer.parseInt(jtfDivisoes.getText());
                repaint();
            }
*/
            // Enviando a Forma a ser desenhada e a cor da linha
            areaDesenho.setTipo( tipo );
        }
    } 
}
