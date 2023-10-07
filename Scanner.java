import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    private final String source;

    // Se crea un ArrayList de tokens llamado "tokens"
    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source + " ";
    }

    // Metodo que escanea sobre un archivo
    // En esta parte definiremos varios automatas que realizaran la tarea de
    // identificar que lexema es

    // ANALIZADOR LEXICO O SCANNER
    public List<Token> scan() throws Exception {

        // El estado inicial del automata es cero
        // Inicializamos una cadena llamado lexema
        // Definimos un caracter c, que formara parte del lexema
        int estado = 0;
        String lexema = "";
        char c;

        // Hasta que recorra toda la cadena
        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);

            switch (estado) {
                case 0:
                    if (Character.isLetter(c)) {
                        estado = 13;
                        lexema += c;
                    } else if (Character.isDigit(c)) {
                        estado = 15;
                        lexema += c;

                    }
                    break;

                case 13:
                    if (Character.isLetterOrDigit(c)) {
                        estado = 13;
                        lexema += c;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if (tt == null) {
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        } else {
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;

                    }
                    break;

                case 15:
                    if (Character.isDigit(c)) {
                        estado = 15;
                        lexema += c;
                    } else if (c == '.') {
                        estado = 16;
                        lexema += c;

                    } else if (c == 'E') {
                        estado = 18;
                        lexema += c;

                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

                case 16:
                    if (Character.isDigit(c)) {
                        estado = 17;
                        lexema += c;
                    }
                    break;
                case 17:
                        if(Character.isDigit(c)){
                            estado = 17;
                            lexema +=c;
                        }
                        else if(c == 'E'){
                            estado = 18;
                            lexema += c;
                        }
                        else{
                        Token t = new Token(TipoToken.NUMBER, lexema, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--; 
                        }
                        break;
                case 18: 
                       if(c == '+' || c == '-'){
                        estado = 19;
                        lexema +=c;
                       }
                       else if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                       }
                       break;
                case 19: 
                        if(Character.isDigit(c)){
                            estado = 20;
                            lexema +=c;
                        }
                        break;
                case 20:
                        if(Character.isDigit(c)){
                            estado = 20;
                            lexema +=c;
                        }
                        else{
                        Token t = new Token(TipoToken.NUMBER, lexema, lexema);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--; 
                        }

            }
            // Autómatas de comentarios, no genera token
            switch (estado)
             {
                case 0:
                    if (c == '/')
                        estado = 26;
                    break;
                case 26:
                    if (c == '/') {
                        System.out.println("comentario de una linea");
                        estado = 27;
                    } else if (c == '*') {
                        System.out.println("Comentario multilinea");
                        estado = 28;
                    }
                    break;
                case 27:

                    if (c == '\n') {
                        estado = 0;
                    } else
                        estado = 27;
                    break;

                case 28:
                    if (c == '/') {
                        estado = 0;
                    } else
                        estado = 28;
                    break;
            }

            // Autómata de reconocimiento de cadenas
            switch (estado) {
                case 0:
                    if (c == '"') {
                        estado = 24;
                    }
                    break;

                case 24:
                    if (c == '"') {
                        estado = 25;
                    } else {
                        estado = 24;
                        lexema += c;
                    }
                    break;

                case 25:
                    Token t = new Token(TipoToken.STRING, lexema,lexema);
                    tokens.add(t);
                    /*
                     * Para el caso de las cadenas, el atributo literal
                     * debe contener la cadena detectada, pero sin las comillas inicial y final.
                     */
                    estado = 0;
                    lexema = "";

                    break;
            }
            // Autómata de reconocimiento de símbolos
            switch (estado) {
                case 0:
                    if (c == '>') {
                        estado = 1;
                        lexema += c;
                    } else if (c == '<') {
                        estado = 4;
                        lexema += c;
                    } else if (c == '=') {
                        estado = 7;
                        lexema += c;
                    } else if (c == '!') {
                        estado = 10;
                        lexema += c;
                    } else if (c == '(') {
                        estado = 45;
                        lexema += c;

                    } else if (c == ')') {
                        estado = 46;
                        lexema += c;
                    } else if (c == '{') {
                        estado = 47;
                        lexema += c;
                    } else if (c == '}') {
                        estado = 48;
                        lexema += c;
                    } else if (c == ',') {
                        estado = 49;
                        lexema += c;
                    } else if (c == ';') {
                        estado = 50;
                        lexema += c;
                    } else if (c == '.') {
                        estado = 51;
                        lexema += c;
                    }
                    break;
                case 1:
                    if (c == '=') {
                        estado = 2;
                        lexema += c;
                    }
                    break;
                case 4:
                    if (c == '=') {
                        estado = 5;
                        lexema += c;
                    } else {
                        estado = 6;
                    }
                    break;
                case 10:
                    if (c == '=') {
                        estado = 11;
                        lexema += c;
                    } else {
                        estado = 12;

                    }
                    break;
                case 2:
                    Token a = new Token(TipoToken.GREATER_EQUAL, lexema);
                    tokens.add(a);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 3:
                    Token b = new Token(TipoToken.GREATER, lexema);
                    tokens.add(b);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 5: {
                    Token h = new Token(TipoToken.LESS_EQUAL, lexema);
                    tokens.add(h);
                }
                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 7:
                    if (c == '=') {
                        estado = 8;
                        lexema += c;
                    } else {
                        estado = 9;
                        lexema += c;
                    }
                    break;
                case 8:

                    Token d = new Token(TipoToken.EQUAL_EQUAL, lexema);
                    tokens.add(d);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 9:
                    Token e = new Token(TipoToken.EQUAL, lexema);
                    tokens.add(e);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 11:
                    Token f = new Token(TipoToken.BANG_EQUAL, lexema);
                    tokens.add(f);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 12:
                    Token g = new Token(TipoToken.BANG, lexema);
                    tokens.add(g);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 45:
                    Token h = new Token(TipoToken.LEFT_PAREN, lexema);
                    tokens.add(h);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 46:
                    Token j = new Token(TipoToken.RIGHT_PAREN, lexema);
                    tokens.add(j);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 47:
                    Token k = new Token(TipoToken.LEFT_BRACE, lexema);
                    tokens.add(k);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 48:

                    Token l = new Token(TipoToken.RIGHT_BRACE, lexema);
                    tokens.add(l);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

                case 49:

                    Token m = new Token(TipoToken.COMMA, lexema);
                    tokens.add(m);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 50:

                    Token n = new Token(TipoToken.SEMICOLON, lexema);
                    tokens.add(n);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;
                case 51:

                    Token p = new Token(TipoToken.DOT, lexema);
                    tokens.add(p);
                    estado = 0;
                    lexema = "";
                    i--;
                    break;

            }

        }

        return tokens;

    }

}