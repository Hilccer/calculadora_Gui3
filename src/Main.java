import javax.swing.*; // Importa componentes gráficos do Swing
import java.awt.*; // Importa classes de layout e gráficos
import java.awt.event.*; // Importa eventos de interface
import java.util.List; // Importa interface de lista
import java.util.ArrayList; // Importa implementação ArrayList

public class Main {

    public static void main(String[] args) {

        //try (var conn = ConnectionFactory.getConnection()) {
         //   System.out.println("Conexão bem sucedida!");
         //} catch (Exception e) {
          //   e.printStackTrace();
         //}

        JFrame frame = new JFrame("Calculadora"); // Cria a janela principal com título "Calculadora"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
        frame.setSize(300, 400); // Define tamanho da janela

        JTextField textField = new JTextField(); // Campo de texto para mostrar expressão/resultado
        textField.setEditable(false); // Impede edição manual
        textField.setHorizontalAlignment(JTextField.RIGHT); // Alinha texto à direita
        textField.setPreferredSize(new Dimension(300, 50)); // Define tamanho preferido
        frame.add(textField, BorderLayout.NORTH); // Adiciona campo no topo da janela

        JPanel panel = new JPanel(); // Painel que conterá os botões
        panel.setLayout(new GridLayout(4, 4)); // Grade 4x4

        String[] botoes = { // Definição dos botões da calculadora
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };
        // PARA CADA TEXTO QUE TENHO NA LISTA DE BOTOES, EU CRIO UM BOTAO NOVO.
        for (String text : botoes) {
            JButton button = new JButton(text); // Cria botão com o texto correspondente
            panel.add(button); // Adiciona botão ao painel

            button.addActionListener(e -> { // Adiciona ação ao clicar no botão
                String comando = e.getActionCommand(); // Obtém o texto do botão clicado

                if (comando.equals("C")) { // Se for C, limpa o campo
                    textField.setText("");
                } else if (comando.equals("=")) { // Se for "=", calcula expressão
                    try {
                        String expressao = textField.getText(); // Pega o que foi digitado
                        double resultado = calcular(expressao); // Chama função de cálculo
                        textField.setText(String.valueOf(resultado)); // Mostra resultado

                        CalculadoraDAO sqlinsert = new CalculadoraDAO();
                        sqlinsert.salvarOperacao(expressao, resultado);

                    } catch (Exception ex) {
                        textField.setText("Erro"); // Mostra erro se não conseguir calcular
                    }
                } else {
                    textField.setText(textField.getText() + comando); // Adiciona número/operador no campo
                }
            });
        }

        frame.add(panel, BorderLayout.CENTER); // Adiciona painel de botões no centro
        frame.setVisible(true); // Torna a janela visível
    }

    public static double calcular(String expressao) {
        expressao = expressao.replaceAll("\\s+", ""); // Remove espaços da expressão

        List<Double> numeros = new ArrayList<>(); // Lista para armazenar números
        List<Character> operadores = new ArrayList<>(); // Lista para operadores

        String numeroBuffer = ""; // Buffer para acumular dígitos de um número

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i); // Pega caractere atual

            if (Character.isDigit(c) || c== '.') { // Se for dígito
                numeroBuffer += c; // Adiciona ao número em construção
            } else if (c == '+' || c == '-' || c == '*' || c == '/') { // Se for operador
                if (numeroBuffer.isEmpty()) { // Verifica se não há número antes do operador
                    throw new IllegalArgumentException("Número esperado antes do operador");
                }
                numeros.add(Double.parseDouble(numeroBuffer)); // Adiciona número à lista
                operadores.add(c); // Adiciona operador à lista
                numeroBuffer = ""; // Limpa buffer para próximo número
            } else {
                throw new IllegalArgumentException("Caractere inválido: " + c); // Erro para caracteres inválidos
            }
        }



        if (!numeroBuffer.isEmpty()) { // Adiciona último número se existir
            numeros.add(Double.parseDouble(numeroBuffer));
        }

        for (int i = 0; i < operadores.size(); i++) { // Primeiro resolve * e /
            char op = operadores.get(i);
            if (op == '*' || op == '/') {
                double a = numeros.get(i); // Número da esquerda
                double b = numeros.get(i + 1); // Número da direita
                double res;

                switch (op) {
                    case '*': res = a * b; break; // Multiplicação
                    case '/':
                        if (b == 0) throw new ArithmeticException("Divisão por zero"); // Protege divisão por zero
                        res = a / b; break; // Divisão
                    default: throw new IllegalStateException("Operador inesperado");
                }

                numeros.set(i, res); // Substitui pelo resultado
                numeros.remove(i + 1); // Remove número usado
                operadores.remove(i); // Remove operador usado
                i--; // Recuar índice para reavaliar posição
            }
        }

        double resultado = numeros.get(0); // Começa do primeiro número
        for (int i = 0; i < operadores.size(); i++) { // Resolve + e -
            char op = operadores.get(i);
            double b = numeros.get(i + 1);

            switch (op) {
                case '+': resultado += b; break; // Soma
                case '-': resultado -= b; break; // Subtração
                default: throw new IllegalStateException("Operador inesperado");
            }
        }

        return resultado; // Retorna resultado final definitivo3
    }

}
